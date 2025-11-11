package com.group7.medicationadherenceapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.group7.medicationadherenceapp.data.local.database.User
import com.group7.medicationadherenceapp.data.local.database.Medication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val database = DatabaseProvider.getDatabase(application)
    private val userDao = database.userDao()
    private val medicationDao = database.medicationDao()

    private val _allUsers = MutableStateFlow<List<User>>(emptyList())
    private val _allMedication = MutableStateFlow<List<Medication>>(emptyList())
    val allUsers: StateFlow<List<User>> = _allUsers
    val allMedication: StateFlow<List<Medication>> = _allMedication

    private val _testUser = MutableStateFlow<User?>(null)
    private val _testMedication = MutableStateFlow<Medication?>(null)
    val testUser: StateFlow<User?> = _testUser
    val testMedication: StateFlow<Medication?> = _testMedication

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

    fun insertTestMedication(){
        viewModelScope.launch {
            val testMedication = Medication(
                mid = 1,
                name = "Apap",
                description = "painkiller",
                dosage = "10",
                frequency = "3 times a day",
                startDate = "10/11",
                endDate = "20/11",
                status = "Takken",
                createdAt = "10/11"
            )
            medicationDao.insertAll(testMedication)
            loadAllMedication();
        }
    }

    fun loadAllMedication() {
        viewModelScope.launch {
            _allMedication.value = medicationDao.getAll()
            if (_allMedication.value.isNotEmpty()) {
                _testMedication.value = _allMedication.value.first()
            }
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