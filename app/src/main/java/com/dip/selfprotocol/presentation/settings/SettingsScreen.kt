package com.dip.selfprotocol.presentation.settings

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsState(initial = true)
    val hasAppLockEnabled by viewModel.hasAppLockEnabled.collectAsState(initial = false)
    val isBiometricEnabled by viewModel.isBiometricEnabled.collectAsState(initial = false)
    val autoLock by viewModel.autoLock.collectAsState(initial = true)
    val pin by viewModel.pin.collectAsState(initial = null)
    
    val exportResult by viewModel.exportResult.collectAsState()
    val importResult by viewModel.importResult.collectAsState()

    val context = LocalContext.current

    var showPinDialog by remember { mutableStateOf(false) }
    var pinInput by remember { mutableStateOf("") }
    
    var showImportConfirmDialog by remember { mutableStateOf(false) }
    var importUriToConfirm by remember { mutableStateOf<android.net.Uri?>(null) }

    val exportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/octet-stream")) { uri ->
        uri?.let { viewModel.exportData(it) }
    }

    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let { 
            importUriToConfirm = it
            showImportConfirmDialog = true
        }
    }

    LaunchedEffect(exportResult) {
        exportResult?.let {
            if (it.isSuccess) {
                Toast.makeText(context, "Export successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Export failed: ${it.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
            }
            viewModel.clearExportResult()
        }
    }

    LaunchedEffect(importResult) {
        importResult?.let {
            if (it.isSuccess) {
                Toast.makeText(context, "Import successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Import failed: ${it.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
            }
            viewModel.clearImportResult()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            SettingsCategoryTitle("Appearance")
            SettingsSwitch(
                title = "Dark Theme",
                checked = isDarkTheme,
                onCheckedChange = viewModel::setDarkTheme
            )

            SettingsCategoryTitle("Security")
            SettingsSwitch(
                title = "App Lock",
                checked = hasAppLockEnabled,
                onCheckedChange = { 
                    if (it && pin == null) {
                        showPinDialog = true
                    } else {
                        viewModel.setAppLockEnabled(it)
                    }
                }
            )
            
            if (hasAppLockEnabled) {
                SettingsSwitch(
                    title = "Biometric Unlock",
                    checked = isBiometricEnabled,
                    onCheckedChange = viewModel::setBiometricEnabled
                )
                SettingsSwitch(
                    title = "Auto Lock (Background)",
                    checked = autoLock,
                    onCheckedChange = viewModel::setAutoLock
                )
                SettingsItem(
                    title = "Change PIN",
                    onClick = { showPinDialog = true }
                )
            }

            SettingsCategoryTitle("Backup")
            SettingsItem(
                title = "Export Pack",
                subtitle = "Export to an encrypted .ejson file",
                onClick = { exportLauncher.launch("SelfProtocol_Backup.ejson") }
            )
            SettingsItem(
                title = "Import Pack",
                subtitle = "Restore from an encrypted .ejson file",
                onClick = { importLauncher.launch(arrayOf("application/octet-stream", "*/*")) }
            )
            
            SettingsCategoryTitle("About")
            SettingsItem(
                title = "Version",
                subtitle = "1.0.0",
                onClick = {}
            )
            SettingsItem(
                title = "Philosophy",
                subtitle = "Rules Over Emotions",
                onClick = {}
            )
        }
    }

    if (showPinDialog) {
        AlertDialog(
            onDismissRequest = { showPinDialog = false },
            title = { Text(if (pin == null) "Set PIN" else "Change PIN") },
            text = {
                OutlinedTextField(
                    value = pinInput,
                    onValueChange = { if (it.length <= 6 && it.all { char -> char.isDigit() }) pinInput = it },
                    label = { Text("Enter 6-digit PIN") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (pinInput.length == 6) {
                            viewModel.setPin(pinInput)
                            viewModel.setAppLockEnabled(true)
                            showPinDialog = false
                            pinInput = ""
                        }
                    },
                    enabled = pinInput.length == 6
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPinDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    if (showImportConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showImportConfirmDialog = false },
            title = { Text("Replace Everything?") },
            text = { Text("Importing a backup will completely replace your current database and settings. This cannot be undone.") },
            confirmButton = {
                Button(onClick = {
                    importUriToConfirm?.let { viewModel.importData(it) }
                    showImportConfirmDialog = false
                }) {
                    Text("Import")
                }
            },
            dismissButton = {
                TextButton(onClick = { showImportConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SettingsCategoryTitle(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingsSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
