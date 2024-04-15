package com.tinnovakovic.catcataloguer.presentation.home

import androidx.annotation.MainThread
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tinnovakovic.catcataloguer.data.CatRepo
import com.tinnovakovic.catcataloguer.data.UserPreferencesRepo
import com.tinnovakovic.catcataloguer.data.mediator.BreedSortOrder
import com.tinnovakovic.catcataloguer.data.mediator.BreedSortOrder.Breed
import com.tinnovakovic.catcataloguer.data.mediator.BreedSortOrder.Origin
import com.tinnovakovic.catcataloguer.data.models.local.CatBreed
import com.tinnovakovic.catcataloguer.shared.NavDirection
import com.tinnovakovic.catcataloguer.shared.NavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navManager: NavManager,
    private val catRepo: CatRepo,
    private val userPreferencesRepo: UserPreferencesRepo,
) : HomeContract.ViewModel() {

    private var initialiseCalled = false
    override val _uiState: MutableStateFlow<HomeContract.UiState> =
        MutableStateFlow(initialUiState())

    @MainThread
    private fun initialise() {
        if (initialiseCalled) return
        initialiseCalled = true

        getUserPrefBreedSortOder()
    }

    private fun getUserPrefBreedSortOder() {
        viewModelScope.launch {
            val breedSortOrder =
                if (userPreferencesRepo.userPreferences().sortBreedsByName) Breed else Origin
            observeCatPager(breedSortOrder)
        }
    }

    override fun onUiEvent(event: HomeContract.UiEvents) {
        when (event) {
            HomeContract.UiEvents.Initialise -> initialise()

            is HomeContract.UiEvents.CatBreedClicked -> {
                navManager.navigate(direction = NavDirection.catBreedDetailScreen(event.catBreedId, event.catBreedName))
            }

            is HomeContract.UiEvents.FilterOptionClicked -> {
                viewModelScope.launch {
                    when (event.sortOrder) {
                        is Breed -> userPreferencesRepo.updateBreedSortOrder(true)
                        is Origin -> userPreferencesRepo.updateBreedSortOrder(false)
                    }
                    observeCatPager(event.sortOrder)
                }
            }
        }
    }

    private fun observeCatPager(sortOrder: BreedSortOrder) {
        val catBreedPagingFlow: Flow<PagingData<CatBreed>> =
            catRepo
                .observeCatBreedPager(sortOrder = sortOrder)
                .cachedIn(viewModelScope)

        val sortOrderForFilter = when (sortOrder) {
            is Origin -> Breed
            is Breed -> Origin
        }

        updateUiState { it.copy(cats = catBreedPagingFlow, sortOrder = sortOrderForFilter) }
    }

    companion object {
        fun initialUiState() = HomeContract.UiState(
            cats = emptyFlow(),
            sortOrder = Origin,
        )
    }
}
