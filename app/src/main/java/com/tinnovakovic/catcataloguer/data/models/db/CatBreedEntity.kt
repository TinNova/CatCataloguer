package com.tinnovakovic.catcataloguer.data.models.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cat_breed_table")
data class CatBreedEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val altNames: String,
    val weightMetric: String,
    val weightImperial: String,
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
