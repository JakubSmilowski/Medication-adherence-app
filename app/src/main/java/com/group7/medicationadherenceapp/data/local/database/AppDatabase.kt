package com.group7.medicationadherenceapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.group7.medicationadherenceapp.data.local.database.User
import com.group7.medicationadherenceapp.data.local.database.UserDao

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}