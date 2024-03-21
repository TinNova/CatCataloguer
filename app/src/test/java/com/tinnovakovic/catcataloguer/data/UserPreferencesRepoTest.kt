package com.tinnovakovic.catcataloguer.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.preferencesOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UserPreferencesRepoTest {

    private val dataStore: DataStore<Preferences> = mockk(relaxed = true)

    private val sut = UserPreferencesRepo(dataStore)

    @Test
    fun `GIVEN userPreferences set to false, WHEN userPreferences(), THEN returns sortBreedsByName false`() =
        runTest {
            // Given
            val sortBreedsByNameKey = booleanPreferencesKey("SORT_BREEDS_BY_NAME")
            val preferences: Preferences = preferencesOf(sortBreedsByNameKey to false)
            coEvery { dataStore.data } returns flowOf(preferences)
            val expected = UserPreferences(sortBreedsByName = false)

            // When
            val actual  = sut.userPreferences()

            // Then
            assertEquals(expected, actual)
        }

    @Test
    fun `GIVEN preference is unset, WHEN userPreferences(), THEN return sortBreedsByName true`() = runTest {
        // Given
        val preferences = preferencesOf()
        coEvery { dataStore.data } returns flowOf(preferences)
        val expected = UserPreferences(sortBreedsByName = true) // Default value is true

        // When
        val actual = sut.userPreferences()

        // Then
        assertEquals(expected, actual)
    }

}

