// This line declares the package name. It helps organize code into logical groups
// and avoids naming conflicts. All files in this directory belong to this package.
package com.group7.medicationadherenceapp.caregiver

// AndroidX imports for creating a ViewModel, which survives configuration changes.
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Coroutine Flow imports for creating an observable state holder that the UI can listen to.
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
// Standard Java/Kotlin imports for date formatting and handling.
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
// Imports the MedItem data class from its model sub-package.
import com.group7.medicationadherenceapp.caregiver.model.MedItem

/**
 * A data class that represents the entire state of the Caregiver UI at any given moment.
 * It's immutable (all properties are `val`), which is a best practice for state management.
 * When something changes, we create a new copy of this state object with the updated values.
 */
data class CaregiverUiState(
    val dateText: String = "",                 // The formatted string for the current date (e.g., "Mon, Jan 1").
    val meds: List<MedItem> = emptyList(),     // The list of medications to be displayed.
    val progressPercent: Int = 0,              // The calculated percentage of medications taken.
    val isLoading: Boolean = false,            // A flag to indicate if data is being loaded (e.g., from a network).
    val error: String? = null                  // An optional error message to show in the UI if something goes wrong.
)

/**
 * A sealed class that defines all possible user actions (events) that can occur on the UI.
 * Using a sealed class ensures that the `when` statement handling these events is exhaustive,
 * meaning the compiler will warn us if we forget to handle a new event type.
 */
sealed class CaregiverEvent {
    // Event representing the user toggling the 'taken' status of a medication. It carries the ID of the med.
    data class ToggleTaken(val id: Long) : CaregiverEvent()
    // Event representing the user clicking the "Contact Doctor" button.
    object ContactDoctorClicked : CaregiverEvent()
    // Event to manually refresh the data on the screen.
    object Refresh : CaregiverEvent()
}

/**
 * The ViewModel is the business logic and state holder for the Caregiver screen.
 * It prepares and manages the UI state and reacts to user events.
 * It extends `ViewModel`, so it's lifecycle-aware and survives configuration changes like screen rotation.
 */
class CaregiverViewModel : ViewModel() {

    // A private date formatter instance. This is created once to be reused, which is efficient.
    private val dateFormatter = SimpleDateFormat("EEE, MMM d", Locale.getDefault())

    // `_uiState` is the private, mutable state holder. Only the ViewModel can modify it.
    // It's initialized with the default screen state, including a hardcoded list of medications for now.
    // The `.let(::withProgress)` part immediately calculates the initial progress percentage.
    private val _uiState = MutableStateFlow(
        CaregiverUiState(
            dateText = dateFormatter.format(Date()),
            meds = listOf(
                MedItem(1, "Medication 1", "08:00"),
                MedItem(2, "Medication 2", "13:00"),
                MedItem(3, "Medication 3", "20:00")
            )
        ).let(::withProgress) // This is a clever way to chain the progress calculation on the initial state.
    )
    // `uiState` is the public, read-only version of the state that the UI (Composable) will observe.
    // This prevents the UI from directly modifying the state, enforcing a unidirectional data flow.
    val uiState: StateFlow<CaregiverUiState> = _uiState

    /**
     * This is the single entry point for the UI to send events to the ViewModel.
     * It uses a `when` statement to delegate the event to the appropriate private function.
     * @param event The user action that occurred.
     */
    fun onEvent(event: CaregiverEvent) {
        when (event) {
            is CaregiverEvent.ToggleTaken -> toggle(event.id)
            // In this case, contacting the doctor is a UI-specific action (launching an Intent),
            // so the ViewModel doesn't need to do anything. The comment makes this clear.
            CaregiverEvent.ContactDoctorClicked -> { /* handled by UI (intent) */ }
            CaregiverEvent.Refresh -> refresh()
        }
    }

    /**
     * Toggles the 'taken' status of a medication.
     * `viewModelScope.launch` starts a coroutine that is automatically cancelled when the ViewModel is cleared.
     * @param id The ID of the medication to toggle.
     */
    private fun toggle(id: Long) = viewModelScope.launch {
        // `update` is a thread-safe way to modify the StateFlow's value.
        // It guarantees that the block runs atomically.
        _uiState.update { state ->
            // `map` creates a *new* list. For each medication (`m`)...
            val updated = state.meds.map { m ->
                // ...if the ID matches, create a copy of it with the `taken` value flipped.
                if (m.id == id) m.copy(taken = !m.taken) else m //...otherwise, keep the original.
            }
            // Recalculate the progress with the newly updated list and return the final new state.
            withProgress(state.copy(meds = updated))
        }
    }

    /**
     * Refreshes the data on the screen. In a real app, this might re-fetch data from a database or network.
     * Here, it just re-formats the current date.
     */
    private fun refresh() = viewModelScope.launch {
        _uiState.update { state ->
            // Creates a new state copy with a potentially new date string and recalculates progress.
            state.copy(dateText = dateFormatter.format(Date()))
                .let(::withProgress)
        }
    }

    /**
     * A private helper function that takes a UI state and returns a new state with the
     * `progressPercent` field correctly calculated and updated.
     * @param s The current CaregiverUiState.
     * @return A new CaregiverUiState with the `progressPercent` updated.
     */
    private fun withProgress(s: CaregiverUiState): CaregiverUiState {
        val total = s.meds.size
        val done = s.meds.count { it.taken }
        // Avoids division by zero if the list is empty.
        val pct = if (total == 0) 0 else (100 * done / total)
        // Returns a copy of the state with the new percentage.
        return s.copy(progressPercent = pct)
    }
}
