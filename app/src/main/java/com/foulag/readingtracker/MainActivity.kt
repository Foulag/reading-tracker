package com.foulag.readingtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.foulag.readingtracker.ui.theme.ReadingTrackerTheme

private data class AppDestination(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReadingTrackerTheme {
                val destinations = listOf(
                    AppDestination("Accueil", Icons.Default.Home),
                    AppDestination("Bibliothèque", Icons.Default.Book),
                    AppDestination("Carnet", Icons.Default.Bookmark),
                    AppDestination("Stats", Icons.Default.QueryStats)
                )
                var selectedIndex by remember { mutableIntStateOf(0) }

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            destinations.forEachIndexed { index, destination ->
                                NavigationBarItem(
                                    selected = selectedIndex == index,
                                    onClick = { selectedIndex = index },
                                    icon = { Icon(destination.icon, contentDescription = null) },
                                    label = { Text(destination.label) }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Text(
                            text = when (selectedIndex) {
                                0 -> "Bienvenue dans votre carnet de lecture"
                                1 -> "Votre bibliothèque"
                                2 -> "Vos notes et annotations"
                                else -> "Vos statistiques de lecture"
                            },
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(androidx.compose.foundation.layout.PaddingValues(all = androidx.compose.ui.unit.dp(24)))
                        )
                    }
                }
            }
        }
    }
}
