package com.group7.medicationadherenceapp.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MedicationDao {
    @Query("SELECT * FROM medication")
    suspend fun getAll(): List<Medication>

    @Query("SELECT * FROM medication WHERE mid IN (:medicationIds)")
    suspend fun loadAllByIds(medicationIds: IntArray): List<Medication>

    @Query("SELECT * FROM medication WHERE name LIKE :name")
    suspend fun findByName(name: String): Medication?

    @Insert
    suspend fun insertAll(vararg medication: Medication)

    @Delete
    suspend fun delete(medication: Medication)
}