package com.group7.medicationadherenceapp.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.group7.medicationadherenceapp.HomeScreen
import com.group7.medicationadherenceapp.ProfileScreen
import com.group7.medicationadherenceapp.SettingsScreen
import com.group7.medicationadherenceapp.caregiver.CaregiverHomeScreen
import com.group7.medicationadherenceapp.history.HistoryScreen

/**
 * Plug-in graph for the Caregiver feature.
 * Calls this from MainActivity's NavHost builder.
 */
fun NavGraphBuilder.caregiverGraph(nav: NavController) {
    composable(Dest.CAREGIVER) { CaregiverHomeScreen(nav) }
    // Simple placeholders so bottom bar can navigate:
    composable(Dest.HISTORY) { HistoryScreen(nav) }
    composable(Dest.PROFILE) { ProfileScreen (nav) }
    composable(Dest.SETTINGS) { SettingsScreen(nav) }
}

fun NavGraphBuilder.patientGraph(nav: NavController) {
    // Główny ekran pacjenta
    composable(Dest.HOME) { HomeScreen(nav) }

    // Wspólne ekrany dla pacjenta (możesz je później rozdzielić, jeśli będzie potrzeba)
    composable(Dest.HISTORY) { HistoryScreen(nav) }
    composable(Dest.PROFILE) { ProfileScreen(nav) }
    composable(Dest.SETTINGS) { SettingsScreen(nav) }
}