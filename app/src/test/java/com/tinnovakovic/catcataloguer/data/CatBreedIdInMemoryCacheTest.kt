package com.tinnovakovic.catcataloguer.data

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CatBreedIdInMemoryCacheTest {

    private val sut = CatBreedIdInMemoryCache()

    @Test
    fun `GIVEN initial cache value, WHEN cache, THEN return an empty string`() = runTest {
        // Given // When
        val initialValue = sut.cache.first()

        // Then
        assertEquals("", initialValue)
    }

    @Test
    fun `GIVEN new cache value, WHEN updateCache, THEN successfully updates the cache value`() = runTest {
        // Given
        val newCatBreedId = "abc123"

        // When
        sut.updateCache(newCatBreedId)

        // Then
        val updatedValue = sut.cache.first()
        assertEquals(newCatBreedId, updatedValue)
    }

}