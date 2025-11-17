//holds ui state, of List<HistoryIntake> (mock data for now)
//view - state, viewmodel -  filtering, loading, data mapping

package com.group7.medicationadherenceapp.history

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HistoryViewModel : ViewModel() {
    private val _items = MutableStateFlow(
        listOf(
            HistoryIntake("1", "Alex A.", "Metformin 500mg", IntakeStatus.Taken, "Today, 08.10"),
            HistoryIntake("2", "Ivan B.", "Lisinopril 10mg", IntakeStatus.Missed, "Yesterday 21.00"),
            HistoryIntake("3", "Alina F.", "Vitamin D 1000IU", IntakeStatus.Skipped, "Today 09.00")
        )
    )
    val items: StateFlow<List<HistoryIntake>> = _items
}

//later filters such as today/week/month, role: patientId, caregiverId can be added