package com.tinnovakovic.catcataloguer.data.mediator

import com.tinnovakovic.catcataloguer.data.api.TheCatApi
import com.tinnovakovic.catcataloguer.data.db.CatDatabase
import javax.inject.Inject

interface CatImageRemoteMediatorFactory {
    fun create(catId: String): CatBreedImageRemoteMediator
}

class CatImageRemoteMediatorFactoryImpl @Inject constructor(
    private val catDatabase: CatDatabase,
    private val catApi: TheCatApi
) : CatImageRemoteMediatorFactory {
    override fun create(catId: String): CatBreedImageRemoteMediator {
        return CatBreedImageRemoteMediator(catDatabase, catApi, catId)
    }
}
