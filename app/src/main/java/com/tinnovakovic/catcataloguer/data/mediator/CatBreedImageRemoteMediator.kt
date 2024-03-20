package com.tinnovakovic.catcataloguer.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.tinnovakovic.catcataloguer.data.TheCatApi
import com.tinnovakovic.catcataloguer.data.db.CatDatabase
import com.tinnovakovic.catcataloguer.data.models.api.CatBreedImageDto
import com.tinnovakovic.catcataloguer.data.models.db.CatBreedImageEntity
import com.tinnovakovic.catcataloguer.data.models.toCatImageEntity
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CatBreedImageRemoteMediator @Inject constructor(
    private val catDatabase: CatDatabase,
    private val catApi: TheCatApi,
    private val catId: String,
) : RemoteMediator<Int, CatBreedImageEntity>() {

    private var page = 0

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CatBreedImageEntity>
    ): MediatorResult {
        return try {
            val loadKey: Int = when (loadType) {
                LoadType.REFRESH -> {
                    0
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        page++ // No items, so fetch the first list
                    } else {
                        page++ //the next page to fetch, figure out this logic...
                    }
                }
            }

            val catImages: List<CatBreedImageDto> = catApi.getCatImageDtos(
                breedId = catId,
                page = loadKey
            )

            catDatabase.withTransaction {
                val catImageEntities = catImages.map { catImageDto ->
                    catImageDto.toCatImageEntity(catId)
                }
                catDatabase.catDao().insertCatBreedImages(catImageEntities)
            }

            MediatorResult.Success(
                endOfPaginationReached = catImages.isEmpty()
            )

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

}
