//small ui data class for one row in the history list,
//presentation model, keep UI independent from database/network fromats
package com.group7.medicationadherenceapp.history

enum class IntakeStatus { Taken, Missed, Skipped }

/** one row in history screen**/
data class HistoryIntake(
    val id: String,
    val patientName: String,
    val medicationName: String,
    val status: IntakeStatus,
    val whenText: String //example: today, 09.30
)
