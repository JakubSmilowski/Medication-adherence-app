package com.group7.medicationadherenceapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val database = DatabaseProvider.getDatabase(application)
    private val userDao = database.userDao()

    private val _allUsers = MutableStateFlow<List<User>>(emptyList())
    val allUsers: StateFlow<List<User>> = _allUsers

    private val _testUser = MutableStateFlow<User?>(null)
    val testUser: StateFlow<User?> = _testUser

    fun insertTestUser() {
        viewModelScope.launch {
            val testUser = User(
                uid = 1,
                firstName = "John",
                lastName = "Doe",
                username = "user1",
                password = "password1",
                email = "john.doe@example.com",
                phoneNumber = "1234567890"
            )
            userDao.insertAll(testUser)
            loadAllUsers()
        }
    }

    fun loadAllUsers() {
        viewModelScope.launch {
            _allUsers.value = userDao.getAll()
            if (_allUsers.value.isNotEmpty()) {
                _testUser.value = _allUsers.value.first()
            }
        }
    }

    fun login(username: String, password: String, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            val user = userDao.login(username, password)
            if (user != null) {
                onSuccess()
            } else {
                onError()
            }
        }
    }
}