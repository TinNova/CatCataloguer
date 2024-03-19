package com.tinnovakovic.catcataloguer.data

import com.tinnovakovic.catcataloguer.shared.InMemoryCache
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatBreedIdInMemoryCache @Inject constructor() : InMemoryCache<String> {

    private val _cache = MutableStateFlow<String>("")
    override val cache: StateFlow<String> = _cache.asStateFlow()

    override suspend fun updateCache(newData: String) {
        _cache.update { newData }
    }
}