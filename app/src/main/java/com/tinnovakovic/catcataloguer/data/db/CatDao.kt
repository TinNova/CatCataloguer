package com.tinnovakovic.catcataloguer.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Upsert
import androidx.sqlite.db.SupportSQLiteQuery
import com.tinnovakovic.catcataloguer.data.models.db.CatBreedEntity
import com.tinnovakovic.catcataloguer.data.models.db.CatBreedImageEntity

@Dao
interface CatDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCatBreedImages(catImageEntities: List<CatBreedImageEntity>)

    @Upsert
    suspend fun upsertAll(catEntities: List<CatBreedEntity>)

    @RawQuery(observedEntities = [CatBreedEntity::class])
    fun catBreedPagingSource(query: SupportSQLiteQuery): PagingSource<Int, CatBreedEntity>

    @Transaction
    @Query("SELECT * FROM cat_breed_image_table WHERE catId = :catId")
    fun getCatBreedImagesPagingSourceByBreedId(catId: String): PagingSource<Int, CatBreedImageEntity>

    @Query("SELECT * FROM cat_breed_table WHERE id = :catId")
    suspend fun getCatBreedEntity(catId: String): CatBreedEntity
}