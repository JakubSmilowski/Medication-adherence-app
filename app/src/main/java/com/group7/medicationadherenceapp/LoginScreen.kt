package com.group7.medicationadherenceapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    // Collect the state from ViewModel
    val allUsers by viewModel.allUsers.collectAsState()
    val testUser by viewModel.testUser.collectAsState()

    val allMedication by viewModel.allMedication.collectAsState()
    val testMedication by viewModel.testMedication.collectAsState()

    // Load users when screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.loadAllUsers()
        viewModel.loadAllMedication()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display test user data
        if (testUser != null) {
            Text(
                text = "Test User in DB:",
                color = Color.Blue
            )
            Text(
                text = "Name: ${testUser?.firstName} ${testUser?.lastName}",
                color = Color.Blue
            )
            Text(
                text = "Username: ${testUser?.username}",
                color = Color.Blue
            )
            Text(
                text = "Total users: ${allUsers.size}",
                color = Color.Blue
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Test button to insert user
        Button(onClick = {
            viewModel.insertTestUser()
        }) {
            Text("Insert Test User")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display test user data
        if (testMedication != null) {
            Text(
                text = "Test Medication in DB:",
                color = Color.Blue
            )
            Text(
                text = "Name: ${testMedication?.name} ${testMedication?.description}",
                color = Color.Blue
            )
            Text(
                text = "Username: ${testMedication?.dosage}",
                color = Color.Blue
            )
            Text(
                text = "Total users: ${allMedication.size}",
                color = Color.Blue
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Test button to insert Medication
        Button(onClick = {
            viewModel.insertTestMedication()
        }) {
            Text("Insert Test Medication")
        }

        Spacer(modifier = Modifier.height(16.dp))



        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (showError) {
            Text(
                text = "Invalid username or password",
                color = Color.Red
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            viewModel.login(
                username = username,
                password = password,
                onSuccess = {
                    onLoginClick()
                    showError = false
                },
                onError = {
                    showError = true
                }
            )
        }) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            // Handle sign up
        }) {
            Text("I don't have an account")
        }
    }
}