package com.dip.selfprotocol.presentation.lessons

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dip.selfprotocol.data.local.dao.CategoryDao
import com.dip.selfprotocol.data.local.dao.LessonDao
import com.dip.selfprotocol.data.local.entity.LessonEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonsViewModel @Inject constructor(
    private val lessonDao: LessonDao,
    private val categoryDao: CategoryDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val categoryId: Int = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0

    val targetCategories = categoryDao.getLessonCategoriesWithCount().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val baseLessons = lessonDao.getLessonsByCategory(categoryId)

    val lessons = baseLessons.combine(_searchQuery) { list, q ->
        if (q.isBlank()) list else list.filter { 
            it.question.contains(q, ignoreCase = true) || 
            it.answer.contains(q, ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedLessonIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedLessonIds = _selectedLessonIds.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun toggleSelection(lessonId: Int) {
        val current = _selectedLessonIds.value
        _selectedLessonIds.value = if (current.contains(lessonId)) {
            current - lessonId
        } else {
            current + lessonId
        }
    }

    fun clearSelection() {
        _selectedLessonIds.value = emptySet()
    }

    fun deleteSelectedLessons() {
        val idsToDelete = _selectedLessonIds.value
        if (idsToDelete.isEmpty()) return
        
        viewModelScope.launch {
            val lessonsToDelete = lessons.value.filter { it.id in idsToDelete }
            lessonDao.deleteLessons(lessonsToDelete)
            clearSelection()
        }
    }

    fun selectAll() {
        _selectedLessonIds.value = lessons.value.map { it.id }.toSet()
    }

    fun moveSelectedLessons(newCategoryId: Int) {
        val idsToMove = _selectedLessonIds.value
        if (idsToMove.isEmpty()) return
        
        viewModelScope.launch {
            val lessonsToMove = lessons.value.filter { it.id in idsToMove }
            lessonsToMove.forEach { 
                lessonDao.updateLesson(it.copy(categoryId = newCategoryId))
            }
            clearSelection()
        }
    }

    fun copySelectedLessons(newCategoryId: Int) {
        val idsToCopy = _selectedLessonIds.value
        if (idsToCopy.isEmpty()) return
        
        viewModelScope.launch {
            val lessonsToCopy = lessons.value.filter { it.id in idsToCopy }
            val newLessons = lessonsToCopy.map { 
                it.copy(id = 0, categoryId = newCategoryId, createdAt = System.currentTimeMillis(), lastEditedAt = System.currentTimeMillis()) 
            }
            lessonDao.insertLessons(newLessons)
            clearSelection()
        }
    }
}
