package com.dip.selfprotocol.presentation.categories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dip.selfprotocol.data.local.dao.CategoryDao
import com.dip.selfprotocol.data.local.entity.CategoryEntity
import com.dip.selfprotocol.domain.model.CategoryType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryDao: CategoryDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryTypeString: String = savedStateHandle.get<String>("type") ?: CategoryType.RULE.name
    val categoryType = CategoryType.valueOf(categoryTypeString)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val baseCategories = if (categoryType == CategoryType.RULE) {
        categoryDao.getRuleCategoriesWithCount()
    } else {
        categoryDao.getLessonCategoriesWithCount()
    }

    val categories = baseCategories.combine(_searchQuery) { list, q ->
        if (q.isBlank()) list else list.filter { it.category.name.contains(q, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    private val _selectedCategoryIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedCategoryIds = _selectedCategoryIds.asStateFlow()

    fun toggleSelection(categoryId: Int) {
        val current = _selectedCategoryIds.value
        _selectedCategoryIds.value = if (current.contains(categoryId)) {
            current - categoryId
        } else {
            current + categoryId
        }
    }

    fun clearSelection() {
        _selectedCategoryIds.value = emptySet()
    }

    fun deleteSelectedCategories() {
        val idsToDelete = _selectedCategoryIds.value
        if (idsToDelete.isEmpty()) return
        
        viewModelScope.launch {
            val categoriesToDelete = categories.value
                .filter { it.category.id in idsToDelete }
                .map { it.category }
            categoryDao.deleteCategories(categoriesToDelete)
            clearSelection()
        }
    }

    fun addCategory(name: String) {
        viewModelScope.launch {
            categoryDao.insertCategory(
                CategoryEntity(name = name, type = categoryType, isDefault = false)
            )
        }
    }

    fun selectAll() {
        val currentIds = categories.value.map { it.category.id }.toSet()
        _selectedCategoryIds.value = currentIds
    }

    fun renameCategory(id: Int, newName: String) {
        viewModelScope.launch {
            val cat = categories.value.find { it.category.id == id }?.category
            if (cat != null) {
                categoryDao.updateCategory(cat.copy(name = newName))
                clearSelection()
            }
        }
    }

}
