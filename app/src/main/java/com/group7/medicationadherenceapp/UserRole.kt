package com.group7.medicationadherenceapp
//import com.group7.medicationadherenceapp.navigation.Dest
//Created so it is error-proof

enum class UserRole(val startDestination: String) {
    PATIENT(startDestination = "home"),
    CAREGIVER(startDestination = "caregiver")
}
