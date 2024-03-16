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
import com.tinnovakovic.catcataloguer.data.models.db.CatEntity
import com.tinnovakovic.catcataloguer.data.models.db.CatImageEntity
import com.tinnovakovic.catcataloguer.data.models.db.CatWithImages
import kotlinx.coroutines.flow.Flow

@Dao
interface CatDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCatImages(catImageEntities: List<CatImageEntity>)

    @Upsert
    suspend fun upsertAll(catEntities: List<CatEntity>)

    @RawQuery(observedEntities = [CatEntity::class])
    fun catPagingSource(query: SupportSQLiteQuery): PagingSource<Int, CatEntity>

    @Transaction
    @Query("SELECT * FROM cat_table WHERE id = :catId")
    fun getCatWithImages(catId: String): Flow<CatWithImages>

    @Transaction
    @Query("SELECT * FROM cat_image_table WHERE catId = :catId")
    fun getCatImagesPagingSourceByBreedId(catId: String): PagingSource<Int, CatImageEntity>
}