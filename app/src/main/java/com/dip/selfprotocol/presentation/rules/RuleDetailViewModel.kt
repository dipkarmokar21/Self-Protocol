package com.dip.selfprotocol.presentation.rules

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dip.selfprotocol.data.local.dao.RuleDao
import com.dip.selfprotocol.data.local.entity.RuleEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RuleDetailViewModel @Inject constructor(
    private val ruleDao: RuleDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val categoryId: Int = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0
    val ruleId: Int? = savedStateHandle.get<String>("ruleId")?.toIntOrNull()

    private val _question = MutableStateFlow("")
    val question = _question.asStateFlow()

    private val _brutalAnswer = MutableStateFlow("")
    val brutalAnswer = _brutalAnswer.asStateFlow()

    private val _rule = MutableStateFlow("")
    val rule = _rule.asStateFlow()

    private var existingCreatedAt: Long = System.currentTimeMillis()
    private var existingIsFavorite: Boolean = false

    init {
        if (ruleId != null) {
            viewModelScope.launch {
                val existingRule = ruleDao.getAllRulesSync().find { it.id == ruleId }
                existingRule?.let {
                    _question.value = it.question
                    _brutalAnswer.value = it.brutalAnswer
                    _rule.value = it.rule
                    existingCreatedAt = it.createdAt
                    existingIsFavorite = it.isFavorite
                }
            }
        }
    }

    fun onQuestionChange(value: String) { _question.value = value }
    fun onBrutalAnswerChange(value: String) { _brutalAnswer.value = value }
    fun onRuleChange(value: String) { _rule.value = value }

    fun saveRule(onSaved: () -> Unit) {
        viewModelScope.launch {
            val entity = RuleEntity(
                id = ruleId ?: 0,
                categoryId = categoryId,
                question = question.value,
                brutalAnswer = brutalAnswer.value,
                rule = rule.value,
                createdAt = if (ruleId != null) existingCreatedAt else System.currentTimeMillis(),
                lastEditedAt = System.currentTimeMillis(),
                isFavorite = existingIsFavorite
            )
            
            if (ruleId != null) {
                ruleDao.updateRule(entity)
            } else {
                ruleDao.insertRule(entity)
            }
            onSaved()
        }
    }
}
