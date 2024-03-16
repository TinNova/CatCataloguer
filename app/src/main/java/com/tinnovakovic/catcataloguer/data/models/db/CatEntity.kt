package com.tinnovakovic.catcataloguer.data.models.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Embedded
import androidx.room.Index
import androidx.room.Relation

@Entity(tableName = "cat_table")
data class CatEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val altNames: String,
    val temperament: String,
    val origin: String,
    val countryCode: String,
    val description: String,
    val lifeSpan: String,
    val indoor: Int,
    val lap: Int,
    val adaptability: Int,
    val affectionLevel: Int,
    val childFriendly: Int,
    val dogFriendly: Int,
    val energyLevel: Int,
    val grooming: Int,
    val healthIssues: Int,
    val intelligence: Int,
    val sheddingLevel: Int,
    val socialNeeds: Int,
    val strangerFriendly: Int,
    val vocalisation: Int,
    val experimental: Int,
    val hairless: Int,
    val natural: Int,
    val rare: Int,
    val rex: Int, //-> Refers to if the breed can have curly hair
    val suppressedTail: Int,
    val shortLegs: Int,
    val hypoallergenic: Int,
)

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
    val catId: String // This is a foreign key that references the Cat's ID
)

data class CatWithImages(
    @Embedded val cat: CatEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "catId"
    )
    val images: List<CatImageEntity>
)
