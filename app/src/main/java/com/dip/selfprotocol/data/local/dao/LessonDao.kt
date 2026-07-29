package com.dip.selfprotocol.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dip.selfprotocol.data.local.entity.LessonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonDao {
    @Query("SELECT * FROM lessons WHERE categoryId = :categoryId ORDER BY id DESC")
    fun getLessonsByCategory(categoryId: Int): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE question LIKE '%' || :query || '%' OR answer LIKE '%' || :query || '%' ORDER BY id DESC")
    fun searchLessons(query: String): Flow<List<LessonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lesson: LessonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonEntity>)

    @Update
    suspend fun updateLesson(lesson: LessonEntity)

    @Delete
    suspend fun deleteLesson(lesson: LessonEntity)

    @Delete
    suspend fun deleteLessons(lessons: List<LessonEntity>)
    
    @Query("SELECT * FROM lessons")
    suspend fun getAllLessonsSync(): List<LessonEntity>
    
    @Query("DELETE FROM lessons")
    suspend fun deleteAllLessons()
}
