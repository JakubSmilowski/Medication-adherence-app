package com.group7.medicationadherenceapp

import android.R
import android.provider.CalendarContract
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.modifier.modifierLocalConsumer
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
                TextButton (
                    onClick = { nav.navigate(Dest.PROFILE) }
                ) {
                    Text("Profile")
                }

                Text("Email")
                Text("Caretaker: ") //Here caretaker will be displaied
                Text("About")
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )
                Text(
                    text = "Notifications",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                ) // Headder
                Text("Notifications - mutes all notifications ") // On/off
                Text("Sound: ")
                Text("Notify caretaker of medication intake.")
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )
                Text(
                    text = "Accessibliyty",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text("Text size")
                Text("Dark mode") // button or smth
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )


            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,

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
