package com.dip.selfprotocol.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class RuleDraft(
    val question: String,
    val brutalAnswer: String,
    val rule: String
)

@Serializable
data class LessonDraft(
    val title: String,
    val content: String
)

@Singleton
class DraftManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("editor_drafts", Context.MODE_PRIVATE)
    
    // --- Rule Drafts ---
    
    fun saveRuleDraft(id: String, draft: RuleDraft) {
        prefs.edit().putString("rule_draft_$id", Json.encodeToString(draft)).apply()
    }
    
    fun getRuleDraft(id: String): RuleDraft? {
        val json = prefs.getString("rule_draft_$id", null) ?: return null
        return try {
            Json.decodeFromString<RuleDraft>(json)
        } catch (e: Exception) {
            null
        }
    }
    
    fun clearRuleDraft(id: String) {
        prefs.edit().remove("rule_draft_$id").apply()
    }
    
    // --- Lesson Drafts ---
    
    fun saveLessonDraft(id: String, draft: LessonDraft) {
        prefs.edit().putString("lesson_draft_$id", Json.encodeToString(draft)).apply()
    }
    
    fun getLessonDraft(id: String): LessonDraft? {
        val json = prefs.getString("lesson_draft_$id", null) ?: return null
        return try {
            Json.decodeFromString<LessonDraft>(json)
        } catch (e: Exception) {
            null
        }
    }
    
    fun clearLessonDraft(id: String) {
        prefs.edit().remove("lesson_draft_$id").apply()
    }
}
