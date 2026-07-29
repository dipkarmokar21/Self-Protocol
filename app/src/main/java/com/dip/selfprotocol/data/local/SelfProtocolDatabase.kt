package com.dip.selfprotocol.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dip.selfprotocol.data.local.dao.CategoryDao
import com.dip.selfprotocol.data.local.dao.LessonDao
import com.dip.selfprotocol.data.local.dao.RuleDao
import com.dip.selfprotocol.data.local.entity.CategoryEntity
import com.dip.selfprotocol.data.local.entity.LessonEntity
import com.dip.selfprotocol.data.local.entity.RuleEntity

@Database(
    entities = [CategoryEntity::class, RuleEntity::class, LessonEntity::class],
    version = 2,
    exportSchema = false
)
abstract class SelfProtocolDatabase : RoomDatabase() {
    abstract val categoryDao: CategoryDao
    abstract val ruleDao: RuleDao
    abstract val lessonDao: LessonDao
}
