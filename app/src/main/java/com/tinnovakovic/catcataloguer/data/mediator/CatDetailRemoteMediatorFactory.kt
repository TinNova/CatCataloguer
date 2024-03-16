package com.tinnovakovic.catcataloguer.data.mediator

import com.tinnovakovic.catcataloguer.data.CatImageRemoteMediator
import com.tinnovakovic.catcataloguer.data.TheCatApi
import com.tinnovakovic.catcataloguer.data.db.CatDatabase
import javax.inject.Inject

interface CatImageRemoteMediatorFactory {
    fun create(catId: String): CatImageRemoteMediator
}

class CatImageRemoteMediatorFactoryImpl @Inject constructor(
    private val catDatabase: CatDatabase,
    private val catApi: TheCatApi
) : CatImageRemoteMediatorFactory {
    override fun create(catId: String): CatImageRemoteMediator {
        return CatImageRemoteMediator(catDatabase, catApi, catId)
    }
}
