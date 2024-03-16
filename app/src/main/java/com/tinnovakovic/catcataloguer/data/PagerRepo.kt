package com.tinnovakovic.catcataloguer.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.tinnovakovic.catcataloguer.data.db.CatDatabase
import com.tinnovakovic.catcataloguer.data.mediator.CatImageRemoteMediatorFactory
import com.tinnovakovic.catcataloguer.data.models.db.CatEntity
import com.tinnovakovic.catcataloguer.data.models.db.CatImageEntity
import com.tinnovakovic.catcataloguer.data.models.local.Cat
import com.tinnovakovic.catcataloguer.data.models.local.CatImage
import com.tinnovakovic.catcataloguer.data.models.toCat
import com.tinnovakovic.catcataloguer.data.models.toCatImage
import com.tinnovakovic.catcataloguer.shared.IMAGE_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PagerRepo @Inject constructor(
    private val catPager: Pager<Int, CatEntity>,
    private val catImageMediatorFactory: CatImageRemoteMediatorFactory,
    private val catDatabase: CatDatabase
) {

    fun observeCatPager(): Flow<PagingData<Cat>> {
        return catPager
            .flow
            .map { pagingData: PagingData<CatEntity> ->
                pagingData.map { it.toCat() }
            }
    }

    fun observeCatImagePager(catId: String): Flow<PagingData<CatImage>> {
        return createPager(catId)
            .flow
            .map {pagingData: PagingData<CatImageEntity> ->
                pagingData.map { it.toCatImage() }
            }
    }

    private fun createPager(catId: String): Pager<Int, CatImageEntity> {
        return Pager(
            config = PagingConfig(pageSize = IMAGE_PAGE_SIZE),
            remoteMediator = catImageMediatorFactory.create(catId),
            pagingSourceFactory = { catDatabase.catDao().getCatImagesPagingSourceByBreedId(catId) }
        )
    }
}
