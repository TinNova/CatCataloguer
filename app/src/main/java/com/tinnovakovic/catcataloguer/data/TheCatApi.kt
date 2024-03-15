package com.tinnovakovic.catcataloguer.data

import com.tinnovakovic.catcataloguer.BuildConfig
import com.tinnovakovic.catcataloguer.data.models.api.CatBreedDto
import com.tinnovakovic.catcataloguer.data.models.api.CatImageDto
import com.tinnovakovic.catcataloguer.shared.PAGE_SIZE
import retrofit2.http.GET
import retrofit2.http.Query

interface TheCatApi {

    @GET("breeds?api_key=${BuildConfig.THE_CAT_API_API_KEY}")
    suspend fun getCatBreedDtos(
        @Query("limit") limit: Int = PAGE_SIZE,
        @Query("page") page: Int,
        ): List<CatBreedDto>

    @GET("images/search?api_key=${BuildConfig.THE_CAT_API_API_KEY}")
    suspend fun getCatImageDtos(
        @Query("limit") limit: Int = 10,
        @Query("DESC") order: String = ORDER_FILTER,
        @Query("page") page: Int,
        @Query("breed_ids") breedId: String,
    ): Result<List<CatImageDto>>

}

const val ORDER_FILTER = "DESC"
