package com.tinnovakovic.catcataloguer.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.tinnovakovic.catcataloguer.data.UserPreferencesRepo.PreferencesKeys.SORT_BREEDS_BY_NAME
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepo @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {

    suspend fun userPreferences(): UserPreferences = dataStore.data
        .map { preferences ->
            val sortBreedsByName = preferences[SORT_BREEDS_BY_NAME] ?: true
            UserPreferences(sortBreedsByName)
        }.first()

    suspend fun updateBreedSortOrder(sortBreedsByName: Boolean) {
        dataStore.edit { preferences ->
            preferences[SORT_BREEDS_BY_NAME] = sortBreedsByName
        }
    }

    private object PreferencesKeys {
        val SORT_BREEDS_BY_NAME = booleanPreferencesKey("SORT_BREEDS_BY_NAME")
    }
}

data class UserPreferences(val sortBreedsByName: Boolean)