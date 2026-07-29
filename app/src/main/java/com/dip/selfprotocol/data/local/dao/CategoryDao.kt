package com.dip.selfprotocol.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dip.selfprotocol.data.local.entity.CategoryEntity
import com.dip.selfprotocol.domain.model.CategoryType
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("""
        SELECT c.*, COUNT(r.id) as count 
        FROM categories c 
        LEFT JOIN rules r ON c.id = r.categoryId 
        WHERE c.type = :type 
        GROUP BY c.id
    """)
    fun getRuleCategoriesWithCount(type: CategoryType = CategoryType.RULE): Flow<List<CategoryWithCount>>

    @Query("""
        SELECT c.*, COUNT(l.id) as count 
        FROM categories c 
        LEFT JOIN lessons l ON c.id = l.categoryId 
        WHERE c.type = :type 
        GROUP BY c.id
    """)
    fun getLessonCategoriesWithCount(type: CategoryType = CategoryType.LESSON): Flow<List<CategoryWithCount>>

    @Query("SELECT * FROM categories WHERE name LIKE '%' || :query || '%' AND type = :type")
    fun searchCategories(query: String, type: CategoryType): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)
    
    @Delete
    suspend fun deleteCategories(categories: List<CategoryEntity>)
    
    @Query("SELECT * FROM categories")
    suspend fun getAllCategoriesSync(): List<CategoryEntity>
    
    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()
}
