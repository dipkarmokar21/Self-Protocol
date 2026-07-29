package com.dip.selfprotocol.presentation.lessons

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dip.selfprotocol.data.local.dao.LessonDao
import com.dip.selfprotocol.data.local.entity.LessonEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonDetailViewModel @Inject constructor(
    private val lessonDao: LessonDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val categoryId: Int = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0
    val lessonId: Int? = savedStateHandle.get<String>("lessonId")?.toIntOrNull()

    private val _question = MutableStateFlow("")
    val question = _question.asStateFlow()

    private val _answer = MutableStateFlow("")
    val answer = _answer.asStateFlow()

    private var existingCreatedAt: Long = System.currentTimeMillis()
    private var existingIsFavorite: Boolean = false

    init {
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
            onSaved()
        }
    }
}
