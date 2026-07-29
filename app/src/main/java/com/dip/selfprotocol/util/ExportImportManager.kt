package com.dip.selfprotocol.util

import android.content.Context
import android.net.Uri
import com.dip.selfprotocol.data.local.dao.CategoryDao
import com.dip.selfprotocol.data.local.dao.LessonDao
import com.dip.selfprotocol.data.local.dao.RuleDao
import com.dip.selfprotocol.data.local.entity.CategoryEntity
import com.dip.selfprotocol.data.local.entity.LessonEntity
import com.dip.selfprotocol.data.local.entity.RuleEntity
import com.dip.selfprotocol.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext

@Serializable
data class AppBackup(
    val version: Int = 1,
    val categories: List<CategoryEntity>,
    val rules: List<RuleEntity>,
    val lessons: List<LessonEntity>,
    val settings: BackupSettings
)

@Serializable
data class BackupSettings(
    val isDarkTheme: Boolean,
    val hasAppLockEnabled: Boolean,
    val isBiometricEnabled: Boolean,
    val autoLock: Boolean
)

class ExportImportManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val categoryDao: CategoryDao,
    private val ruleDao: RuleDao,
    private val lessonDao: LessonDao,
    private val settingsRepository: SettingsRepository
) {
    private val encryptionKeyString = "SelfProtocolBackupKey32BytesLong" // 32 bytes

    private val MAGIC_HEADER = "SELF_PROTOCOL_EJSON_V1\n"

    suspend fun exportData(uri: Uri): Result<Unit> {
        return try {
            val backup = AppBackup(
                categories = categoryDao.getAllCategoriesSync(),
                rules = ruleDao.getAllRulesSync(),
                lessons = lessonDao.getAllLessonsSync(),
                settings = BackupSettings(
                    isDarkTheme = settingsRepository.isDarkTheme.first(),
                    hasAppLockEnabled = settingsRepository.hasAppLockEnabled.first(),
                    isBiometricEnabled = settingsRepository.isBiometricEnabled.first(),
                    autoLock = settingsRepository.autoLock.first()
                )
            )

            val jsonString = Json.encodeToString(backup)
            val encryptedBytes = encrypt(jsonString)

            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(MAGIC_HEADER.toByteArray(Charsets.UTF_8))
                outputStream.write(encryptedBytes)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun importData(uri: Uri): Result<Unit> {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val magicHeaderBytes = ByteArray(MAGIC_HEADER.toByteArray(Charsets.UTF_8).size)
                val bytesRead = inputStream.read(magicHeaderBytes)
                if (bytesRead != magicHeaderBytes.size) {
                    return Result.failure(Exception("File too small."))
                }
                
                val header = String(magicHeaderBytes, Charsets.UTF_8)
                if (header != MAGIC_HEADER) {
                    return Result.failure(Exception("Invalid file format. Magic Header mismatch."))
                }

                val encryptedBytes = inputStream.readBytes()
                val decryptedJson = decrypt(encryptedBytes)
                
                val backup = Json.decodeFromString<AppBackup>(decryptedJson)
                
                ruleDao.deleteAllRules()
                lessonDao.deleteAllLessons()
                categoryDao.deleteAllCategories()
                
                categoryDao.insertCategories(backup.categories)
                ruleDao.insertRules(backup.rules)
                lessonDao.insertLessons(backup.lessons)
                
                settingsRepository.setDarkTheme(backup.settings.isDarkTheme)
                settingsRepository.setAppLockEnabled(backup.settings.hasAppLockEnabled)
                settingsRepository.setBiometricEnabled(backup.settings.isBiometricEnabled)
                settingsRepository.setAutoLock(backup.settings.autoLock)
                
                Result.success(Unit)
            } ?: Result.failure(Exception("Failed to open file"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun encrypt(data: String): ByteArray {
        val secretKey = SecretKeySpec(encryptionKeyString.toByteArray(Charsets.UTF_8), "AES")
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        
        val iv = ByteArray(12)
        SecureRandom().nextBytes(iv)
        val spec = GCMParameterSpec(128, iv)
        
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec)
        val encryptedData = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        
        return iv + encryptedData
    }

    private fun decrypt(encryptedBytes: ByteArray): String {
        val secretKey = SecretKeySpec(encryptionKeyString.toByteArray(Charsets.UTF_8), "AES")
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        
        val iv = encryptedBytes.sliceArray(0 until 12)
        val actualEncryptedData = encryptedBytes.sliceArray(12 until encryptedBytes.size)
        val spec = GCMParameterSpec(128, iv)
        
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
        val decryptedData = cipher.doFinal(actualEncryptedData)
        
        return String(decryptedData, Charsets.UTF_8)
    }
}
