package com.dip.selfprotocol.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.dip.selfprotocol.data.local.dao.CategoryDao
import com.dip.selfprotocol.data.local.dao.LessonDao
import com.dip.selfprotocol.data.local.dao.RuleDao
import com.dip.selfprotocol.data.local.entity.CategoryEntity
import com.dip.selfprotocol.data.local.entity.LessonEntity
import com.dip.selfprotocol.data.local.entity.RuleEntity
import com.dip.selfprotocol.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
    val autoLock: Boolean,
    val isScreenshotAllowed: Boolean = false
)

class ExportImportManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val categoryDao: CategoryDao,
    private val ruleDao: RuleDao,
    private val lessonDao: LessonDao,
    private val settingsRepository: SettingsRepository
) {
    private val TAG = "ExportImportManager"
    private val encryptionKeyString = "SelfProtocolBackupKey32BytesLong" // 32 bytes

    private val MAGIC_HEADER = "SELF_PROTOCOL_EJSON_V1\n"

    suspend fun exportData(uri: Uri): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Export started, URI: $uri")
                val totalBytes = createExportBytes()
                writeExportBytes(uri, totalBytes)
                Log.d(TAG, "Export completed successfully, wrote ${totalBytes.size} bytes")
                Result.success(Unit)
            } catch (e: Exception) {
                Log.e(TAG, "Export failed", e)
                Result.failure(e)
            }
        }
    }

    suspend fun exportDataToDownloads(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                return@withContext Result.failure(Exception("Direct export needs Android 10 or newer."))
            }

            var exportUri: Uri? = null
            try {
                val fileName = createExportFileName()
                val totalBytes = createExportBytes()
                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }

                exportUri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                    ?: return@withContext Result.failure(Exception("Failed to create export file in Downloads"))

                Log.d(TAG, "Direct export started, URI: $exportUri")
                writeExportBytes(exportUri, totalBytes)

                values.clear()
                values.put(MediaStore.MediaColumns.IS_PENDING, 0)
                context.contentResolver.update(exportUri, values, null, null)

                Log.d(TAG, "Direct export completed: $fileName, wrote ${totalBytes.size} bytes")
                Result.success(Unit)
            } catch (e: Exception) {
                exportUri?.let { context.contentResolver.delete(it, null, null) }
                Log.e(TAG, "Direct export failed", e)
                Result.failure(e)
            }
        }
    }

    suspend fun importData(uri: Uri): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val magicHeaderBytes = ByteArray(MAGIC_HEADER.toByteArray(Charsets.UTF_8).size)
                    val bytesRead = inputStream.read(magicHeaderBytes)
                    if (bytesRead != magicHeaderBytes.size) {
                        return@withContext Result.failure(Exception("File too small."))
                    }

                    val header = String(magicHeaderBytes, Charsets.UTF_8)
                    if (header != MAGIC_HEADER) {
                        return@withContext Result.failure(Exception("Invalid file format. Magic Header mismatch."))
                    }

                    val encryptedBytes = inputStream.readBytes()
                    if (encryptedBytes.size <= 12) {
                        return@withContext Result.failure(Exception("Backup data is empty or corrupted."))
                    }

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
                    settingsRepository.setScreenshotAllowed(backup.settings.isScreenshotAllowed)

                    Result.success(Unit)
                } ?: Result.failure(Exception("Failed to open file"))
            } catch (e: Exception) {
                Log.e(TAG, "Import failed", e)
                Result.failure(e)
            }
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

    private suspend fun createExportBytes(): ByteArray {
        val categories = categoryDao.getAllCategoriesSync()
        val rules = ruleDao.getAllRulesSync()
        val lessons = lessonDao.getAllLessonsSync()

        Log.d(TAG, "Data fetched - Categories: ${categories.size}, Rules: ${rules.size}, Lessons: ${lessons.size}")

        val backup = AppBackup(
            categories = categories,
            rules = rules,
            lessons = lessons,
            settings = BackupSettings(
                isDarkTheme = settingsRepository.isDarkTheme.first(),
                hasAppLockEnabled = settingsRepository.hasAppLockEnabled.first(),
                isBiometricEnabled = settingsRepository.isBiometricEnabled.first(),
                autoLock = settingsRepository.autoLock.first(),
                isScreenshotAllowed = settingsRepository.isScreenshotAllowed.first()
            )
        )

        val jsonString = Json.encodeToString(backup)
        Log.d(TAG, "JSON size: ${jsonString.length} chars")

        val encryptedBytes = encrypt(jsonString)
        Log.d(TAG, "Encrypted size: ${encryptedBytes.size} bytes")

        val headerBytes = MAGIC_HEADER.toByteArray(Charsets.UTF_8)
        val totalBytes = headerBytes + encryptedBytes
        Log.d(TAG, "Total bytes to write: ${totalBytes.size}")
        return totalBytes
    }

    private fun writeExportBytes(uri: Uri, bytes: ByteArray) {
        context.contentResolver.openOutputStream(uri, "wt")?.use { outputStream ->
            outputStream.write(bytes)
            outputStream.flush()
        } ?: throw IOException("Failed to open export file")
    }

    private fun createExportFileName(): String {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        return "SelfProtocol_Backup_$timestamp.ejson"
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
