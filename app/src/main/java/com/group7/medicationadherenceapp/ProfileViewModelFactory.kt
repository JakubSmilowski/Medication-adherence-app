package com.group7.medicationadherenceapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.group7.medicationadherenceapp.data.local.database.AppDatabase

class ProfileViewModelFactory(private val db: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}