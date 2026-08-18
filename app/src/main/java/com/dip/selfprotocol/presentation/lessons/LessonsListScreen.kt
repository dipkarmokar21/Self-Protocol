package com.dip.selfprotocol.presentation.lessons

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dip.selfprotocol.data.local.dao.CategoryWithCount
import com.dip.selfprotocol.data.local.entity.LessonEntity
import com.dip.selfprotocol.presentation.components.EmptyState
import com.dip.selfprotocol.util.bounceClick
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonsListScreen(
    viewModel: LessonsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Int?) -> Unit
) {
    val lessons by viewModel.lessons.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val listState = rememberLazyListState()
    val selectedIds by viewModel.selectedLessonIds.collectAsState()
    val targetCategories by viewModel.targetCategories.collectAsState()
    val isSelectionMode = selectedIds.isNotEmpty()

    var deleteDialogTrigger by remember { mutableStateOf(false) }
    var showMoveDialog by remember { mutableStateOf(false) }
    var showCopyDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isSelectionMode) "${selectedIds.size} Selected" else "Past Lessons", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    if (isSelectionMode) {
                        IconButton(onClick = { viewModel.clearSelection() }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear Selection")
                        }
                    } else {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    if (isSelectionMode) {
                        IconButton(onClick = { viewModel.selectAll() }) {
                            Icon(Icons.Default.SelectAll, contentDescription = "Select All")
                        }
                        IconButton(onClick = { showMoveDialog = true }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Move")
                        }
                        IconButton(onClick = { showCopyDialog = true }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                        }
                        IconButton(onClick = { deleteDialogTrigger = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Selected", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            if (!isSelectionMode) {
                FloatingActionButton(
                    onClick = { onNavigateToDetail(null) },
                    containerColor = Color(0xFF3B82F6), // BlueAccent
                    contentColor = Color.White,
                    modifier = Modifier.bounceClick { onNavigateToDetail(null) }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Lesson")
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (!isSelectionMode) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Search Lessons", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                )
            }

            if (lessons.isEmpty() && searchQuery.isBlank()) {
                EmptyState(
                    title = "No Lessons Yet",
                    subtitle = "Reflect on your past mistakes or breakthroughs and document them here.",
                    icon = Icons.Default.MenuBook,
                    iconTint = Color(0xFF3B82F6)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp)
                ) {
                    itemsIndexed(lessons, key = { _, lesson -> lesson.id }) { index, lesson ->
                        val isSelected = selectedIds.contains(lesson.id)
                        LessonItem(
                            lesson = lesson,
                            index = index + 1,
                            isSelected = isSelected,
                            onClick = {
                                if (isSelectionMode) {
                                    viewModel.toggleSelection(lesson.id)
                                } else {
                                    onNavigateToDetail(lesson.id)
                                }
                            },
                            onLongClick = {
                                viewModel.toggleSelection(lesson.id)
                            }
                        )
                    }
                }
            }
        }
    }

    if (deleteDialogTrigger) {
        AlertDialog(
            onDismissRequest = { 
                deleteDialogTrigger = false
            },
            title = { Text("Delete Lessons") },
            text = { Text("Are you sure you want to delete the selected lessons?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteSelectedLessons()
                        deleteDialogTrigger = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteDialogTrigger = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showMoveDialog) {
        CategorySelectionDialog(
            title = "Move Lessons",
            categories = targetCategories,
            currentCategoryId = viewModel.categoryId,
            onDismiss = { showMoveDialog = false },
            onCategorySelected = { targetId ->
                viewModel.moveSelectedLessons(targetId)
                showMoveDialog = false
            }
        )
    }

    if (showCopyDialog) {
        CategorySelectionDialog(
            title = "Copy Lessons",
            categories = targetCategories,
            currentCategoryId = viewModel.categoryId,
            onDismiss = { showCopyDialog = false },
            onCategorySelected = { targetId ->
                viewModel.copySelectedLessons(targetId)
                showCopyDialog = false
            }
        )
    }
}

@Composable
fun CategorySelectionDialog(
    title: String,
    categories: List<CategoryWithCount>,
    currentCategoryId: Int,
    onDismiss: () -> Unit,
    onCategorySelected: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            LazyColumn {
                items(categories.filter { it.category.id != currentCategoryId }) { catWithCount ->
                    Text(
                        text = catWithCount.category.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCategorySelected(catWithCount.category.id) }
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                if (categories.filter { it.category.id != currentCategoryId }.isEmpty()) {
                    item {
                        Text("No other categories available.", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private val DateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LessonItem(
    lesson: LessonEntity,
    index: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val dateString = remember(lesson.createdAt) { DateFormatter.format(Date(lesson.createdAt)) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .bounceClick { onClick() }
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary 
                        else Color(0xFF3B82F6).copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Text(
                        text = index.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3B82F6)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lesson.question,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = lesson.answer,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF3B82F6))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Lesson • $dateString",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
