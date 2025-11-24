// This line declares the package name, organizing the app's code.
// This file specifically handles the UI theme.
package com.group7.medicationadherenceapp.ui.theme

// Imports from the Android framework, used for interacting with the Android system.
import android.app.Activity
import android.os.Build

// Imports from Jetpack Compose for building the UI.
import androidx.compose.foundation.isSystemInDarkTheme // A function to check if the user's device is in dark mode.
import androidx.compose.material3.MaterialTheme          // The main theme composable that applies styling to its children.
import androidx.compose.material3.darkColorScheme        // A function to create a color scheme for dark theme.
import androidx.compose.material3.dynamicDarkColorScheme   // Creates a dark color scheme based on the user's wallpaper (Android 12+).
import androidx.compose.material3.dynamicLightColorScheme  // Creates a light color scheme based on the user's wallpaper (Android 12+).
import androidx.compose.material3.lightColorScheme       // A function to create a color scheme for light theme.
import androidx.compose.runtime.Composable                // The fundamental annotation for all Jetpack Compose UI functions.
import androidx.compose.ui.platform.LocalContext           // Provides the current Android `Context`.

// This defines the specific set of colors to be used when the app is in dark mode.
// These colors (e.g., Purple80) are defined in the Color.kt file.
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// This defines the specific set of colors to be used when the app is in light mode.
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    You can uncomment and customize these colors to further control the theme.
    For example, changing the default background or the color of text on top of surfaces.
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

/**
 * This is the main theme composable for the entire application.
 * It wraps all the UI content and applies a consistent color scheme and typography.
 * You wrap your entire app content with this function, usually in MainActivity.kt.
 *
 * @param darkTheme A boolean to manually force dark or light theme. By default, it follows the system setting.
 * @param dynamicColor A boolean to enable or disable "Dynamic Color" (Material You). By default, it's enabled.
 *                     Dynamic color automatically creates a theme from the user's wallpaper.
 * @param content The actual UI content of your app that this theme will be applied to.
 */
@Composable
fun MedicationAdherenceAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12 (API level 31, or "S") and higher.
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit // This is where your app's screens will be passed in.
) {
    // This `when` block determines which color scheme to use based on the parameters.
    val colorScheme = when {
        // Condition 1: If dynamic color is enabled AND the device is Android 12 or newer...
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current // Get the current Android context.
            // ...then use the dynamic color scheme, choosing dark or light based on the system theme.
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        // Condition 2: If dynamic color is not used, and the system is in dark theme...
        darkTheme -> DarkColorScheme // ...use our predefined DarkColorScheme.

        // Condition 3 (else): If none of the above, it must be a light theme...
        else -> LightColorScheme // ...so use our predefined LightColorScheme.
    }

    // MaterialTheme is the core composable that applies the theme.
    // It takes the chosen color scheme and typography and makes them available to all
    // child composables through `MaterialTheme.colorScheme` and `MaterialTheme.typography`.
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Typography is defined in the Typography.kt file.
        content = content        // Renders the app's UI content within this theme.
    )
}
