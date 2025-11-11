package com.group7.medicationadherenceapp.data.local.database

import android.R
import android.adservices.adid.AdId
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Medication(
    @PrimaryKey(autoGenerate = true) val mid: Int,
    @ColumnInfo(name = "userId") val userId: Long,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "dosage") val dosage: String?,
    @ColumnInfo(name = "frequency") val frequency: String?,
    @ColumnInfo(name = "start_date") val startDate: String?,
    @ColumnInfo(name = "end_date") val endDate: String?,
    @ColumnInfo(name = "status") val status: String?,
    @ColumnInfo(name = "created_at") val createdAt: String?,
)