package com.tinnovakovic.catcataloguer.data.models.local

data class CatDetail(
    val id: String,
    val name: String,
    val altNames: String,
    val weightMetric: String,
    val weightImperial: String,
    val temperament: List<String>,
    val origin: String,
    val countryCode: String,
    val description: String,
    val lifeSpan: String,
    val personalityScores: List<PersonalityScore>,
    val features: List<Feature>,
)

data class PersonalityScore(
    val personality: String,
    val score: Int
)

data class Feature(
    val feature: String,
    val hasFeature: Boolean
)
