package com.group7.medicationadherenceapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.group7.medicationadherenceapp.ui.theme.MedicationAdherenceAppTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedicationAdherenceAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(onLoginClick = {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            })
                        }
                        composable("home") {
                            HomeScreen(navController)
                        }
                        composable("medicationDetails/{medName}") { backStackEntry ->
                            val medName = backStackEntry.arguments?.getString("medName") ?: ""
                            MedicationDetailScreen(
                                medicationName = medName,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MedicationRow(
    medicationName: String,
    onMedicationClick: () -> Unit,
    onDateClick: () -> Unit,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = onMedicationClick,
            modifier = Modifier.weight(3f)
        ) {
            Text(medicationName)
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = onDateClick,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = "Select Date"
            )
        }

        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun BottomBarItem(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var isMedication1Taken by remember { mutableStateOf(false) }
    var isMedication2Taken by remember { mutableStateOf(false) }
    var isMedication3Taken by remember { mutableStateOf(false) }

    val totalMeds = 3
    val completedMeds = listOf(isMedication1Taken, isMedication2Taken, isMedication3Taken).count { it }
    val progress = if (totalMeds > 0) completedMeds.toFloat() / totalMeds.toFloat() else 0f
    val progressPercent = (progress * 100).toInt()

    val context = LocalContext.current
    var selectedMedicationName by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("N/A") }
    var selectedTime by remember { mutableStateOf("N/A") }

    val timeCalendar = Calendar.getInstance()
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
            println("Medication: $selectedMedicationName, Date: $selectedDate, Time: $selectedTime")
        },
        timeCalendar.get(Calendar.HOUR_OF_DAY),
        timeCalendar.get(Calendar.MINUTE),
        false
    )

    val showTimePicker: () -> Unit = { timePickerDialog.show() }

    val dateCalendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
            showTimePicker()
        },
        dateCalendar.get(Calendar.YEAR),
        dateCalendar.get(Calendar.MONTH),
        dateCalendar.get(Calendar.DAY_OF_MONTH)
    )

    val showDatePicker: (String) -> Unit = { medName ->
        selectedMedicationName = medName
        datePickerDialog.show()
    }

    val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text("Welcome, Patient")
                        Text(currentDate, style = MaterialTheme.typography.bodySmall)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    BottomBarItem(icon = Icons.Filled.Home, contentDescription = "Home", onClick = { /* Already home */ })
                    BottomBarItem(icon = Icons.Filled.DateRange, contentDescription = "History", onClick = { /* Navigate to History */ })
                    BottomBarItem(icon = Icons.Filled.Person, contentDescription = "Profile", onClick = { context.startActivity(Intent(context, ProfileActivity::class.java)) })
                    BottomBarItem(icon = Icons.Filled.Settings, contentDescription = "Settings", onClick = { /* Navigate to Settings */ })
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MedicationRow(
                medicationName = "Medication 1",
                onMedicationClick = { navController.navigate("medicationDetails/Medication 1") },
                onDateClick = { showDatePicker("Medication 1") },
                isChecked = isMedication1Taken,
                onCheckedChange = { isMedication1Taken = it }
            )
            MedicationRow(
                medicationName = "Medication 2",
                onMedicationClick = { navController.navigate("medicationDetails/Medication 2") },
                onDateClick = { showDatePicker("Medication 2") },
                isChecked = isMedication2Taken,
                onCheckedChange = { isMedication2Taken = it }
            )
            MedicationRow(
                medicationName = "Medication 3",
                onMedicationClick = { navController.navigate(
                "medicationDetails/Medication 3") },
                onDateClick = { showDatePicker("Medication 3") },
                isChecked = isMedication3Taken,
                onCheckedChange = { isMedication3Taken = it }
            )

            Text(
                text = "Last Selection: $selectedMedicationName on $selectedDate at $selectedTime",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* TODO: Implement logic to add new medication */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text("Add Medication")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    text = "Daily Progress: $completedMeds of $totalMeds ($progressPercent%)",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(
                onClick = { /* TODO: Contact Caregiver */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text("Contact Caregiver")
            }

            OutlinedButton(
                onClick = { /* TODO: Contact Doctor */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text("Contact Doctor")
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    MedicationAdherenceAppTheme {
        HomeScreen(navController)
    }
}
