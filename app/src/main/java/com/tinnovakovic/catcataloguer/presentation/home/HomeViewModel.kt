package com.tinnovakovic.catcataloguer.presentation.home

import androidx.annotation.MainThread
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tinnovakovic.catcataloguer.data.CatRepo
import com.tinnovakovic.catcataloguer.data.mediator.BreedSortOrder
import com.tinnovakovic.catcataloguer.data.models.local.Cat
import com.tinnovakovic.catcataloguer.shared.NavDirection
import com.tinnovakovic.catcataloguer.shared.NavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navManager: NavManager,
    private val catRepo: CatRepo,
) : HomeContract.ViewModel() {

    private var initialiseCalled = false
    override val _uiState: MutableStateFlow<HomeContract.UiState> =
        MutableStateFlow(initialUiState())

    @MainThread
    private fun initialise() {
        if (initialiseCalled) return
        initialiseCalled = true
        observeCatPager(BreedSortOrder.Name)
    }

    override fun onUiEvent(event: HomeContract.UiEvents) {
        when (event) {
            HomeContract.UiEvents.Initialise -> initialise()

            is HomeContract.UiEvents.CatBreedClicked -> {
                navManager.navigate(direction = NavDirection.catBreedDetailScreen(event.catBreedId))
            }

            is HomeContract.UiEvents.FilterOptionClicked -> observeCatPager(event.sortOrder)
        }
    }

    private fun observeCatPager(sortOrder: BreedSortOrder) {
        val catPagingFlow: Flow<PagingData<Cat>> =
            catRepo
                .observeCatPager(sortOrder = sortOrder)
                .cachedIn(viewModelScope)

        val sortOrderForFilter = when (sortOrder) {
            is BreedSortOrder.Origin -> BreedSortOrder.Name
            is BreedSortOrder.Name -> BreedSortOrder.Origin
        }

        updateUiState { it.copy(cats = catPagingFlow, sortOrder = sortOrderForFilter) }
    }

    companion object {
        fun initialUiState() = HomeContract.UiState(
            cats = null,
            sortOrder = BreedSortOrder.Origin
        )
    }
}
