package com.group7.medicationadherenceapp

/**
 * This should work rn, if not, ima kill myself
 * */

import android.content.pm.ApplicationInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginClick: (UserRole, Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: LoginViewModel = viewModel(),
    onDevCaregiverClick: (() -> Unit)? = null
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val isDebuggable =
        (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Login") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                scope.launch {
                    val user = viewModel.login(username, password)
                    if (user != null) {
                        println("DEBUG_USER: Successfully logged in. User ID is: ${user.uid}")
                        val role = if (user.patient == true) UserRole.PATIENT else UserRole.CAREGIVER
                        onLoginClick(role, user.uid)
                    } else {
                        showError = true
                    }
                }
            }) {
                Text("Login")
            }

            if (isDebuggable && onDevCaregiverClick != null) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(onClick = { onDevCaregiverClick() }) {
                    Text("Dev → Caregiver")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = {

            }) {
                Text("I don't have an account")
            }
        }
    }
}