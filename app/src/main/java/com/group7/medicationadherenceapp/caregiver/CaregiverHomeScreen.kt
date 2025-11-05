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

// frame colors
private val FrameColor = Color(0xFFE0E0E0)
private val BorderColor = Color(0xFFBDBDBD)

@Composable
fun CaregiverHomeScreen(nav: NavController) {
    val context = LocalContext.current

    fun contactDoctor() {
        // Opens the dialer with number prefilled (no CALL_PHONE permission needed)
        context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:123456789")))
    }

    // temporary data (keep as-is for now)
    val meds = remember {
        mutableStateListOf(
            MedItem(1, "Medication 1", "08:00"),
            MedItem(2, "Medication 2", "13:00"),
            MedItem(3, "Medication 3", "20:00"),
        )
    }

    val dateText = remember {
        SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(Date())
    }

    Scaffold(
        bottomBar = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Spacer(Modifier.height(4.dp))
                Frame(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp)
                        .clickable { contactDoctor() }     // ← tap to open dialer
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Contact doctor",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                BottomBarRect(
                    onHome = { nav.navigate(Dest.HOME) { launchSingleTop = true } },
                    onHistory = { nav.navigate(Dest.HISTORY) { launchSingleTop = true } },
                    onProfile = { nav.navigate(Dest.PROFILE) { launchSingleTop = true } },
                    onSettings = { nav.navigate(Dest.SETTINGS) { launchSingleTop = true } },
                )
            }
        }
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            //header: Hello & Date
            item {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Frame {
                        Text(
                            "Hello, Name",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Frame {
                        Text(
                            dateText,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            // title-medication's name
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

            // medication rows
            items(meds, key = { it.id }) { med ->
                MedicationRowRect(
                    med = med,
                    onToggleTaken = {
                        val i = meds.indexOfFirst { it.id == med.id }
                        if (i >= 0) meds[i] = meds[i].copy(taken = !med.taken)
                    }
                )
            }

            // Daily progress
            item {
                val total = meds.size
                val done = meds.count { it.taken }
                val pct = if (total == 0) 0 else (100 * done / total)
                Frame(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Daily Progress: $pct%",
                        fontSize = 22.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

//* medication row (Name | Time | Pill or Pill Off) */
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
        // Medication name
        Frame(modifier = Modifier.weight(1.9f)) {
            Text(
                med.name,
                fontSize = 22.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }

        // Time
        Frame(modifier = Modifier.weight(1.0f)) {
            Text(
                med.time,
                fontSize = 22.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }

        // Pill / Pill Off icons with tint
        Frame(modifier = Modifier.size(56.dp)) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onToggleTaken) {
                    if (med.taken) {
                        Image(
                            painter = painterResource(R.drawable.pill),
                            contentDescription = "Taken",
                            modifier = Modifier.size(28.dp),
                            colorFilter = ColorFilter.tint(Color(0xFF1B5E20)) // green when is taken
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.pill_off),
                            contentDescription = "Not taken",
                            modifier = Modifier.size(28.dp),
                            colorFilter = ColorFilter.tint(Color(0xFFD32F2F)) // red when is not taken
                        )
                    }
                }
            }
        }
    }
}

/** Reusable rectangular frame */
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

/** Bottom bar */
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
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BottomSquare(Icons.Filled.Home, "Home", onHome)
        BottomSquare(Icons.Filled.History, "History", onHistory)
        BottomSquare(Icons.Filled.Person, "Profile", onProfile)
        BottomSquare(Icons.Filled.Settings, "Settings", onSettings)
    }
}

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
            .clickable { onClick() },  // ← navigate
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = label)
    }
}
