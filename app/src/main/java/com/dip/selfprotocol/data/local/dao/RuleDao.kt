package com.dip.selfprotocol.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dip.selfprotocol.data.local.entity.RuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RuleDao {
    @Query("SELECT * FROM rules WHERE categoryId = :categoryId ORDER BY id DESC")
    fun getRulesByCategory(categoryId: Int): Flow<List<RuleEntity>>

    @Query("SELECT * FROM rules WHERE question LIKE '%' || :query || '%' OR brutalAnswer LIKE '%' || :query || '%' OR rule LIKE '%' || :query || '%' ORDER BY id DESC")
    fun searchRules(query: String): Flow<List<RuleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRule(rule: RuleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRules(rules: List<RuleEntity>)

    @Update
    suspend fun updateRule(rule: RuleEntity)

    @Delete
    suspend fun deleteRule(rule: RuleEntity)

    @Delete
    suspend fun deleteRules(rules: List<RuleEntity>)
    
    @Query("SELECT * FROM rules")
    suspend fun getAllRulesSync(): List<RuleEntity>
    
    @Query("DELETE FROM rules")
    suspend fun deleteAllRules()
}
