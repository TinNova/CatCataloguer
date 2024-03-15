package com.tinnovakovic.catcataloguer.data

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.tinnovakovic.catcataloguer.data.models.db.CatEntity
import com.tinnovakovic.catcataloguer.data.models.local.CatBreed
import com.tinnovakovic.catcataloguer.data.models.toCatBreed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PagerRepo @Inject constructor(
    private val pager: Pager<Int, CatEntity>
) {

    fun observeCatPager(): Flow<PagingData<CatBreed>> {
        return pager
            .flow
            .map { pagingData ->
                pagingData.map { it.toCatBreed() }
            }
    }

}
