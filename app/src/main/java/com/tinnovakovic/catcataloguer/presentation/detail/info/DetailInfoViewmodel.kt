package com.tinnovakovic.catcataloguer.presentation.detail.info

import androidx.annotation.MainThread
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.tinnovakovic.catcataloguer.data.CatBreedIdInMemoryCache
import com.tinnovakovic.catcataloguer.data.CatRepo
import com.tinnovakovic.catcataloguer.shared.NavDirection
import com.tinnovakovic.catcataloguer.shared.NavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailInfoViewModel @Inject constructor(
    private val navManager: NavManager,
    private val catRepo: CatRepo,
    private val catBreedIdInMemoryCache: CatBreedIdInMemoryCache,
) : DetailInfoContract.ViewModel() {

    private var initialiseCalled = false
    override val _uiState: MutableStateFlow<DetailInfoContract.UiState> =
        MutableStateFlow(initialUiState())

    @MainThread
    private fun initialise() {
        if (initialiseCalled) return
        initialiseCalled = true

        getCatDetail(catBreedIdInMemoryCache.cache.value)
    }

    override fun onUiEvent(event: DetailInfoContract.UiEvents) {
        when (event) {
            is DetailInfoContract.UiEvents.Initialise -> initialise()
            is DetailInfoContract.UiEvents.UpButtonClicked -> {
                navManager.navigate(direction = NavDirection.homeScreen)
            }
        }
    }

    private fun getCatDetail(catBreedId: String) {
        viewModelScope.launch {
            val catDetail = catRepo.getCatDetail(catBreedId)
            updateUiState { it.copy(catDetail = catDetail) }
        }
    }

    private fun initialUiState() = DetailInfoContract.UiState(
        catDetail = null
    )
}
