// This line declares the package name, which helps in organizing the code into logical groups.// All files related to the main application features are likely in this package.
package com.group7.medicationadherenceapp

// A developer's personal comment, often for motivation or humor during development.
/**
 * This should work rn, if not, ima kill myself
 * */

// Imports from the Android framework.
import android.content.pm.ApplicationInfo // Used to inspect the application's metadata, like its debug status.

// Imports from Jetpack Compose for building the UI. These are the building blocks of the screen.
import androidx.compose.foundation.layout.Arrangement // For arranging components within a layout (e.g., centering).
import androidx.compose.foundation.layout.Column // A layout composable that places its children in a vertical sequence.
import androidx.compose.foundation.layout.Spacer // An empty composable used to create space between other elements.
import androidx.compose.foundation.layout.fillMaxSize // A modifier to make a component take up all available space.
import androidx.compose.foundation.layout.height // A modifier to set the height of a component.
import androidx.compose.foundation.layout.padding // A modifier to add space around a component.
import androidx.compose.material.icons.Icons // Provides access to the default Material Design icons.
import androidx.compose.material.icons.filled.ArrowBack // The specific "back arrow" icon.
import androidx.compose.material3.Button // A standard, filled Material Design button.
import androidx.compose.material3.ExperimentalMaterial3Api // An annotation to opt into using experimental Material 3 APIs.
import androidx.compose.material3.Icon // A composable for displaying an icon.
import androidx.compose.material3.IconButton // A button that contains an icon.
import androidx.compose.material3.OutlinedButton // A button with a transparent background and a visible border.
import androidx.compose.material3.OutlinedTextField // A text input field with a border.
import androidx.compose.material3.Scaffold // A layout structure providing slots for top/bottom bars, drawers, etc.
import androidx.compose.material3.Text // A composable for displaying text.
import androidx.compose.material3.TopAppBar // The bar displayed at the top of the screen.
import androidx.compose.runtime.* // Core Compose runtime functions like `remember`, `mutableStateOf`, `Composable`.
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment // For aligning components within their parent layout.
import androidx.compose.ui.Modifier // A collection of decorators that modify a composable's appearance or behavior.
import androidx.compose.ui.graphics.Color // Represents a color, used here for the error message.
import androidx.compose.ui.platform.LocalContext // Provides the current Android `Context`.
import androidx.compose.ui.unit.dp // A unit for specifying size and padding in a density-independent way.

// Imports for Architecture Components.
import androidx.lifecycle.viewmodel.compose.viewModel // A function to get a `ViewModel` instance within a Composable.
import androidx.navigation.NavController // The component responsible for navigating between screens.

// Import for Kotlin Coroutines, used for running asynchronous operations like logging in.
import kotlinx.coroutines.launch

/**
 * This annotation is required because some Material 3 components like Scaffold and TopAppBar
 * are still considered "experimental" and their APIs might change in the future.
 * This tells the compiler that we are aware of this and are opting in to use them.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable // Marks this function as a Jetpack Compose UI component.
fun LoginScreen(
    // A lambda function that is called when the login is successful. It passes the role of the logged-in user.
    onLoginClick: (role: UserRole) -> Unit,
    // A lambda function that is called when the back button in the TopAppBar is clicked.
    onBackClick: () -> Unit,
    // Injects the LoginViewModel. `viewModel()` is a helper that gets or creates the ViewModel for the current scope.
    viewModel: LoginViewModel = viewModel(),
    // An optional lambda for a developer-only shortcut to navigate directly to the caregiver screen.
    onDevCaregiverClick: (() -> Unit)? = null
) {
    // `remember` and `mutableStateOf` create a state variable that Compose can track.
    // When the value of `username` changes, any part of the UI that uses it will automatically update.
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) } // State to control the visibility of the error message.
    val scope = rememberCoroutineScope() // Gets a coroutine scope tied to this composable's lifecycle.

    // Gets the current Android context, needed to access application information.
    val context = LocalContext.current
    // Checks if the app is running in a debug build. This is used to show/hide developer-only UI.
    val isDebuggable =
        (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

    // Scaffold provides the basic layout structure (top bar, content area).
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Login") },
                navigationIcon = {
                    // An icon button that triggers the `onBackClick` lambda when pressed.
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding -> // `innerPadding` is provided by the Scaffold to avoid content overlapping with the bars.
        // A Column arranges its children vertically.
        Column(
            modifier = Modifier
                .padding(innerPadding) // Apply padding to respect the Scaffold's bars.
                .fillMaxSize()         // Make the column fill the entire screen.
                .padding(16.dp),       // Add extra padding around the content.
            horizontalAlignment = Alignment.CenterHorizontally, // Center all children horizontally.
            verticalArrangement = Arrangement.Center          // Center the entire block of children vertically.
        ) {

            OutlinedTextField(
                value = username, // The current text in the field.
                onValueChange = { username = it }, // This lambda is called every time the user types, updating the state.
                label = { Text("Username") } // The placeholder label for the text field.
            )
            Spacer(modifier = Modifier.height(8.dp)) // A small vertical gap.
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
            )
            Spacer(modifier = Modifier.height(16.dp)) // A larger vertical gap.

            // This `if` block conditionally displays the error message only when `showError` is true.
            if (showError) {
                Text(
                    text = "Invalid username or password",
                    color = Color.Red // Sets the text color to red for emphasis.
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                // `scope.launch` starts an asynchronous operation without blocking the UI thread.
                scope.launch {
                    // Calls the login function in the ViewModel, which is a suspend function.
                    val user = viewModel.login(username, password)
                    if (user != null) {
                        // If the login is successful, determine the user's role.
                        // The UserRole enum helps differentiate between user types for easier navigation.
                        if(user.patient == true){
                            onLoginClick(UserRole.PATIENT) // Navigate to the patient flow.
                        }else{
                            onLoginClick(UserRole.CAREGIVER) // Navigate to the caregiver flow.
                        }
                    } else {
                        // If login fails, set the state to show the error message.
                        showError = true
                    }
                }
            }) {
                Text("Login")
            }

            // This block displays a developer-only shortcut button if the app is in debug mode
            // and the `onDevCaregiverClick` lambda was provided.
            if (isDebuggable && onDevCaregiverClick != null) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(onClick = { onDevCaregiverClick() }) {
                    Text("Dev → Caregiver")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = {
                // TODO: Implement navigation to a "Sign Up" or "Create Account" screen.
            }) {
                Text("I don't have an account")
            }
        }
    }
}
