package com.group7.medicationadherenceapp.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.group7.medicationadherenceapp.caregiver.CaregiverHomeScreen

/**
 * Plug-in graph for the Caregiver feature.
 * Call this from MainActivity's NavHost builder.
 */
fun NavGraphBuilder.caregiverGraph(nav: NavController) {
    composable(Dest.CAREGIVER) { CaregiverHomeScreen(nav) }
    // Simple placeholders so bottom bar can navigate:
    composable(Dest.HISTORY) { Text("History (TODO)") }
    composable(Dest.PROFILE) { Text("Profile (TODO)") }
    composable(Dest.SETTINGS) { Text("Settings (TODO)") }
}