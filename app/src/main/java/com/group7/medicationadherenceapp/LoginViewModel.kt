package com.group7.medicationadherenceapp


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.group7.medicationadherenceapp.data.local.database.AppDatabase
import com.group7.medicationadherenceapp.data.local.database.User

/**even i dunno what the following four lines do.
 *  i mean, they are supposed to load the database and to login the user
 *  but i have no clue how they actually do it.
 *  apprently thats the way so i dont care
    */

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getDatabase(application).userDao()

    suspend fun login(username: String, password: String): User? {
        return userDao.login(username, password)
    }
    //This I know, this registers the mf
    suspend fun register(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        isPatient: Boolean
    ): Boolean {
        if (userDao.getUserByUsername(username) != null || userDao.getUserByEmail(email) != null) {
            return false // The mf already exists
        }

        val user = User(
            uid = 0, // auto-generates
            username = username,
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            patient = isPatient,
            phoneNumber = null // Not collecting this in registration for now cause i dunno
        )
        userDao.insert(user)
        return true // Mf was successful
    }
}