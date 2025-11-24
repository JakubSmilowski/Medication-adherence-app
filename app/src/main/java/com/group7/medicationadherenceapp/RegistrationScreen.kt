package com.group7.medicationadherenceapp

/**
 *
 * This mf was actually a lot harder than it looks
 *
 *
 *  -- Good job!
 * */


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Marks this function as a UI component that can be used to build the screen.
fun RegistrationScreen(
    // A lambda function passed from the navigation graph. It's called when registration is successful.
    // It passes the role of the new user so the app can navigate to the correct screen.
    onRegistrationComplete: (role: UserRole) -> Unit,
    // A lambda function to handle the back button click in the TopAppBar.
    onBackClick: () -> Unit,
    // Gets an instance of the LoginViewModel. The `viewModel()` helper handles creating it
    // and ensuring it survives screen rotations and other configuration changes.
    viewModel: LoginViewModel = viewModel()
) {
    // `remember` and `mutableStateOf` create state variables that Compose will track.
    // When any of these variables change (e.g., the user types in a text field),
    // Compose will automatically update (recompose) the parts of the UI that use them.

    var username by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPatient by remember { mutableStateOf(true) }
    var showError by remember { mutableStateOf(false) }// State to control the visibility of the error message.

    // `rememberCoroutineScope` gets a coroutine scope that is tied to this composable's lifecycle.
    // It's used to launch long-running tasks (like a database write) without blocking the UI thread.
    val scope = rememberCoroutineScope()

    // `Scaffold` provides the basic layout structure for the screen (e.g., top bar, main content area).
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Register") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isPatient,
                    onCheckedChange = { isPatient = it }
                )
                Text("Patient?")
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (showError) {
                Text(
                    text = "Username or email already taken.",
                    color = Color.Red
                )
            }

            Button(onClick = {
                scope.launch {
                    val success = viewModel.register(
                        username,
                        firstName,
                        lastName,
                        email,
                        password,
                        isPatient
                    )
                    if (success) {
                        onRegistrationComplete(UserRole.CAREGIVER)
                    } else {
                        showError = true
                    }
                }
            }) {
                Text("Register")
            }
        }
    }
}