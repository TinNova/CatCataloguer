package com.tinnovakovic.catcataloguer.data.models.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cat_image_table",
    foreignKeys = [
        ForeignKey(
            entity = CatEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("catId"),
            onDelete = ForeignKey.CASCADE // when parent is deleted this child is deleted
        )
    ],
    indices = [Index(value = ["catId"])]
)
data class CatImageEntity(
    @PrimaryKey val imageId: String,
    val url: String,
    val catId: String, // This is a foreign key that references the Cat's ID
    val width: Int,
    val height: Int
)
