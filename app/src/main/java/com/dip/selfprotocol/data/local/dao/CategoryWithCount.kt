package com.dip.selfprotocol.data.local.dao

import androidx.room.Embedded
import com.dip.selfprotocol.data.local.entity.CategoryEntity

data class CategoryWithCount(
    @Embedded val category: CategoryEntity,
    val count: Int
)
