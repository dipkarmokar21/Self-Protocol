package com.dip.selfprotocol.presentation.settings

import android.net.Uri
import android.util.Log
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
    val isScreenshotAllowed = settingsRepository.isScreenshotAllowed

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

    fun setScreenshotAllowed(allowed: Boolean) {
        viewModelScope.launch { settingsRepository.setScreenshotAllowed(allowed) }
    }

    fun setPin(newPin: String?) {
        viewModelScope.launch { settingsRepository.setPin(newPin) }
    }

    fun exportData(uri: Uri) {
        Log.d("SettingsViewModel", "exportData called with URI: $uri")
        viewModelScope.launch {
            try {
                val result = exportImportManager.exportData(uri)
                Log.d("SettingsViewModel", "exportData result: ${if (result.isSuccess) "SUCCESS" else "FAILED: ${result.exceptionOrNull()?.message}"}")
                _exportResult.value = result
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "exportData coroutine crashed", e)
                _exportResult.value = Result.failure(e)
            }
        }
    }

    fun exportDataToDownloads() {
        Log.d("SettingsViewModel", "exportDataToDownloads called")
        viewModelScope.launch {
            try {
                val result = exportImportManager.exportDataToDownloads()
                Log.d("SettingsViewModel", "exportDataToDownloads result: ${if (result.isSuccess) "SUCCESS" else "FAILED: ${result.exceptionOrNull()?.message}"}")
                _exportResult.value = result
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "exportDataToDownloads coroutine crashed", e)
                _exportResult.value = Result.failure(e)
            }
        }
    }

    fun importData(uri: Uri) {
        Log.d("SettingsViewModel", "importData called with URI: $uri")
        viewModelScope.launch {
            try {
                val result = exportImportManager.importData(uri)
                Log.d("SettingsViewModel", "importData result: ${if (result.isSuccess) "SUCCESS" else "FAILED: ${result.exceptionOrNull()?.message}"}")
                _importResult.value = result
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "importData coroutine crashed", e)
                _importResult.value = Result.failure(e)
            }
        }
    }

    fun clearExportResult() { _exportResult.value = null }
    fun clearImportResult() { _importResult.value = null }
}
