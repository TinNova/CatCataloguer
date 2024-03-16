package com.tinnovakovic.catcataloguer.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.tinnovakovic.catcataloguer.data.db.CatDatabase
import com.tinnovakovic.catcataloguer.data.models.api.CatImageDto
import com.tinnovakovic.catcataloguer.data.models.db.CatImageEntity
import com.tinnovakovic.catcataloguer.data.models.toCatImageEntity
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CatImageRemoteMediator @Inject constructor(
    private val catDatabase: CatDatabase,
    private val catApi: TheCatApi,
    private val catId: String,
) : RemoteMediator<Int, CatImageEntity>() {

    private var page = 0

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CatImageEntity>
    ): MediatorResult {
        return try {
            val loadKey: Int = when (loadType) {
                LoadType.REFRESH -> {
                    Log.d(javaClass.name, "TINTIN, LoadType is REFRESH")
                    0
                }

                LoadType.PREPEND -> {
                    Log.d(javaClass.name, "TINTIN, LoadType is PREPEND")
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        Log.d(javaClass.name, "TINTIN, LoadType is APPEND Last Item Null: $page")
                        page++ // No items, so fetch the first list
                    } else {
                        Log.d(javaClass.name, "TINTIN, LoadType is APPEND Tin Page: $page")
                        page++ //the next page to fetch, figure out this logic...
                    }
                }
            }

            Log.d(javaClass.name, "TINTIN, loadKey: $loadKey")
            val catImages: List<CatImageDto> = catApi.getCatImageDtos(
                breedId = catId,
                page = loadKey
            )

            catDatabase.withTransaction {
                val catImageEntities = catImages.map { catImageDto ->
                    catImageDto.toCatImageEntity(catId)
                }
                catDatabase.catDao().insertCatImages(catImageEntities)
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
