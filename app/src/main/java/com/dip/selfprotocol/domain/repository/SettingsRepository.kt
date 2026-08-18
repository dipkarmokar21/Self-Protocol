package com.dip.selfprotocol.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val isDarkTheme: Flow<Boolean>
    suspend fun setDarkTheme(enabled: Boolean)

    val hasAppLockEnabled: Flow<Boolean>
    suspend fun setAppLockEnabled(enabled: Boolean)

    val isBiometricEnabled: Flow<Boolean>
    suspend fun setBiometricEnabled(enabled: Boolean)

    val pin: Flow<String?>
    suspend fun setPin(pin: String?)
    
    val autoLock: Flow<Boolean>
    suspend fun setAutoLock(enabled: Boolean)

    val isScreenshotAllowed: Flow<Boolean>
    suspend fun setScreenshotAllowed(allowed: Boolean)
}
