package com.foulag.readingtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foulag.readingtracker.data.local.BookEntity
import com.foulag.readingtracker.data.local.ReadingStatus
import com.foulag.readingtracker.ui.library.LibraryViewModel
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
                val libraryViewModel: LibraryViewModel = viewModel()
                val books by libraryViewModel.books.collectAsStateWithLifecycle()
                var selectedIndex by remember { mutableStateOf(0) }

                val destinations = listOf(
                    AppDestination("Accueil", Icons.Default.Home),
                    AppDestination("Bibliothèque", Icons.Default.Book),
                    AppDestination("Carnet", Icons.Default.Bookmark),
                    AppDestination("Stats", Icons.Default.QueryStats)
                )

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
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        when (selectedIndex) {
                            0 -> HomeScreen(books, onOpenLibrary = { selectedIndex = 1 })
                            1 -> LibraryScreen(
                                books = books,
                                onAddBook = libraryViewModel::addBook,
                                onAdvancePage = libraryViewModel::advancePage,
                                onUpdateStatus = libraryViewModel::updateStatus,
                                onDeleteBook = libraryViewModel::deleteBook
                            )
                            2 -> PlaceholderScreen("Le carnet de notes arrive dans une prochaine étape.")
                            else -> PlaceholderScreen("Les statistiques arriveront après le suivi des lectures.")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(books: List<BookEntity>, onOpenLibrary: () -> Unit) {
    val readingCount = books.count { it.status == ReadingStatus.READING }
    val readCount = books.count { it.status == ReadingStatus.READ }
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Reading Tracker", style = MaterialTheme.typography.headlineMedium)
        Text("Votre espace de lecture", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard("En cours", readingCount.toString(), Modifier.weight(1f))
            SummaryCard("Terminés", readCount.toString(), Modifier.weight(1f))
        }
        Text("${books.size} livre(s) dans votre bibliothèque")
        Button(onClick = onOpenLibrary) { Text("Ouvrir ma bibliothèque") }
        if (books.isEmpty()) {
            Text("Ajoutez votre premier livre pour commencer à suivre vos lectures.")
        }
    }
}

@Composable
private fun SummaryCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(Modifier.padding(16.dp)) {
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun LibraryScreen(
    books: List<BookEntity>,
    onAddBook: (String, String, Int?) -> Unit,
    onAdvancePage: (BookEntity) -> Unit,
    onUpdateStatus: (BookEntity, ReadingStatus) -> Unit,
    onDeleteBook: (Long) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Ma bibliothèque", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = { showAddDialog = true }) { Text("Ajouter") }
        }
        if (books.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Votre bibliothèque est vide.", style = MaterialTheme.typography.titleMedium)
                Text("Ajoutez un livre manuellement pour commencer.")
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(books, key = { it.id }) { book ->
                    BookCard(
                        book = book,
                        onAdvancePage = { onAdvancePage(book) },
                        onUpdateStatus = { status -> onUpdateStatus(book, status) },
                        onDelete = { onDeleteBook(book.id) }
                    )
                }
            }
        }
    }
    if (showAddDialog) {
        AddBookDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { title, author, pages ->
                onAddBook(title, author, pages)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun BookCard(
    book: BookEntity,
    onAdvancePage: () -> Unit,
    onUpdateStatus: (ReadingStatus) -> Unit,
    onDelete: () -> Unit
) {
    var statusMenuExpanded by remember { mutableStateOf(false) }
    Card(shape = RoundedCornerShape(16.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(book.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    if (book.author.isNotBlank()) Text(book.author, style = MaterialTheme.typography.bodyMedium)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Supprimer le livre")
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { statusMenuExpanded = true }) { Text(book.status.toFrenchLabel()) }
                DropdownMenu(expanded = statusMenuExpanded, onDismissRequest = { statusMenuExpanded = false }) {
                    ReadingStatus.entries.forEach { status ->
                        DropdownMenuItem(
                            text = { Text(status.toFrenchLabel()) },
                            onClick = {
                                onUpdateStatus(status)
                                statusMenuExpanded = false
                            }
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
                Text(
                    if (book.pageCount != null) "p. ${book.currentPage} / ${book.pageCount}" else "p. ${book.currentPage}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            if (book.pageCount != null && book.pageCount > 0) {
                LinearProgressIndicator(
                    progress = { book.progressPercent / 100f },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("${book.progressPercent} %", style = MaterialTheme.typography.labelSmall)
            }
            Button(
                onClick = onAdvancePage,
                enabled = book.status != ReadingStatus.READ && (book.pageCount == null || book.currentPage < book.pageCount)
            ) {
                Text("Page suivante")
            }
        }
    }
}

@Composable
private fun AddBookDialog(onDismiss: () -> Unit, onAdd: (String, String, Int?) -> Unit) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var pageCount by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ajouter un livre") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Titre") }, singleLine = true)
                OutlinedTextField(value = author, onValueChange = { author = it }, label = { Text("Auteur (facultatif)") }, singleLine = true)
                OutlinedTextField(
                    value = pageCount,
                    onValueChange = { pageCount = it.filter(Char::isDigit) },
                    label = { Text("Nombre de pages (facultatif)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onAdd(title, author, pageCount.toIntOrNull()?.takeIf { it > 0 }) },
                enabled = title.isNotBlank()
            ) { Text("Ajouter") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annuler") } }
    )
}

@Composable
private fun PlaceholderScreen(message: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(message, style = MaterialTheme.typography.titleMedium)
    }
}

private fun ReadingStatus.toFrenchLabel(): String = when (this) {
    ReadingStatus.TO_READ -> "À lire"
    ReadingStatus.READING -> "En cours"
    ReadingStatus.READ -> "Lu"
    ReadingStatus.PAUSED -> "En pause"
    ReadingStatus.ABANDONED -> "Abandonné"
}
