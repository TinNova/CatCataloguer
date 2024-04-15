package com.tinnovakovic.catcataloguer.data.mediator

import com.tinnovakovic.catcataloguer.data.api.TheCatApi
import com.tinnovakovic.catcataloguer.data.db.CatDatabase
import com.tinnovakovic.catcataloguer.shared.ExceptionHandler
import javax.inject.Inject

interface CatRemoteMediatorFactory {
    fun create(order: BreedSortOrder): CatBreedRemoteMediator
}

class CatRemoteMediatorFactoryImpl @Inject constructor(
    private val catDatabase: CatDatabase,
    private val catApi: TheCatApi,
    private val exceptionHandler: ExceptionHandler,
    ) : CatRemoteMediatorFactory {
    override fun create(order: BreedSortOrder): CatBreedRemoteMediator {
        return CatBreedRemoteMediator(catDatabase, catApi, exceptionHandler)
    }
}

sealed class BreedSortOrder {
    data object Breed: BreedSortOrder()
    data object Origin: BreedSortOrder()
}
