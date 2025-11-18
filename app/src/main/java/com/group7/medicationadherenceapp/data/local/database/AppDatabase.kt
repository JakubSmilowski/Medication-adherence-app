package com.group7.medicationadherenceapp.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.group7.medicationadherenceapp.data.local.database.User
import com.group7.medicationadherenceapp.data.local.database.UserDao

@Database(entities = [User::class, Medication::class], version = 7)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun medicationDao(): MedicationDao

    abstract fun userWithMedicationDao() : UserWithMedicationDao

    companion object {
        /**
         *
         * This prevents race-conditions.... I am taking about the @Volatile stuff
         *
         */
        @Volatile
        private var INSTANCE: AppDatabase? = null
        /**
         *
         * Apparently this mf here is a thread-safe way to get single instances of the db.
         *
         * */

        //The following is AI comment. Even with it, I still dont understand what tf this mf does. Thank god for those StackOverload gods.

        /**
         * This function provides a thread-safe way to get the singleton instance
         * of the AppDatabase.
         *
         * @param context The application context. It's used by the database builder.
         * @return The singleton AppDatabase instance.
         */
        fun getDatabase(context: Context): AppDatabase {
            //If an instance already exists return it to avoid creating a new one. Otherwise enter a synchronized block to create a new instance.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "medication_adherence_database" // The db name
                )
                    .fallbackToDestructiveMigration() //Added for easier development
                    .allowMainThreadQueries()
                    .build()

                //Assign the newly created instance and then returns it
                INSTANCE = instance
                instance
            }
        }
    }

}

/*
package com.group7.medicationadherenceapp.data.local.database

import android.content.Context // Import Context
import androidx.room.Database
import androidx.room.Room // Import Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, Medication::class], version = 7)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun medicationDao(): MedicationDao
    abstract fun userWithMedicationDao() : UserWithMedicationDao

    // --- START OF ADDED CODE ---

    companion object {
        /**
         * The @Volatile annotation ensures that writes to this field are immediately
         * made visible to other threads. This is important to prevent race conditions
         * when multiple threads might try to create a database instance simultaneously.
         */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * This function provides a thread-safe way to get the singleton instance
         * of the AppDatabase.
         *
         * @param context The application context. It's used by the database builder.
         * @return The singleton AppDatabase instance.
         */
        fun getDatabase(context: Context): AppDatabase {
            // If an instance already exists, return it to avoid creating a new one.
            // Otherwise, enter a synchronized block to create a new instance.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "medication_adherence_database" // The official name for your database file
                )
                // In a real app, you would add .fallbackToDestructiveMigration() here
                // during development if you don't want to provide migration paths.
                // For production, you should implement proper migrations.
                .fallbackToDestructiveMigration() // Added for easier development
                .build()

                // Assign the newly created instance and then return it.
                INSTANCE = instance
                instance
            }
        }
    }
    // --- END OF ADDED CODE ---
}

 */