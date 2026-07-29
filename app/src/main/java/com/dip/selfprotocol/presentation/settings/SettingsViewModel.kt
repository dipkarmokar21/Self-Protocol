package com.dip.selfprotocol.presentation.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dip.selfprotocol.domain.repository.SettingsRepository
import com.dip.selfprotocol.util.ExportImportManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val exportImportManager: ExportImportManager
) : ViewModel() {

    val isDarkTheme = settingsRepository.isDarkTheme
    val hasAppLockEnabled = settingsRepository.hasAppLockEnabled
    val isBiometricEnabled = settingsRepository.isBiometricEnabled
    val autoLock = settingsRepository.autoLock
    val pin = settingsRepository.pin

    private val _exportResult = MutableStateFlow<Result<Unit>?>(null)
    val exportResult = _exportResult.asStateFlow()

    private val _importResult = MutableStateFlow<Result<Unit>?>(null)
    val importResult = _importResult.asStateFlow()

    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setDarkTheme(enabled) }
    }

    fun setAppLockEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setAppLockEnabled(enabled) }
    }

    fun setBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setBiometricEnabled(enabled) }
    }

    fun setAutoLock(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setAutoLock(enabled) }
    }

    fun setPin(newPin: String?) {
        viewModelScope.launch { settingsRepository.setPin(newPin) }
    }

    fun exportData(uri: Uri) {
        viewModelScope.launch {
            _exportResult.value = exportImportManager.exportData(uri)
        }
    }

    fun importData(uri: Uri) {
        viewModelScope.launch {
            _importResult.value = exportImportManager.importData(uri)
        }
    }

    fun clearExportResult() { _exportResult.value = null }
    fun clearImportResult() { _importResult.value = null }
}
