//view/ui file
//a list of history intake cards

package com.group7.medicationadherenceapp.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(nav: NavController) {
    // WHY: Remember a VM instance for this screen (simple approach without Hilt).
    val vm = remember { HistoryViewModel() }
    val items by vm.items.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("History") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            FilterRow()
            Divider()
            HistoryBody(items = items, isLoading = false, error = null)
        }
    }
}

@Composable
private fun FilterRow() {
    // WHY: Keep current filter selection in memory (UI state only, for now).
    var selected by remember { mutableStateOf(0) }
    val labels = listOf("Today", "Week", "Month")
    Row(
        modifier = Modifier.padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        labels.forEachIndexed { i, label ->
            FilterChip(
                selected = selected == i,
                onClick = { selected = i }, // later: update VM filter
                label = { Text(label) }
            )
        }
    }
}

@Composable
private fun HistoryBody(items: List<HistoryIntake>, isLoading: Boolean, error: String?) {
    when {
        isLoading -> CircularProgressIndicator(Modifier.padding(24.dp))
        error != null -> Text("Could not load history.\n$error", color = MaterialTheme.colorScheme.error)
        items.isEmpty() -> Text("No history yet", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(24.dp))
        else -> HistoryList(items)
    }
}

@Composable
private fun HistoryList(items: List<HistoryIntake>) {
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            ElevatedCard {
                Column(Modifier.padding(12.dp)) {
                    Text(
                        text = item.medicationName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = item.patientName,
                        style = MaterialTheme.typography.bodySmall
                    )
                    val statusText = when (item.status) {
                        IntakeStatus.Taken   -> "Taken • ${item.whenText}"
                        IntakeStatus.Missed  -> "Missed • ${item.whenText}"
                        IntakeStatus.Skipped -> "Skipped • ${item.whenText}"
                    }
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}