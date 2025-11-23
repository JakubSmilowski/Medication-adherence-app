package com.group7.medicationadherenceapp

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.group7.medicationadherenceapp.data.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * EXAM CONCEPT: ANDROIDVIEWMODEL vs. VIEWMODEL
 * We use ViewModel to survive configuration changes (like screen rotation) and to separate UI-related data
 * from the UI logic. By extending from ViewModel, we ensure this class has the correct lifecycle.
 * This ViewModel takes a repository as a constructor parameter, which is a best practice for dependency injection.
 */
class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    // EXAM CONCEPT: STATEFLOW FOR UI STATE
    // The `isDarkMode` Flow from the repository is a "cold" Flow. We convert it into a "hot" StateFlow here.
    // A StateFlow always has a value, holds the last-emitted value, and allows multiple collectors.
    // This is ideal for representing UI state that can be observed by Composables.
    // - `viewModelScope`: The coroutine scope tied to the ViewModel's lifecycle. All coroutines here are automatically cancelled when the ViewModel is cleared.
    // - `stateIn`: The operator that converts the cold Flow into a hot StateFlow.
    // - `SharingStarted.WhileSubscribed(5_000)`: This keeps the upstream Flow active for 5 seconds after the last collector (UI) unsubscribes. This is an optimization to save resources.
    val isDarkMode: StateFlow<Boolean> = userPreferencesRepository.isDarkMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false // The initial value to use before the DataStore has emitted its first value.
        )

    // EXAM CONCEPT: UPDATING STATE BY CALLING A VIEWMODEL FUNCTION
    // The UI should not modify the repository directly. Instead, it calls this function.
    // This function then launches a coroutine in `viewModelScope` to safely call the `suspend` function
    // in the repository, following the principles of Unidirectional Data Flow (UDF).
    fun setDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setDarkMode(isDarkMode)
        }
    }
}

/**
 * EXAM CONCEPT: VIEWMODELPROVIDER.FACTORY FOR DEPENDENCY INJECTION
 * You cannot pass arguments (like a repository) to a ViewModel's constructor directly when using the default `viewModel()` delegate.
 * A Factory is the standard mechanism for this. It's a class that knows HOW to create your ViewModel instance.
 * This allows us to manually provide the `UserPreferencesRepository` to the `SettingsViewModel`.
 */
class SettingsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // We manually create the repository and pass it to the ViewModel's constructor.
            return SettingsViewModel(UserPreferencesRepository(application)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
