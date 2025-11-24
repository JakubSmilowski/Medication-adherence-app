// This line declares the package name, organizing all related files into a logical group.
package com.group7.medicationadherenceapp

// Imports from the Android framework and the Room library.
import android.content.Context // Provides access to the Android application's resources and services.
import androidx.room.Room       // The main builder class from the Room library used to create the database instance.
import com.group7.medicationadherenceapp.data.local.database.AppDatabase // Imports your own AppDatabase class, which defines the database schema and entities.

/**
 * A singleton object responsible for creating and providing a single instance of the AppDatabase.
 * Using an `object` in Kotlin automatically creates a thread-safe singleton, meaning there will
 * only ever be one `DatabaseProvider` instance in the entire app. This is crucial for managing
 * a database connection efficiently.
 */
object DatabaseProvider {
    /**
     * The `@Volatile` annotation ensures that the `INSTANCE` variable is always up-to-date across
     * all execution threads. When one thread modifies `INSTANCE`, the change is immediately visible
     * to all other threads. This prevents issues where one thread might see a stale (null) value
     * while another has already initialized it.
     */
    @Volatile
    private var INSTANCE: AppDatabase? = null // This variable will hold the single instance of our database. It's nullable because it starts as null.

    /**
     * This function is the public entry point to get the database instance. It's designed to be
     * thread-safe and to create the database only once.
     *
     * @param context The Android Context, which is required by Room to create the database file.
     *                It's best to use the application context to avoid memory leaks.
     * @return The singleton instance of the AppDatabase.
     */
    fun getDatabase(context: Context): AppDatabase {
        // This is a common pattern in Kotlin called the "Elvis operator" (`?:`).
        // It checks if `INSTANCE` is not null. If it's not null, it returns it immediately.
        // If `INSTANCE` IS null, it executes the block of code that follows.
        return INSTANCE ?: synchronized(this) {
            // The `synchronized` block acts as a lock. Only one thread can execute this block at a time.
            // This prevents a "race condition" where two threads might try to create the database
            // simultaneously, resulting in two different database instances.

            // Inside the synchronized block, we check again if INSTANCE is still null.
            // This "double-check" is necessary because another thread might have initialized the database
            // in the time between the first check and acquiring the synchronized lock.
            val instance = INSTANCE ?: Room.databaseBuilder(
                context.applicationContext, // Use the application context to prevent memory leaks associated with Activities or other short-lived contexts.
                AppDatabase::class.java,    // Your Room database class that defines all the tables (entities) and DAOs.
                "medication_database"       // The filename for the SQLite database on the device.
            )
                // This line defines a migration strategy. `fallbackToDestructiveMigration()` tells Room
                // that if the database schema changes (e.g., you add a new column to a table) and you haven't
                // provided a specific migration path, it should just delete the entire old database and
                // create a new one. This is easy for development but means all user data will be lost on an app update.
                // As the comment correctly notes, this should be replaced with a proper migration strategy
                // for a production application.
                .fallbackToDestructiveMigration()
                .build() // This finalizes the creation of the database and returns the instance.

            // Assign the newly created instance to our static variable so it can be reused on subsequent calls.
            INSTANCE = instance

            // Return the new instance.
            instance
        }
    }
}
