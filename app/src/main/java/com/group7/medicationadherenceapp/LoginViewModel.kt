// This line declares the package name, organizing the code into a logical group.
// All files related to the main application features are in this package.
package com.group7.medicationadherenceapp

// Imports from the Android framework and other parts of your app.
import android.app.Application // Provides the application context, needed for initializing the database.
import androidx.lifecycle.AndroidViewModel // A special type of ViewModel that includes an application reference.
import com.group7.medicationadherenceapp.data.local.database.AppDatabase // Imports your AppDatabase class, which defines the database.
import com.group7.medicationadherenceapp.data.local.database.User // Imports the User data class, which represents the user table in the database.

/**
 * The original developer comment, expressing some confusion. Let's clarify it below.
 * "even i dunno what the following four lines do.
 *  i mean, they are supposed to load the database and to login the user
 *  but i have no clue how they actually do it.
 *  apprently thats the way so i dont care"
 */

// This class is responsible for handling all the logic related to logging in and registering a user.
// It acts as a bridge between the UI (the LoginScreen) and the data source (the database).
// It extends `AndroidViewModel` instead of the regular `ViewModel` because it needs the application `Context`
// to initialize the database connection.
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    // --- Explanation for the "magic" lines ---
    // This line initializes the connection to the database's User Data Access Object (DAO).
    // 1. `AppDatabase.getDatabase(application)`: This calls your singleton provider to get the single,
    //    shared instance of the database for the entire app. It's crucial this only happens once.
    // 2. `.userDao()`: Once you have the database instance, this is an abstract function defined in your
    //    `AppDatabase` class that Room implements for you. It returns the `UserDao` interface, which
    //    contains all the methods for interacting with the 'user' table (e.g., login, insert, etc.).
    // By storing `userDao` in a private property, the ViewModel can now easily access all database
    // operations related to users.
    private val userDao = AppDatabase.getDatabase(application).userDao()

    /**
     * Attempts to log in a user with the given credentials.
     * `suspend` marks this as a coroutine function. This is critical because database operations
     * can be slow and must NOT be run on the main UI thread, as that would freeze the app.
     * The UI (LoginScreen) will call this function from within a `CoroutineScope`.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @return The `User` object if the login is successful, or `null` if the credentials are incorrect.
     */
    suspend fun login(username: String, password: String): User? {
        // This calls the `login` function defined in your `UserDao` interface. Room automatically
        // generates the SQL query to find a user where the username and password match.
        return userDao.login(username, password)
    }

    /**
     * Registers a new user in the database.
     * It first checks if the username or email already exists to prevent duplicates.
     * This is also a `suspend` function because it performs database write operations.
     *
     * @param username The desired username.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param email The user's email address.
     * @param password The user's chosen password.
     * @param isPatient A boolean flag to determine if the user is a patient or a caregiver.
     * @return `true` if the registration was successful, `false` if the username or email already exists.
     */
    suspend fun register(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        isPatient: Boolean
    ): Boolean {
        // Database query: Check if a user with this username or email already exists.
        // The `||` means "or", so if either of these calls returns a user, the condition is true.
        if (userDao.getUserByUsername(username) != null || userDao.getUserByEmail(email) != null) {
            // If a user is found, we can't create a duplicate, so we return `false` immediately.
            return false // "The mf already exists"
        }

        // If the checks pass, create a new `User` object with the provided details.
        val user = User(
            uid = 0, // Set to 0 because Room will auto-generate the unique ID for this primary key.
            username = username,
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password, // Note: In a real app, passwords should be hashed, not stored in plain text.
            patient = isPatient,
            phoneNumber = null // Set to null as it's not being collected during registration.
        )
        // This calls the `insert` function in the DAO to save the new user object as a new row in the database.
        userDao.insert(user)
        // Return `true` to indicate that the registration was successful.
        return true // "Mf was successful"
    }
}
