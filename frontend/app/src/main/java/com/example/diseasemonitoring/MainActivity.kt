package com.example.diseasemonitoring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.diseasemonitoring.navigation.AppNavigation
import com.example.diseasemonitoring.ui.theme.DiseaseMonitoringTheme
import com.example.diseasemonitoring.viewmodels.DiseaseViewModels

class MainActivity : ComponentActivity() {
    // Utilise `viewModels` pour créer une instance unique du ViewModel liée à cette activité
    private val diseaseViewModel: DiseaseViewModels by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiseaseMonitoringTheme {
                // Conteneur de surface utilisant la couleur d'arrière-plan du thème
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Passe le ViewModel à la navigation
                    AppNavigation(diseaseViewModel)
                }
            }
        }
    }
}
