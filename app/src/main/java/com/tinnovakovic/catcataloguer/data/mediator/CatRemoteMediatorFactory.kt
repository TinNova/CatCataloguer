package com.tinnovakovic.catcataloguer.data.mediator

import com.tinnovakovic.catcataloguer.data.TheCatApi
import com.tinnovakovic.catcataloguer.data.db.CatDatabase
import javax.inject.Inject

interface CatRemoteMediatorFactory {
    fun create(order: BreedSortOrder): CatRemoteMediator
}

class CatRemoteMediatorFactoryImpl @Inject constructor(
    private val catDatabase: CatDatabase,
    private val catApi: TheCatApi
) : CatRemoteMediatorFactory {
    override fun create(order: BreedSortOrder): CatRemoteMediator {
        return CatRemoteMediator(catDatabase, catApi)
    }
}

sealed class BreedSortOrder {
    data object Name: BreedSortOrder()
    data object Origin: BreedSortOrder()
}
