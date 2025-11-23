package com.group7.medicationadherenceapp

import android.app.Application
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.group7.medicationadherenceapp.navigation.Dest
import com.group7.medicationadherenceapp.ui.theme.MedicationAdherenceAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(nav: NavController) {

    // This state is saved across configuration changes (like rotation) and process death.
    var deleteAccountIsToggled by rememberSaveable { mutableStateOf(false) }
    var areNotificationsMuted by rememberSaveable { mutableStateOf(false) }
    var notifyCaretaker by rememberSaveable { mutableStateOf(true) }

    // --- EXAM CONCEPT: VIEWMODEL INSTANTIATION WITH A FACTORY ---
    // We get an instance of our SettingsViewModel here. Because the ViewModel has dependencies
    // (the UserPreferencesRepository), we must provide a custom factory to tell the system how to create it.
    // This is a core part of manual Dependency Injection.
    val viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(nav.context.applicationContext as Application)
    )

    // --- EXAM CONCEPT: OBSERVING STATEFLOW IN THE UI (STATE CONSUMPTION) ---
    // `collectAsState()` is the key Composable function to observe a StateFlow from a ViewModel.
    // It collects emissions from the Flow and represents the latest value as Compose State.
    // Whenever `isDarkMode` in the ViewModel's StateFlow changes (because the DataStore was updated),
    // this Composable and any others that read `isDarkMode` will automatically recompose.
    val isDarkMode by viewModel.isDarkMode.collectAsState()

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
                    Text("Dark mode", style = MaterialTheme.typography.bodyLarge)
                    // --- EXAM CONCEPT: UNIDIRECTIONAL DATA FLOW (UDF) IN PRACTICE ---
                    // The Switch is a perfect example of UDF.
                    // 1. STATE FLOWS DOWN: The `checked` state is read directly from `isDarkMode`, which comes
                    //    from the ViewModel. The UI is a direct reflection of the state.
                    // 2. EVENTS FLOW UP: When the user toggles the switch, the `onCheckedChange` lambda is triggered.
                    //    It doesn't change a local state variable here. Instead, it calls the `setDarkMode`
                    //    function on the ViewModel, sending the event "up" to the state holder.
                    // The ViewModel then processes this event, updates the DataStore, which in turn causes the
                    // StateFlow to emit a new value, and the new state flows back down to the UI, completing the cycle.
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { newCheckedState ->
                            viewModel.setDarkMode(newCheckedState)
                        }
                    )
                }
            }

            // --- Bottom Buttons ---
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
                        contentColor = MaterialTheme.colorScheme.onError, // White text for contrast on a red button
                        containerColor = MaterialTheme.colorScheme.error,   // Red background to indicate a destructive action
                    )
                ) {
                    Text("Delete account")
                }
                OutlinedButton(
                    onClick = {
                        // EXAM CONCEPT: MANIPULATING THE NAVIGATION BACK STACK
                        // When logging out, we don't just navigate to "login". We also need to clear
                        // the entire back stack so the user cannot press the back button to return
                        // to a screen they are no longer authenticated for.
                        // `popUpTo(0)` clears the whole stack.
                        nav.navigate(Dest.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
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
        // The preview uses a `rememberNavController()` which is a dummy controller
        // that allows the preview to build without crashing.
        SettingsScreen(nav = rememberNavController())
    }
}
