package com.tinnovakovic.catcataloguer.presentation.detail

import androidx.annotation.MainThread
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.tinnovakovic.catcataloguer.data.CatBreedIdInMemoryCache
import com.tinnovakovic.catcataloguer.presentation.detail.DetailContract.*
import com.tinnovakovic.catcataloguer.shared.NavDirection
import com.tinnovakovic.catcataloguer.shared.NavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val navManager: NavManager,
    private val catBreedIdInMemoryCache: CatBreedIdInMemoryCache,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var initialiseCalled = false
    private val catBreedId: String = checkNotNull(savedStateHandle[CAT_BREED_ID])
    private val catBreedName: String = checkNotNull(savedStateHandle[CAT_BREED_NAME])
    override val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(initialUiState())

    @MainThread
    private fun initialise() {
        if (initialiseCalled) return
        initialiseCalled = true

        viewModelScope.launch {
            catBreedIdInMemoryCache.updateCache(catBreedId)
            updateUiState { it.copy(catBreedName = catBreedName) }
        }
    }

    override fun onUiEvent(event: UiEvents) {
        when (event) {
            is UiEvents.Initialise -> initialise()
            is UiEvents.UpButtonClicked -> {
                navManager.navigate(direction = NavDirection.homeScreen)
            }
        }
    }

    companion object {
        fun initialUiState() = UiState(
            catBreedName = null
        )

        const val CAT_BREED_ID = "catBreedId"
        const val CAT_BREED_NAME = "catBreedName"
    }
}
