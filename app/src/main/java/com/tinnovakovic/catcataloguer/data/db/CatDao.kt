package com.tinnovakovic.catcataloguer.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.tinnovakovic.catcataloguer.data.models.api.CatBreedDto
import com.tinnovakovic.catcataloguer.data.models.db.CatEntity

@Dao
interface CatDao {
    @Upsert
    suspend fun upsertAll(cats: List<CatEntity>) // Can this be used to insert images later??

    @Query("SELECT * FROM cat_table") //return decsending based on cat name
    fun catPagingSource(): PagingSource<Int, CatEntity>

}