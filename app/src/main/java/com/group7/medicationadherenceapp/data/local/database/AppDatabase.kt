package com.group7.medicationadherenceapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.group7.medicationadherenceapp.data.local.database.User
import com.group7.medicationadherenceapp.data.local.database.UserDao

@Database(entities = [User::class, Medication::class], version = 7)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun medicationDao(): MedicationDao

    abstract fun userWithMedicationDao() : UserWithMedicationDao

}