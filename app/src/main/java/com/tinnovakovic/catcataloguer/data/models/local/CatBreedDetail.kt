package com.tinnovakovic.catcataloguer.data.models.local

import com.google.gson.annotations.SerializedName

data class CatBreedDetail(
    val id: String,
    val name: String,
    val temperament: String,
    val origin: String,
    val description: String,
    @SerializedName("life_span")
    val lifeSpan: String,
    val indoor: Int,
    val adaptability: Int,
    @SerializedName("affection_level")
    val affectionLevel: Int,
    @SerializedName("child_friendly")
    val childFriendly: Int,
    @SerializedName("dog_friendly")
    val dogFriendly: Int,
    @SerializedName("energy_level")
    val energyLevel: Int,
    val grooming: Int,
    @SerializedName("health_issues")
    val healthIssues: Int,
    val intelligence: Int,
    @SerializedName("shedding_level")
    val sheddingLevel: Int,
    @SerializedName("social_needs")
    val socialNeeds: Int,
    @SerializedName("stranger_friendly")
    val strangerFriendly: Int,
    val vocalisation: Int,
    val experimental: Int,
    val hairless: Int,
    val natural: Int,
    val rare: Int,
    val rex: Int, //-> What is rex?
    @SerializedName("suppressed_tail")
    val suppressedTail: Int,
    @SerializedName("short_legs")
    val shortLegs: Int,
    val hypoallergenic: Int,
//    val images: List<String>
)