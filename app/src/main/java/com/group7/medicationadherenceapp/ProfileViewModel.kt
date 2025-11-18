package com.group7.medicationadherenceapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group7.medicationadherenceapp.data.local.database.AppDatabase
import com.group7.medicationadherenceapp.data.local.database.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val db: AppDatabase) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun loadUser(userId: Int) {
        viewModelScope.launch {
            _user.value = db.userDao().getUserById(userId)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            db.userDao().updateUser(user)
            _user.value = user
        }
    }
}