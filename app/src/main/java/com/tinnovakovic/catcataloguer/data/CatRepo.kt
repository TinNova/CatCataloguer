package com.tinnovakovic.catcataloguer.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.sqlite.db.SimpleSQLiteQuery
import com.tinnovakovic.catcataloguer.data.db.CatDao
import com.tinnovakovic.catcataloguer.data.mediator.BreedSortOrder
import com.tinnovakovic.catcataloguer.data.mediator.CatImageRemoteMediatorFactory
import com.tinnovakovic.catcataloguer.data.mediator.CatRemoteMediatorFactory
import com.tinnovakovic.catcataloguer.data.models.db.CatEntity
import com.tinnovakovic.catcataloguer.data.models.db.CatImageEntity
import com.tinnovakovic.catcataloguer.data.models.local.Cat
import com.tinnovakovic.catcataloguer.data.models.local.CatDetail
import com.tinnovakovic.catcataloguer.data.models.local.CatImage
import com.tinnovakovic.catcataloguer.data.models.toCat
import com.tinnovakovic.catcataloguer.data.models.toCatDetail
import com.tinnovakovic.catcataloguer.data.models.toCatImage
import com.tinnovakovic.catcataloguer.shared.IMAGE_PAGE_SIZE
import com.tinnovakovic.catcataloguer.shared.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CatRepo @Inject constructor(
    private val catImageMediatorFactory: CatImageRemoteMediatorFactory,
    private val catMediatorFactory: CatRemoteMediatorFactory,
    private val catDao: CatDao
) {

    suspend fun getCatDetail(catId: String): CatDetail {
        return catDao.getCatEntity(catId).toCatDetail()
    }

    fun observeCatPager(sortOrder: BreedSortOrder): Flow<PagingData<Cat>> {
        return createCatPager(sortOrder)
            .flow
            .map { pagingData: PagingData<CatEntity> ->
                pagingData.map { it.toCat() }
            }
    }

    fun observeCatImagePager(catId: String): Flow<PagingData<CatImage>> {
        return createCatImagePager(catId)
            .flow
            .map { pagingData: PagingData<CatImageEntity> ->
                pagingData.map { it.toCatImage() }
            }
    }

    private fun createCatPager(sortOrder: BreedSortOrder): Pager<Int, CatEntity> {
        val queryString = when (sortOrder) {
            is BreedSortOrder.Name -> SQL_CAT_TABLE_NAME_QUERY
            is BreedSortOrder.Origin -> SQL_CAT_TABLE_ORIGIN_QUERY
        }
        val sqlLiteQuery = SimpleSQLiteQuery(queryString)

        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            remoteMediator = catMediatorFactory.create(sortOrder),
            pagingSourceFactory = { catDao.catPagingSource(sqlLiteQuery) }
        )
    }

    private fun createCatImagePager(catId: String): Pager<Int, CatImageEntity> {
        return Pager(
            config = PagingConfig(pageSize = IMAGE_PAGE_SIZE),
            remoteMediator = catImageMediatorFactory.create(catId),
            pagingSourceFactory = { catDao.getCatImagesPagingSourceByBreedId(catId) }
        )
    }

    companion object {
        const val SQL_CAT_TABLE_NAME_QUERY = "SELECT * FROM cat_table ORDER BY name ASC"
        const val SQL_CAT_TABLE_ORIGIN_QUERY = "SELECT * FROM cat_table ORDER BY origin ASC"

    }
}
