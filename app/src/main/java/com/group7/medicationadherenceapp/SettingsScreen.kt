package com.group7.medicationadherenceapp
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.group7.medicationadherenceapp.ui.theme.MedicationAdherenceAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(nav: NavController) {
    Column {
        Text("To jest ekran Ustawień!")
        Text("To jest ekran Ustawień!")


    }


}

@Preview(showBackground = true)
@Composable
fun SettingsScreen() {
    MedicationAdherenceAppTheme {
        SettingsScreen(nav = rememberNavController())
    }
}
