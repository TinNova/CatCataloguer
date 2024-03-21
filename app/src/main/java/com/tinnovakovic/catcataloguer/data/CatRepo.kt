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
import com.tinnovakovic.catcataloguer.data.models.db.CatBreedEntity
import com.tinnovakovic.catcataloguer.data.models.db.CatBreedImageEntity
import com.tinnovakovic.catcataloguer.data.models.local.CatBreed
import com.tinnovakovic.catcataloguer.data.models.local.CatDetail
import com.tinnovakovic.catcataloguer.data.models.local.CatBreedImage
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

    suspend fun getCatBreedDetail(catId: String): CatDetail {
        return catDao.getCatBreedEntity(catId).toCatDetail()
    }

    fun observeCatBreedPager(sortOrder: BreedSortOrder): Flow<PagingData<CatBreed>> {
        return createCatBreedPager(sortOrder)
            .flow
            .map { pagingData: PagingData<CatBreedEntity> ->
                pagingData.map { it.toCat() }
            }
    }

    fun observeCatBreedImagePager(catId: String): Flow<PagingData<CatBreedImage>> {
        return createCatBreedImagePager(catId)
            .flow
            .map { pagingData: PagingData<CatBreedImageEntity> ->
                pagingData.map { it.toCatImage() }
            }
    }

    private fun createCatBreedPager(sortOrder: BreedSortOrder): Pager<Int, CatBreedEntity> {
        val queryString = when (sortOrder) {
            is BreedSortOrder.Breed -> SQL_CAT_TABLE_NAME_QUERY
            is BreedSortOrder.Origin -> SQL_CAT_TABLE_ORIGIN_QUERY
        }
        val sqlLiteQuery = SimpleSQLiteQuery(queryString)

        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            remoteMediator = catMediatorFactory.create(sortOrder),
            pagingSourceFactory = { catDao.catBreedPagingSource(sqlLiteQuery) }
        )
    }

    private fun createCatBreedImagePager(catId: String): Pager<Int, CatBreedImageEntity> {
        return Pager(
            config = PagingConfig(pageSize = IMAGE_PAGE_SIZE),
            remoteMediator = catImageMediatorFactory.create(catId),
            pagingSourceFactory = { catDao.getCatBreedImagesPagingSourceByBreedId(catId) }
        )
    }

    companion object {
       private const val SQL_CAT_TABLE_NAME_QUERY = "SELECT * FROM cat_breed_table ORDER BY name ASC"
       private const val SQL_CAT_TABLE_ORIGIN_QUERY = "SELECT * FROM cat_breed_table ORDER BY origin ASC"

    }
}
