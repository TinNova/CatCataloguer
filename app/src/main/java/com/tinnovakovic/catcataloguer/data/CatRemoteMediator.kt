package com.tinnovakovic.catcataloguer.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.tinnovakovic.catcataloguer.data.db.CatDatabase
import com.tinnovakovic.catcataloguer.data.models.api.CatBreedDto
import com.tinnovakovic.catcataloguer.data.models.db.CatEntity
import com.tinnovakovic.catcataloguer.data.models.toCatEntity
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CatRemoteMediator @Inject constructor(
    val catDatabase: CatDatabase,
    val catApi: TheCatApi,
) : RemoteMediator<Int, CatEntity>() {

    private var page = 0

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CatEntity>
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
            val cats: List<CatBreedDto> = catApi.getCatBreedDtos(
                page = loadKey
            )

            catDatabase.withTransaction {
                val catEntities = cats.map { it.toCatEntity() }
                catDatabase.catDao().upsertAll(catEntities)
            }

            MediatorResult.Success(
                endOfPaginationReached = cats.isEmpty()
            )

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

}
