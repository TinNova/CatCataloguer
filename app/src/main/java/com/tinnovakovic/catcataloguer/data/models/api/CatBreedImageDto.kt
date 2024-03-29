package com.tinnovakovic.catcataloguer.data.models.api

data class CatBreedImageDto(
    val id: String,
    val url: String, //id is in the url anyway, and the url can be used to create a set so id not needed
    val width: Int,
    val height: Int
)
