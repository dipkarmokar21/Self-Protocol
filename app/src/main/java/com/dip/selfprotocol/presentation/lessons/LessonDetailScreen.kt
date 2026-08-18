package com.dip.selfprotocol.presentation.lessons

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.activity.compose.BackHandler
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDetailScreen(
    viewModel: LessonDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val question by viewModel.question.collectAsState()
    val answer by viewModel.answer.collectAsState()
    val hasDraft by viewModel.hasDraft.collectAsState()
    
    // If it's a new lesson (id == null), start in edit mode. Else start in read mode.
    var isEditMode by remember { mutableStateOf(viewModel.lessonId == null) }
    var showDraftDialog by remember { mutableStateOf(false) }

    LaunchedEffect(hasDraft) {
        if (hasDraft) showDraftDialog = true
    }

    val navigateBackWithSave = {
        if (isEditMode) viewModel.saveDraftIfNeeded()
        onNavigateBack()
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, isEditMode) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP && isEditMode) {
                viewModel.saveDraftIfNeeded()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    BackHandler {
        navigateBackWithSave()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = navigateBackWithSave) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            if (isEditMode) {
                FloatingActionButton(
                    onClick = { viewModel.saveLesson { isEditMode = false; if (viewModel.lessonId == null) onNavigateBack() } },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Save")
                }
            } else {
                FloatingActionButton(
                    onClick = { isEditMode = true },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Crossfade(targetState = isEditMode, label = "ReadEditToggle") { editing ->
            if (editing) {
                // EDIT MODE
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .imePadding()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = if (viewModel.lessonId == null) "New Lesson" else "Edit Lesson",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    OutlinedTextField(
                        value = question,
                        onValueChange = viewModel::onQuestionChange,
                        label = { Text("Question") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 2
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    OutlinedTextField(
                        value = answer,
                        onValueChange = viewModel::onAnswerChange,
                        label = { Text("Answer") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 4
                    )
                    Spacer(modifier = Modifier.height(100.dp))
                }
            } else {
                // READ MODE
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .imePadding()
                        .verticalScroll(rememberScrollState())
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(32.dp)) {
                            Text(
                                text = "Question",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = question,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = MaterialTheme.typography.headlineMedium.lineHeight * 1.2
                            )
                            Spacer(modifier = Modifier.height(48.dp))
                            
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF3B82F6).copy(alpha = 0.1f) // BlueAccent
                                )
                            ) {
                                Column(modifier = Modifier.padding(24.dp)) {
                                    Text(
                                        text = "ANSWER & LESSON",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF3B82F6)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = answer,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = MaterialTheme.typography.titleLarge.lineHeight * 1.3
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }

    if (showDraftDialog) {
        AlertDialog(
            onDismissRequest = { showDraftDialog = false },
            title = { Text("Unsaved Draft Found") },
            text = { Text("Do you want to restore your unsaved changes or discard them?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.restoreDraft()
                    showDraftDialog = false
                    isEditMode = true
                }) {
                    Text("Restore")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.discardDraft()
                    showDraftDialog = false
                }) {
                    Text("Discard")
                }
            }
        )
    }
}
