package com.tinnovakovic.catcataloguer.data

import com.tinnovakovic.catcataloguer.BuildConfig
import com.tinnovakovic.catcataloguer.data.models.api.CatBreedDto
import com.tinnovakovic.catcataloguer.data.models.api.CatImageDto
import retrofit2.http.GET
import retrofit2.http.Query

interface TheCatApi {

    @GET("breeds?api_key=${BuildConfig.THE_CAT_API_API_KEY}")
    suspend fun getCatBreedDtos(
    ): Result<List<CatBreedDto>>

    @GET("images/search?api_key=${BuildConfig.THE_CAT_API_API_KEY}")
    suspend fun getCatImageDtos(
        @Query("limit") limit: Int = 10,
        @Query("DESC") order: String = IMAGE_ORDER,
        @Query("page") page: Int,
        @Query("breed_ids") breedId: String,
    ): Result<List<CatImageDto>>

}

const val IMAGE_ORDER = "DESC"
