package com.group7.medicationadherenceapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
//CONSOLIDATED UI & LAYOUT
import androidx.compose.foundation.layout.* // Covers Row, Column, Spacer, padding, fillMaxSize, fillMaxWidth, width, size, Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.* // Covers Home, Restore, Person, Settings, DateRange
import androidx.compose.material3.* // Covers Scaffold, TopAppBar, BottomAppBar, Button, OutlinedButton, Text, MaterialTheme, Icon, IconButton, Checkbox, LinearProgressIndicator, etc.
import androidx.compose.runtime.* // Covers Composable, remember, mutableStateOf, getValue, setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector // Specific UI/Graphics type


//Needed for data picking
import androidx.compose.ui.platform.LocalContext
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import java.util.Calendar
import java.util.Locale

// KOTLIN AND JAVA
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.group7.medicationadherenceapp.ui.theme.MedicationAdherenceAppTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedicationAdherenceAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
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

    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    // State variables for dialogs
    val context = LocalContext.current
    var selectedMedicationName by remember { mutableStateOf("") }

    // State for the selected date and time (for demo purposes)
    var selectedDate by remember { mutableStateOf("N/A") }
    var selectedTime by remember { mutableStateOf("N/A") }

    val timeCalendar = Calendar.getInstance()
    // 2. Define the Time Picker Dialog (must come first)
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
            // Log the final date/time
            println("Medication: $selectedMedicationName, Date: $selectedDate, Time: $selectedTime")
        },
        timeCalendar.get(Calendar.HOUR_OF_DAY),
        timeCalendar.get(Calendar.MINUTE),
        false // Set to true for 24-hour clock
    )

    // Helper function to show the Time Picker (now correctly defined)
    val showTimePicker: () -> Unit = {
        timePickerDialog.show()
    }

    val dateCalendar = Calendar.getInstance()
    // 1. Define the Date Picker Dialog, referencing the now-defined showTimePicker
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            // Date selected successfully:
            selectedDate = "$dayOfMonth/${month + 1}/$year"
            // FIX APPLIED: showTimePicker() is now in scope and callable.
            showTimePicker()
        },
        dateCalendar.get(Calendar.YEAR),
        dateCalendar.get(Calendar.MONTH),
        dateCalendar.get(Calendar.DAY_OF_MONTH)
    )

    // Helper function to show the Date Picker
    val showDatePicker: (String) -> Unit = { medName ->
        selectedMedicationName = medName
        datePickerDialog.show()
    }
    //Checkbox needed
    var isMedication1Taken by remember { mutableStateOf(false) }
    var isMedication2Taken by remember { mutableStateOf(false) }
    var isMedication3Taken by remember { mutableStateOf(false) }

    //Daily Progress
    val totalMeds = 3
    val completedMeds = listOf(isMedication1Taken, isMedication2Taken, isMedication3Taken).count { it }
    val progress = remember(completedMeds) {
        if (totalMeds == 0) 0f else completedMeds.toFloat() / totalMeds.toFloat()
    }
    val progressPercent = (progress * 100).toInt()

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
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    // Home Button
                    BottomBarItem(
                        icon = Icons.Filled.Home,
                        contentDescription = "Home",
                        onClick = { println("Home button clicked: Stay on Home Screen") }
                    )

                    // History Button
                    BottomBarItem(
                        icon = Icons.Filled.DateRange,
                        contentDescription = "History",
                        onClick = { /* Navigate to History */ }
                    )

                    // Profile Button
                    BottomBarItem(
                        icon = Icons.Filled.Person,
                        contentDescription = "Profile",
                        onClick = { /* Navigate to Profile */ }
                    )

                    // Settings Button
                    BottomBarItem(
                        icon = Icons.Filled.Settings,
                        contentDescription = "Settings",
                        onClick = { /* Navigate to Settings */ }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            //verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MedicationRow(
                medicationName = "Medication 1",
                onMedicationClick = { /* TODO: Navigate to M1 Details */ },
                onDateClick = { showDatePicker("Medication 1") },
                isChecked = isMedication1Taken,
                onCheckedChange = { isMedication1Taken = it }
            )
            MedicationRow(
                medicationName = "Medication 2",
                onMedicationClick = { /* TODO: Navigate to M2 Details */ },
                onDateClick = { showDatePicker("Medication 2") },
                isChecked = isMedication2Taken,
                onCheckedChange = { isMedication2Taken = it }
            )
            MedicationRow(
                medicationName = "Medication 3",
                onMedicationClick = { /* TODO: Navigate to M3 Details */ },
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
                    progress = progress, // Uses the calculated progress float
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
                    .padding(horizontal = 32.dp)// vertical = 8.dp)
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
    MedicationAdherenceAppTheme {
        HomeScreen()
    }
}
