package com.group7.medicationadherenceapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.group7.medicationadherenceapp.navigation.Dest
import com.group7.medicationadherenceapp.ui.theme.MedicationAdherenceAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(nav: NavController) {

    var deleteAccountIsToggled by rememberSaveable { mutableStateOf(false) }

    var areNotificationsMuted by rememberSaveable { mutableStateOf(false) }
    var notifyCaretaker by rememberSaveable { mutableStateOf(true) }
    var isDarkMode by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "General",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Profile",
                    modifier = Modifier.clickable { nav.navigate(Dest.PROFILE) },
                    style = MaterialTheme.typography.bodyLarge
                )
                Row {
                    Text("Email: ", style = MaterialTheme.typography.bodyLarge)
                    Text("xxx@gmail.com", style = MaterialTheme.typography.bodyLarge) // Placeholder for actual email
                }
                Row {
                    Text("Caretaker: ", style = MaterialTheme.typography.bodyLarge)
                    Text("xxxx xxxxx", style = MaterialTheme.typography.bodyLarge) // Placeholder for actual caretaker name
                }
                Text("About", style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Notifications",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Mute all notifications", style = MaterialTheme.typography.bodyLarge)
                    Switch(
                        checked = areNotificationsMuted,
                        onCheckedChange = { areNotificationsMuted = it }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Sound: ", style = MaterialTheme.typography.bodyLarge)
                    Text("Default", style = MaterialTheme.typography.bodyLarge) // Placeholder for dropdown
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Notify caretaker of medication intake", style = MaterialTheme.typography.bodyLarge)
                    Switch(
                        checked = notifyCaretaker,
                        onCheckedChange = { notifyCaretaker = it }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))


                Text(
                    text = "Accessibility",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Text size", style = MaterialTheme.typography.bodyLarge)
                    Text("Medium", style = MaterialTheme.typography.bodyLarge) // Placeholder for dropdown
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Dark mode", style = MaterialTheme.typography.bodyLarge)
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { isDarkMode = it }
                    )
                }
            }

            // BOTTOM BUTTONS
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        deleteAccountIsToggled = !deleteAccountIsToggled
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.onError,
                        containerColor = MaterialTheme.colorScheme.error,
                    )
                ) {
                    Text("Delete account")
                }
                OutlinedButton(
                    onClick = { nav.navigate(Dest.LOGIN) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Log-out")
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Settings Screen Preview")
@Composable
fun SettingsScreenPreview() {
    MedicationAdherenceAppTheme {
        SettingsScreen(nav = rememberNavController())
    }
}
