package com.tinnovakovic.catcataloguer.data.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.tinnovakovic.catcataloguer.data.api.TheCatApi
import com.tinnovakovic.catcataloguer.data.db.CatDatabase
import com.tinnovakovic.catcataloguer.data.models.api.CatBreedDto
import com.tinnovakovic.catcataloguer.data.models.db.CatBreedEntity
import com.tinnovakovic.catcataloguer.data.models.toCatEntity
import com.tinnovakovic.catcataloguer.shared.ErrorToUser
import com.tinnovakovic.catcataloguer.shared.ExceptionHandler
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CatBreedRemoteMediator @Inject constructor(
    private val catDatabase: CatDatabase,
    private val catApi: TheCatApi,
    private val exceptionHandler: ExceptionHandler,
) : RemoteMediator<Int, CatBreedEntity>() {

    private var page = 0

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CatBreedEntity>
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
                    page++ // No items, so fetch the first list
                }
            }

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
            Log.d(javaClass.name, "IOException")
            MediatorResult.Error(
                throwable = Throwable(
                    message = exceptionHandler.execute(e).message,
                    cause = null
                )
            )
        } catch (e: HttpException) {
            Log.d(javaClass.name, "HttpException")
            MediatorResult.Error(
                throwable = Throwable(
                    message = exceptionHandler.execute(e).message,
                    cause = null
                )
            )
        }
    }
}
