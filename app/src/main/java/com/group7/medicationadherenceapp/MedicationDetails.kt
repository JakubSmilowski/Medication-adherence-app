package com.group7.medicationadherenceapp
//CONSOLIDATED UI & LAYOUT
import androidx.compose.foundation.layout.*    import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.group7.medicationadherenceapp.ui.theme.MedicationAdherenceAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationDetailScreen(medicationName: String, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar( // This is the experimental API
                title = { Text(medicationName) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Details about $medicationName", fontSize = 22.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MedicationDetailPreview() {
    MedicationAdherenceAppTheme {
        MedicationDetailScreen(
            medicationName = "Medication 1",
            onBackClick = {}
        )
    }
}
    