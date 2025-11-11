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
import com.group7.medicationadherenceapp.ui.theme.MedicationAdherenceAppTheme
import androidx.compose.material3.OutlinedButton
import android.content.pm.ApplicationInfo
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    viewModel: LoginViewModel = viewModel()
    onDevCaregiverClick: (() -> Unit)? = null
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

 
    LaunchedEffect(Unit) {
    }
    // ✅ define debuggable flag
    val context = LocalContext.current
    val isDebuggable =
        (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

    Column(
        modifier = Modifier
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
            onLoginClick()
        }) {
            Text("Login")
        }

// Dev-only Caregiver shortcut (no BuildConfig needed)
        if (isDebuggable && onDevCaregiverClick != null) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(onClick = { onDevCaregiverClick() }) {
                Text("Dev → Caregiver")
            }
        }
    }
}

        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            // Handle sign up
        }) {
            Text("I don't have an account")
        }
    }
}