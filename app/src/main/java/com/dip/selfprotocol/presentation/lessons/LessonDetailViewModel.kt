package com.dip.selfprotocol.presentation.lessons

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dip.selfprotocol.data.local.dao.LessonDao
import com.dip.selfprotocol.data.local.entity.LessonEntity
import com.dip.selfprotocol.util.DraftManager
import com.dip.selfprotocol.util.LessonDraft
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonDetailViewModel @Inject constructor(
    private val lessonDao: LessonDao,
    private val draftManager: DraftManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val categoryId: Int = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0
    val lessonId: Int? = savedStateHandle.get<String>("lessonId")?.toIntOrNull()

    private val _question = MutableStateFlow("")
    val question = _question.asStateFlow()

    private val _answer = MutableStateFlow("")
    val answer = _answer.asStateFlow()

    private val _hasDraft = MutableStateFlow(false)
    val hasDraft = _hasDraft.asStateFlow()

    private var existingCreatedAt: Long = System.currentTimeMillis()
    private var existingIsFavorite: Boolean = false

    private val draftKey: String
        get() = if (lessonId != null) "lesson_$lessonId" else "new_$categoryId"

    init {
        val draft = draftManager.getLessonDraft(draftKey)
        if (draft != null) {
            _hasDraft.value = true
        }

        if (lessonId != null) {
            viewModelScope.launch {
                val existingLesson = lessonDao.getAllLessonsSync().find { it.id == lessonId }
                existingLesson?.let {
                    _question.value = it.question
                    _answer.value = it.answer
                    existingCreatedAt = it.createdAt
                    existingIsFavorite = it.isFavorite
                }
            }
        }
    }

    fun onQuestionChange(value: String) { _question.value = value }
    fun onAnswerChange(value: String) { _answer.value = value }

    fun restoreDraft() {
        draftManager.getLessonDraft(draftKey)?.let { draft ->
            _question.value = draft.title
            _answer.value = draft.content
        }
        _hasDraft.value = false
    }

    fun discardDraft() {
        draftManager.clearLessonDraft(draftKey)
        _hasDraft.value = false
    }

    fun saveDraftIfNeeded() {
        if (_question.value.isNotBlank() || _answer.value.isNotBlank()) {
            draftManager.saveLessonDraft(
                draftKey,
                LessonDraft(_question.value, _answer.value)
            )
        }
    }

    fun saveLesson(onSaved: () -> Unit) {
        viewModelScope.launch {
            val entity = LessonEntity(
                id = lessonId ?: 0,
                categoryId = categoryId,
                question = question.value,
                answer = answer.value,
                createdAt = if (lessonId != null) existingCreatedAt else System.currentTimeMillis(),
                lastEditedAt = System.currentTimeMillis(),
                isFavorite = existingIsFavorite
            )
            
            if (lessonId != null) {
                lessonDao.updateLesson(entity)
            } else {
                lessonDao.insertLesson(entity)
            }
            draftManager.clearLessonDraft(draftKey)
            onSaved()
        }
    }
}
