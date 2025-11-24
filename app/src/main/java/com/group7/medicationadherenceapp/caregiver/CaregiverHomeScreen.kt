package com.group7.medicationadherenceapp.caregiver

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.group7.medicationadherenceapp.R
import com.group7.medicationadherenceapp.caregiver.model.MedItem
import com.group7.medicationadherenceapp.navigation.Dest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Defines constant colors for the UI frames to maintain a consistent look.
private val FrameColor = Color(0xFFE0E0E0) // A light gray for the background of frames.
private val BorderColor = Color(0xFFBDBDBD) // A slightly darker gray for the borders.

/**
 * This is the main screen for the Caregiver view. It displays a list of medications,
 * progress, and navigation controls.
 * @param nav The NavController used for navigating between screens.
 */
@Composable
fun CaregiverHomeScreen(nav: NavController) {
    // LocalContext provides access to the Android application context, needed for system services like starting an Intent.
    val context = LocalContext.current

    // This function creates an Intent to open the phone's dialer with a pre-filled number.
    // ACTION_DIAL is used because it doesn't require the dangerous CALL_PHONE permission.
    fun contactDoctor() {
        context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:123456789")))
    }

    // `remember` with `mutableStateListOf` creates a stateful list.
    // When this list is modified (items added, removed, or updated), Compose will automatically
    // recompose any UI elements that use this list.
    val meds = remember {
        mutableStateListOf(
            MedItem(1, "Medication 1", "08:00"),
            MedItem(2, "Medication 2", "13:00"),
            MedItem(3, "Medication 3", "20:00"),
        )
    }

    // `remember` ensures the date is formatted only once when the composable is first created,
    // avoiding recalculation on every recomposition.
    val dateText = remember {
        SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(Date())
    }

    // Scaffold provides a standard layout structure with slots for top bars, bottom bars, and the main content.
    Scaffold(
        // The bottomBar is defined here. It contains the "Contact doctor" button and the main navigation bar.
        bottomBar = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Spacer(Modifier.height(4.dp)) // Provides a small gap at the top.
                // A custom Frame composable that is made clickable.
                Frame(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp)
                        .clickable { contactDoctor() }     // When tapped, it opens the phone dialer.
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center // Centers the text inside the box.
                    ) {
                        Text(
                            text = "Contact doctor",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                // The main navigation bar with Home, History, Profile, and Settings.
                BottomBarRect(
                    onHome = { nav.navigate(Dest.CAREGIVER) { launchSingleTop = true } },
                    onHistory = { nav.navigate(Dest.HISTORY) { launchSingleTop = true } },
                    onProfile = { nav.navigate(Dest.PROFILE) { launchSingleTop = true } },
                    onSettings = { nav.navigate(Dest.SETTINGS) { launchSingleTop = true } },
                )
            }
        }
    ) { inner -> // `inner` contains the padding values needed to avoid content overlapping with the bars.
        // LazyColumn is used for displaying scrollable lists efficiently.
        // It only composes and lays out the items that are currently visible on screen.
        LazyColumn(
            modifier = Modifier
                .padding(inner) // Applies the padding from the Scaffold.
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp) // Adds space between each item in the column.
        ) {
            // The `item` block is used for a single, non-repeating element inside a LazyColumn.
            // This item displays the "Hello" message and the current date.
            item {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween // Pushes the two text boxes to opposite ends.
                ) {
                    Frame {
                        Text(
                            "Hello, Name",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp)) // Space between the two frames.
                    Frame {
                        Text(
                            dateText,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            // A title for the medication list section.
            item {
                Frame {
                    Text(
                        "Name’s medication",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // The `items` block is used to display a dynamic list of items.
            // It takes the `meds` list and creates a `MedicationRowRect` for each `med` item.
            // The `key` helps Compose optimize recomposition by uniquely identifying each item.
            items(meds, key = { it.id }) { med ->
                MedicationRowRect(
                    med = med,
                    onToggleTaken = {
                        // This logic finds the index of the medication that was toggled.
                        val i = meds.indexOfFirst { it.id == med.id }
                        // If found, it replaces the item with a new copy that has the `taken` status flipped.
                        // This modification triggers a recomposition to update the UI.
                        if (i >= 0) meds[i] = meds[i].copy(taken = !med.taken)
                    }
                )
            }

            // This item displays the patient's daily medication adherence progress.
            item {
                val total = meds.size
                val done = meds.count { it.taken }
                val pct = if (total == 0) 0 else (100 * done / total) // Calculates the percentage.
                Frame(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Daily Progress: $pct%",
                        fontSize = 22.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Adds extra space at the bottom of the list to ensure the last item isn't hidden by the bottom bar.
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

/**
 * A composable function that displays a single row for a medication,
 * including its name, scheduled time, and a toggle button to mark it as taken.
 * @param med The medication data item to display.
 * @param onToggleTaken A lambda function to be called when the user taps the pill icon.
 */
@Composable
private fun MedicationRowRect(
    med: MedItem,
    onToggleTaken: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Medication name frame. `weight` allows it to take up a proportional amount of space.
        Frame(modifier = Modifier.weight(1.9f)) {
            Text(
                med.name,
                fontSize = 22.sp,
                maxLines = 1, // Prevents text from wrapping to a new line.
                overflow = TextOverflow.Ellipsis, // Adds "..." if the text is too long.
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }

        // Time frame.
        Frame(modifier = Modifier.weight(1.0f)) {
            Text(
                med.time,
                fontSize = 22.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }

        // Frame for the pill icon button.
        Frame(modifier = Modifier.size(56.dp)) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onToggleTaken) {
                    // Conditionally display a different icon and color based on the `taken` state.
                    if (med.taken) {
                        Image(
                            painter = painterResource(R.drawable.pill),
                            contentDescription = "Taken",
                            modifier = Modifier.size(28.dp),
                            colorFilter = ColorFilter.tint(Color(0xFF1B5E20)) // Dark green tint when taken.
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.pill_off),
                            contentDescription = "Not taken",
                            modifier = Modifier.size(28.dp),
                            colorFilter = ColorFilter.tint(Color(0xFFD32F2F)) // Dark red tint when not taken.
                        )
                    }
                }
            }
        }
    }
}

/**
 * A reusable composable that creates a styled frame with a border and background.
 * It's a Row, so content passed to it will be arranged horizontally.
 * @param modifier Modifiers to be applied to the frame.
 * @param content The composable content to be placed inside the frame.
 */
@Composable
private fun Frame(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier
            .border(1.dp, BorderColor, RoundedCornerShape(6.dp))
            .background(FrameColor, RoundedCornerShape(6.dp)),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

/**
 * A composable for the bottom navigation bar, arranging four navigation items horizontally.
 * @param onHome Lambda to execute when the Home button is clicked.
 * @param onHistory Lambda to execute when the History button is clicked.
 * @param onProfile Lambda to execute when the Profile button is clicked.
 * @param onSettings Lambda to execute when the Settings button is clicked.
 */
@Composable
private fun BottomBarRect(
    onHome: () -> Unit,
    onHistory: () -> Unit,
    onProfile: () -> Unit,
    onSettings: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween // Distributes items evenly with space between them.
    ) {
        BottomSquare(Icons.Filled.Home, "Home", onHome)
        BottomSquare(Icons.Filled.History, "History", onHistory)
        BottomSquare(Icons.Filled.Person, "Profile", onProfile)
        BottomSquare(Icons.Filled.Settings, "Settings", onSettings)
    }
}

/**
 * A single square button for the bottom navigation bar.
 * @param icon The vector icon to display.
 * @param label A content description for accessibility.
 * @param onClick The navigation action to perform when clicked.
 */
@Composable
private fun BottomSquare(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Box(
        Modifier
            .size(72.dp)
            .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
            .background(FrameColor, RoundedCornerShape(10.dp))
            .clickable { onClick() },  // Makes the entire box clickable.
        contentAlignment = Alignment.Center // Centers the icon inside the box.
    ) {
        Icon(icon, contentDescription = label)
    }
}
