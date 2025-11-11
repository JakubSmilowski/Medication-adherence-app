package com.group7.medicationadherenceapp.caregiver.model

data class MedItem(
    val id: Long,
    val name: String,
    val time: String,
    val taken: Boolean = false
)