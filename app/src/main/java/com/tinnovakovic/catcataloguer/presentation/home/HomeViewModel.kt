package com.tinnovakovic.catcataloguer.presentation.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tinnovakovic.catcataloguer.data.PagerRepo
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
    private val pagerRepo: PagerRepo,
) : HomeContract.ViewModel() {

    override val _uiState: MutableStateFlow<HomeContract.UiState> =
        MutableStateFlow(initialUiState())

    init {
        val catPagingFlow: Flow<PagingData<Cat>> =
            pagerRepo.observeCatPager().cachedIn(viewModelScope)

        updateUiState { it.copy(cats = catPagingFlow) }

    }


    override fun onUiEvent(event: HomeContract.UiEvents) {
        when (event) {
            is HomeContract.UiEvents.CatBreedClicked -> {
                navManager.navigate(direction = NavDirection.catBreedDetailScreen(event.catBreedId))
            }
        }
    }

    companion object {
        fun initialUiState() = HomeContract.UiState(
            cats = null
        )
    }
}
