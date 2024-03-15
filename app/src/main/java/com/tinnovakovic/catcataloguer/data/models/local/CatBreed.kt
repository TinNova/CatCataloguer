package com.tinnovakovic.catcataloguer.data.models.local

import com.google.gson.annotations.SerializedName

data class CatBreed(
    val id: String,
    val name: String,
    val origin: String,
)