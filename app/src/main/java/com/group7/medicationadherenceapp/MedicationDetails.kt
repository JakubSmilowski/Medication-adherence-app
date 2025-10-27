package com.group7.medicationadherenceapp
//CONSOLIDATED UI & LAYOUT
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group7.medicationadherenceapp.ui.theme.MedicationAdherenceAppTheme

// --- Helper Composable for the Main Information Rows (Medication Name, Dosage, Reminder) ---
@Composable
fun InfoRow(
    label: String,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.width(8.dp))
        content()
    }
}

// --- Helper Composable for the Past Doses Entry ---
@Composable
fun PastDoseEntry(date: String, time: String, isTaken: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AssistChip(onClick = { /* Not clickable in design */ }, label = { Text(date) })
            Spacer(Modifier.width(8.dp))
            AssistChip(onClick = { /* Not clickable in design */ }, label = { Text(time) })
        }
        Switch(
            checked = isTaken,
            onCheckedChange = onToggle
        )
    }
}

// Main Screen Composable
@Composable
fun MedicationDetailContent(medicationName: String) {
    // State for the Reminder switch
    var isReminderOn by remember { mutableStateOf(true) }
    // State for the Past Dose switches
    var pastDose1Taken by remember { mutableStateOf(true) }
    var pastDose2Taken by remember { mutableStateOf(true) }
    // State for the Notes field
    var notesText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
            // "Medication X details" Title
            Text(
                text = "$medicationName details",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            Divider(Modifier.padding(horizontal = 16.dp))

            // Medication Name Row
            InfoRow(label = "Medication name") {
                // The name is usually displayed here, but since it's the title of the screen,
                // we can just put a placeholder
            }

            Divider(Modifier.padding(horizontal = 16.dp))

            // Dosage Row
            InfoRow(label = "Dosage") {
                Text(
                    text = "x mg",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Divider(Modifier.padding(horizontal = 16.dp))

            // Reminder Row
            InfoRow(label = "Reminder") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AssistChip(onClick = { /* Time picker */ }, label = { Text("9:41 AM") })
                    Spacer(Modifier.width(8.dp))
                    Switch(
                        checked = isReminderOn,
                        onCheckedChange = { isReminderOn = it }
                    )
                }
            }

            Divider(Modifier.padding(horizontal = 16.dp))

            // "Past doses" Header
            Text(
                text = "Past doses",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, bottom = 8.dp)
            )

            // Past Doses Entries
            PastDoseEntry(
                date = "Apr 1, 2025",
                time = "9:41 AM",
                isTaken = pastDose1Taken,
                onToggle = { pastDose1Taken = it }
            )
            PastDoseEntry(
                date = "Apr 1, 2025",
                time = "9:41 AM",
                isTaken = pastDose2Taken,
                onToggle = { pastDose2Taken = it }
            )

        Divider(Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp))

            // "Notes" Header and Text Field
            Text(
                text = "Notes",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, bottom = 8.dp)
            )

        OutlinedTextField(
            value = notesText,onValueChange = { notesText = it },
            label = { Text("Add any notes here...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp) // Apply padding first
                .height(150.dp),                              // Then set the height
            singleLine = false,
            maxLines = 5
        )
        }
    }

// Preview Composable
@Preview(showBackground = true)
@Composable
fun MedicationDetailPreview() {
    MedicationAdherenceAppTheme {
        MedicationDetailContent(
            medicationName = "Medication 1"
        )
    }
}