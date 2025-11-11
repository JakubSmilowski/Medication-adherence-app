package com.group7.medicationadherenceapp.data.local.database

import androidx.room.Embedded
import androidx.room.Relation


data class UserWithMedication(
    @Embedded val user: User,
    @Relation(
        parentColumn = "uid",
        entityColumn = "userId"
    )
    val medication: List<Medication>
)