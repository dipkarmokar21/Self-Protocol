package com.dip.selfprotocol.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dip.selfprotocol.data.local.SelfProtocolDatabase
import com.dip.selfprotocol.data.local.dao.CategoryDao
import com.dip.selfprotocol.data.local.dao.LessonDao
import com.dip.selfprotocol.data.local.dao.RuleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SelfProtocolDatabase {
        val MIGRATION_1_2 = object : androidx.room.migration.Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Rules table migration
                db.execSQL("ALTER TABLE rules ADD COLUMN createdAt INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}")
                db.execSQL("ALTER TABLE rules ADD COLUMN lastEditedAt INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}")
                db.execSQL("ALTER TABLE rules ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0")

                // Lessons table migration
                db.execSQL("ALTER TABLE lessons ADD COLUMN createdAt INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}")
                db.execSQL("ALTER TABLE lessons ADD COLUMN lastEditedAt INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}")
                db.execSQL("ALTER TABLE lessons ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0")
            }
        }

        return Room.databaseBuilder(
            context,
            SelfProtocolDatabase::class.java,
            "self_protocol_db"
        ).addMigrations(MIGRATION_1_2)
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                val defaultCategories = listOf(
                    "Relationship", "Friendship", "Privacy", "Mindset", "Study",
                    "Career", "Money", "Health", "Sleep", "Food", "Tour",
                    "Family", "Religion", "Social Media", "Gaming", "Smoking",
                    "Alcohol", "Anger"
                )
                defaultCategories.forEach { categoryName ->
                    // Insert for Rules
                    db.execSQL("INSERT INTO categories (name, type, isDefault) VALUES ('$categoryName', 'RULE', 1)")
                    // Insert for Lessons
                    db.execSQL("INSERT INTO categories (name, type, isDefault) VALUES ('$categoryName', 'LESSON', 1)")
                }
            }
        }).build()
    }

    @Provides
    fun provideCategoryDao(db: SelfProtocolDatabase): CategoryDao = db.categoryDao

    @Provides
    fun provideRuleDao(db: SelfProtocolDatabase): RuleDao = db.ruleDao

    @Provides
    fun provideLessonDao(db: SelfProtocolDatabase): LessonDao = db.lessonDao
}
