package com.group7.medicationadherenceapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// EXAM CONCEPT: DATASTORE INITIALIZATION
// The `preferencesDataStore` delegate creates a singleton instance of DataStore<Preferences>.
// This is done as a top-level extension on Context, ensuring there is only one instance
// of the DataStore named "user_settings" for the entire application.
// DataStore is the modern, coroutine-based replacement for SharedPreferences.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

/**
 * EXAM CONCEPT: REPOSITORY PATTERN
 * This class abstracts the data source (DataStore) from the rest of the app (like the ViewModel).
 * This separation of concerns makes the code more modular and easier to test.
 */
class UserPreferencesRepository(private val context: Context) {

    // EXAM CONCEPT: DATASTORE KEYS
    // To ensure type safety, DataStore uses key objects. You define a key with a specific
    // type (e.g., booleanPreferencesKey) and a unique name. This key is used to read and write data.
    private object PreferencesKeys {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    }

    // EXAM CONCEPT: EXPOSING DATA WITH FLOW
    // DataStore reads data asynchronously using Kotlin Flows. `dataStore.data` returns a Flow<Preferences>.
    // We use the .map() operator to transform the Flow of full Preferences into a Flow of just the
    // boolean value we care about. This creates a reactive data stream.
    // If the key doesn't exist (e.g., on first launch), we provide a default value of `false`.
    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.IS_DARK_MODE] ?: false
        }

    // EXAM CONCEPT: MODIFYING DATA WITH A SUSPEND FUNCTION
    // All writes to DataStore are asynchronous. The `edit` method is a suspend function,
    // so our `setDarkMode` function must also be a `suspend` function.
    // This ensures that I/O operations do not block the main thread, preventing UI freezes.
    suspend fun setDarkMode(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_MODE] = isDarkMode
        }
    }
}
