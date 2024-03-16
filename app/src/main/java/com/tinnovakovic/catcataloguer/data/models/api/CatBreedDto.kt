package com.tinnovakovic.catcataloguer.data.models.api

import com.google.gson.annotations.SerializedName

data class CatBreedDto(
    val id: String,
    val name: String,
    @SerializedName("alt_names")
    val altNames: String?,
    val temperament: String,
    val weight: WeightDto,
    val origin: String,
    val lap: Int?,
    @SerializedName("country_code")
    val countryCode: String,
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
    val rex: Int,
    @SerializedName("suppressed_tail")
    val suppressedTail: Int,
    @SerializedName("short_legs")
    val shortLegs: Int,
    val hypoallergenic: Int,
)

data class WeightDto(
    val metric: String,
    val imperial: String
)
