package com.group7.medicationadherenceapp.data.local.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface UserWithMedicationDao
{
    @Transaction
    @Query("SELECT * FROM User")
    suspend fun getUserWithMedications(): List<UserWithMedication>

}