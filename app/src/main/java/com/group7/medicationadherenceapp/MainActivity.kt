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
    //Checkbox needed
    var isMedication1Taken by remember { mutableStateOf(false) }
    var isMedication2Taken by remember { mutableStateOf(false) }
    var isMedication3Taken by remember { mutableStateOf(false) }

    //Daily Progress
    val totalMeds = 3
    val completedMeds = listOf(isMedication1Taken, isMedication2Taken).count { it }
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
                        onClick = { /* Navigate Home */ }
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
                onDateClick = { /* TODO: Open Date Picker Dialog */ },
                isChecked = isMedication1Taken,
                onCheckedChange = { isMedication1Taken = it }
            )
            MedicationRow(
                medicationName = "Medication 2",
                onMedicationClick = { /* TODO: Navigate to M2 Details */ },
                onDateClick = { /* TODO: Open Date Picker Dialog */ },
                isChecked = isMedication2Taken,
                onCheckedChange = { isMedication2Taken = it }
            )
            MedicationRow(
                medicationName = "Medication 3",
                onMedicationClick = { /* TODO: Navigate to M3 Details */ },
                onDateClick = { /* TODO: Open Date Picker Dialog */ },
                isChecked = isMedication3Taken,
                onCheckedChange = { isMedication3Taken = it }
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
