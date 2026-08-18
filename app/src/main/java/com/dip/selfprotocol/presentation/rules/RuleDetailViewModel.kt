package com.dip.selfprotocol.presentation.rules

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dip.selfprotocol.data.local.dao.RuleDao
import com.dip.selfprotocol.data.local.entity.RuleEntity
import com.dip.selfprotocol.util.DraftManager
import com.dip.selfprotocol.util.RuleDraft
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RuleDetailViewModel @Inject constructor(
    private val ruleDao: RuleDao,
    private val draftManager: DraftManager,
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

    private val _hasDraft = MutableStateFlow(false)
    val hasDraft = _hasDraft.asStateFlow()

    private var existingCreatedAt: Long = System.currentTimeMillis()
    private var existingIsFavorite: Boolean = false
    
    private val draftKey: String
        get() = if (ruleId != null) "rule_$ruleId" else "new_$categoryId"

    init {
        val draft = draftManager.getRuleDraft(draftKey)
        if (draft != null) {
            _hasDraft.value = true
        }

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

    fun restoreDraft() {
        draftManager.getRuleDraft(draftKey)?.let { draft ->
            _question.value = draft.question
            _brutalAnswer.value = draft.brutalAnswer
            _rule.value = draft.rule
        }
        _hasDraft.value = false
    }

    fun discardDraft() {
        draftManager.clearRuleDraft(draftKey)
        _hasDraft.value = false
    }

    fun saveDraftIfNeeded() {
        // Save draft if any text exists
        if (_question.value.isNotBlank() || _brutalAnswer.value.isNotBlank() || _rule.value.isNotBlank()) {
            draftManager.saveRuleDraft(
                draftKey,
                RuleDraft(_question.value, _brutalAnswer.value, _rule.value)
            )
        }
    }

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
            draftManager.clearRuleDraft(draftKey)
            onSaved()
        }
    }
}
