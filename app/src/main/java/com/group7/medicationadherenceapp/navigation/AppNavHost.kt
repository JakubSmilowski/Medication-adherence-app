package com.group7.medicationadherenceapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.group7.medicationadherenceapp.patient.PatientHomeScreen
import com.group7.medicationadherenceapp.ProfileScreen
import com.group7.medicationadherenceapp.SettingsScreen
import com.group7.medicationadherenceapp.caregiver.CaregiverHomeScreen
import com.group7.medicationadherenceapp.history.HistoryScreen


fun NavGraphBuilder.caregiverGraph(nav: NavController) {
    composable("${Dest.CAREGIVER}/{userId}", arguments = listOf(navArgument("userId") { type = NavType.IntType })) { backStackEntry ->
        val userId = backStackEntry.arguments?.getInt("userId")
        requireNotNull(userId) { "userId parameter wasn't found. Please make sure it's set!" }
        CaregiverHomeScreen(nav)
    }
    composable("${Dest.HISTORY}/{userId}", arguments = listOf(navArgument("userId") { type = NavType.IntType })) { backStackEntry ->
        val userId = backStackEntry.arguments?.getInt("userId")
        requireNotNull(userId) { "userId parameter wasn't found. Please make sure it's set!" }
        HistoryScreen(nav)
    }
    composable("${Dest.PROFILE}/{userId}", arguments = listOf(navArgument("userId") { type = NavType.IntType })) { backStackEntry ->
        val userId = backStackEntry.arguments?.getInt("userId")
        requireNotNull(userId) { "userId parameter wasn't found. Please make sure it's set!" }
        ProfileScreen (nav = nav, userId = userId) 
    }
    composable("${Dest.SETTINGS}/{userId}", arguments = listOf(navArgument("userId") { type = NavType.IntType })) { backStackEntry ->
        val userId = backStackEntry.arguments?.getInt("userId")
        requireNotNull(userId) { "userId parameter wasn't found. Please make sure it's set!" }
        SettingsScreen(nav)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.patientGraph(nav: NavController) {
    // Define the route to accept a userId argument
    val patientHomeRoute = "${Dest.HOME}/{userId}"

    composable(
        route = patientHomeRoute,
        arguments = listOf(navArgument("userId") { type = NavType.IntType })
    ) { backStackEntry ->
        // extract userId
        val userId = backStackEntry.arguments?.getInt("userId")
        requireNotNull(userId) { "userId parameter wasn't found. Please make sure it's set!" }
        PatientHomeScreen(navController = nav, userId = userId)
    }

    // You will likely need to update other screens if they also require the userId

    val profileRoute = "${Dest.PROFILE}/{userId}"

    composable(
        route = profileRoute,
        arguments = listOf(navArgument("userId") { type = NavType.IntType })
    ) { backStackEntry ->
        val userId = backStackEntry.arguments?.getInt("userId")
        requireNotNull(userId) { "userId parameter wasn't found for ProfileScreen." }
        ProfileScreen(nav = nav, userId = userId)
    }

    // You will likely need to update other screens if they also require the userId
    composable("${Dest.HISTORY}/{userId}", arguments = listOf(navArgument("userId") { type = NavType.IntType })) { backStackEntry ->
        val userId = backStackEntry.arguments?.getInt("userId")
        requireNotNull(userId) { "userId parameter wasn't found. Please make sure it's set!" }
        // If HistoryScreen needs userId, you must update its route and argument handling as well
        HistoryScreen(nav)
    }
    composable("${Dest.SETTINGS}/{userId}", arguments = listOf(navArgument("userId") { type = NavType.IntType })) { backStackEntry ->
        val userId = backStackEntry.arguments?.getInt("userId")
        requireNotNull(userId) { "userId parameter wasn't found. Please make sure it's set!" }
        SettingsScreen(nav)
    }
}