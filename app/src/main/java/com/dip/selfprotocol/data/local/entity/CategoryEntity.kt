package com.dip.selfprotocol.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dip.selfprotocol.domain.model.CategoryType
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: CategoryType,
    val isDefault: Boolean = false
)
