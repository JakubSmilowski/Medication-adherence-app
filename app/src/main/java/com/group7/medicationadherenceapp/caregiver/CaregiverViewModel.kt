package com.group7.medicationadherenceapp.caregiver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.group7.medicationadherenceapp.caregiver.model.MedItem

data class CaregiverUiState(
    val dateText: String = "",
    val meds: List<MedItem> = emptyList(),
    val progressPercent: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class CaregiverEvent {
    data class ToggleTaken(val id: Long) : CaregiverEvent()
    object ContactDoctorClicked : CaregiverEvent()
    object Refresh : CaregiverEvent()
}

class CaregiverViewModel : ViewModel() {

    private val dateFormatter = SimpleDateFormat("EEE, MMM d", Locale.getDefault())

    private val _uiState = MutableStateFlow(
        CaregiverUiState(
            dateText = dateFormatter.format(Date()),
            meds = listOf(
                MedItem(1, "Medication 1", "08:00"),
                MedItem(2, "Medication 2", "13:00"),
                MedItem(3, "Medication 3", "20:00")
            )
        ).let(::withProgress)
    )
    val uiState: StateFlow<CaregiverUiState> = _uiState

    fun onEvent(event: CaregiverEvent) {
        when (event) {
            is CaregiverEvent.ToggleTaken -> toggle(event.id)
            CaregiverEvent.ContactDoctorClicked -> { /* handled by UI (intent) */ }
            CaregiverEvent.Refresh -> refresh()
        }
    }

    private fun toggle(id: Long) = viewModelScope.launch {
        _uiState.update { state ->
            val updated = state.meds.map { m ->
                if (m.id == id) m.copy(taken = !m.taken) else m
            }
            withProgress(state.copy(meds = updated))
        }
    }

    private fun refresh() = viewModelScope.launch {
        // You could change date or shuffle meds here if you want demo behavior
        _uiState.update { state ->
            state.copy(dateText = dateFormatter.format(Date()))
                .let(::withProgress)
        }
    }

    private fun withProgress(s: CaregiverUiState): CaregiverUiState {
        val total = s.meds.size
        val done = s.meds.count { it.taken }
        val pct = if (total == 0) 0 else (100 * done / total)
        return s.copy(progressPercent = pct)
    }
}