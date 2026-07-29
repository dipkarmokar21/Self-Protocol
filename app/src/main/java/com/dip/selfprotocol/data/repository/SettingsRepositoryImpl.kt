package com.dip.selfprotocol.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.dip.selfprotocol.domain.repository.SettingsRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class SettingsRepositoryImpl(
    context: Context
) : SettingsRepository {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_settings",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private fun getFlowForBoolean(key: String, defaultValue: Boolean): Flow<Boolean> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, changedKey ->
            if (key == changedKey) {
                trySend(prefs.getBoolean(key, defaultValue))
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        trySend(sharedPreferences.getBoolean(key, defaultValue))
        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    private fun getFlowForString(key: String, defaultValue: String?): Flow<String?> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, changedKey ->
            if (key == changedKey) {
                trySend(prefs.getString(key, defaultValue))
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        trySend(sharedPreferences.getString(key, defaultValue))
        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    override val isDarkTheme: Flow<Boolean> = getFlowForBoolean("DARK_THEME", true)
    override suspend fun setDarkTheme(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("DARK_THEME", enabled).apply()
    }

    override val hasAppLockEnabled: Flow<Boolean> = getFlowForBoolean("APP_LOCK", false)
    override suspend fun setAppLockEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("APP_LOCK", enabled).apply()
    }

    override val isBiometricEnabled: Flow<Boolean> = getFlowForBoolean("BIOMETRIC", false)
    override suspend fun setBiometricEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("BIOMETRIC", enabled).apply()
    }

    override val pin: Flow<String?> = getFlowForString("PIN", null)
    override suspend fun setPin(pin: String?) {
        sharedPreferences.edit().putString("PIN", pin).apply()
    }

    override val autoLock: Flow<Boolean> = getFlowForBoolean("AUTO_LOCK", true)
    override suspend fun setAutoLock(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("AUTO_LOCK", enabled).apply()
    }
}
