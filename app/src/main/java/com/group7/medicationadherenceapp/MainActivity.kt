package com.group7.medicationadherenceapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.group7.medicationadherenceapp.navigation.caregiverGraph
import com.group7.medicationadherenceapp.navigation.patientGraph
import com.group7.medicationadherenceapp.ui.theme.MedicationAdherenceAppTheme
import com.group7.medicationadherenceapp.ui.theme.intro.IntroScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedicationAdherenceAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "intro") {
                        //Intro route
                        composable("intro") {
                            IntroScreen(
                                onLoginClick = {
                                    navController.navigate("login")
                                },
                                onRegisterClick = {
                                    navController.navigate("register")
                                }
                            )
                        }
                        //login route
                        composable("login") {
                            LoginScreen(
                                onLoginClick = { role, userId ->
                                    val routeWithId = "${role.startDestination}/$userId"
                                    navController.navigate(routeWithId) {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onBackClick = { navController.popBackStack() },
                                onDevCaregiverClick = {
                                    navController.navigate("caregiver/1") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }
                        //register route
                        composable("register") {
                            RegistrationScreen(
                                onRegistrationComplete = { role ->
                                    navController.navigate(role.startDestination) {
                                        popUpTo("register") { inclusive = true }
                                    }
                                },
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                        //Patient graph
                        patientGraph(navController)
                        //Caregiver graph
                        caregiverGraph(navController)
                        //medication details route
                        composable("medicationDetails/{medName}") { backStackEntry ->
                            val medName = backStackEntry.arguments?.getString("medName") ?: ""
                            MedicationDetailScreen(
                                medicationName = medName,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}