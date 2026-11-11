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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val labels = listOf("Accueil", "Bibliothèque", "Carnet", "Stats")
                val icons = listOf(Icons.Default.Home, Icons.Default.Book, Icons.Default.Bookmark, Icons.Default.QueryStats)
                var selected by remember { mutableIntStateOf(0) }
                Scaffold(bottomBar = {
                    NavigationBar {
                        labels.forEachIndexed { index, label ->
                            NavigationBarItem(
                                selected = selected == index,
                                onClick = { selected = index },
                                icon = { Icon(icons[index], contentDescription = null) },
                                label = { Text(label) }
                            )
                        }
                    }
                }) { padding ->
                    Surface(Modifier.padding(padding)) {
                        Text(
                            text = listOf("Bienvenue dans votre carnet de lecture", "Votre bibliothèque", "Vos notes et annotations", "Vos statistiques de lecture")[selected],
                            modifier = Modifier.padding(24.dp),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            }
        }
    }
}
