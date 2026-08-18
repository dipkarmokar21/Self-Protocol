package com.dip.selfprotocol.presentation.rules

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dip.selfprotocol.data.local.dao.CategoryDao
import com.dip.selfprotocol.data.local.dao.RuleDao
import com.dip.selfprotocol.data.local.entity.RuleEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RulesViewModel @Inject constructor(
    private val ruleDao: RuleDao,
    private val categoryDao: CategoryDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val categoryId: Int = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0

    val targetCategories = categoryDao.getRuleCategoriesWithCount().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val baseRules = ruleDao.getRulesByCategory(categoryId)

    val rules = baseRules.combine(_searchQuery) { list, q ->
        if (q.isBlank()) list else list.filter { 
            it.question.contains(q, ignoreCase = true) || 
            it.brutalAnswer.contains(q, ignoreCase = true) || 
            it.rule.contains(q, ignoreCase = true) 
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedRuleIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedRuleIds = _selectedRuleIds.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun toggleSelection(ruleId: Int) {
        val current = _selectedRuleIds.value
        _selectedRuleIds.value = if (current.contains(ruleId)) {
            current - ruleId
        } else {
            current + ruleId
        }
    }

    fun clearSelection() {
        _selectedRuleIds.value = emptySet()
    }

    fun deleteSelectedRules() {
        val idsToDelete = _selectedRuleIds.value
        if (idsToDelete.isEmpty()) return
        
        viewModelScope.launch {
            val rulesToDelete = rules.value.filter { it.id in idsToDelete }
            ruleDao.deleteRules(rulesToDelete)
            clearSelection()
        }
    }

    fun selectAll() {
        _selectedRuleIds.value = rules.value.map { it.id }.toSet()
    }

    fun moveSelectedRules(newCategoryId: Int) {
        val idsToMove = _selectedRuleIds.value
        if (idsToMove.isEmpty()) return
        
        viewModelScope.launch {
            val rulesToMove = rules.value.filter { it.id in idsToMove }
            rulesToMove.forEach { 
                ruleDao.updateRule(it.copy(categoryId = newCategoryId))
            }
            clearSelection()
        }
    }

    fun copySelectedRules(newCategoryId: Int) {
        val idsToCopy = _selectedRuleIds.value
        if (idsToCopy.isEmpty()) return
        
        viewModelScope.launch {
            val rulesToCopy = rules.value.filter { it.id in idsToCopy }
            val newRules = rulesToCopy.map { 
                it.copy(id = 0, categoryId = newCategoryId, createdAt = System.currentTimeMillis(), lastEditedAt = System.currentTimeMillis()) 
            }
            ruleDao.insertRules(newRules)
            clearSelection()
        }
    }
}
