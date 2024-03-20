package com.tinnovakovic.catcataloguer.presentation.detail.images

import androidx.annotation.MainThread
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tinnovakovic.catcataloguer.data.CatBreedIdInMemoryCache
import com.tinnovakovic.catcataloguer.data.CatRepo
import com.tinnovakovic.catcataloguer.data.models.local.CatImage
import com.tinnovakovic.catcataloguer.presentation.detail.images.DetailImagesContract.*
import com.tinnovakovic.catcataloguer.shared.ErrorToUser
import com.tinnovakovic.catcataloguer.shared.ExceptionHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

@HiltViewModel
class DetailImagesViewModel @Inject constructor(
    private val catRepo: CatRepo,
    private val exceptionHandler: ExceptionHandler,
    private val catBreedIdInMemoryCache: CatBreedIdInMemoryCache,
) : ViewModel() {

    private var initialiseCalled = false
    override val _uiState: MutableStateFlow<UiState> = MutableStateFlow(initialUiState())

    @MainThread
    private fun initialise() {
        if (initialiseCalled) return
        initialiseCalled = true

        observeCatImagePager(catBreedIdInMemoryCache.cache.value)
    }

    override fun onUiEvent(event: UiEvents) {
        when (event) {
            is UiEvents.Initialise -> initialise()
            is UiEvents.PagingError -> {
                if (!uiState.value.errorShown) {
                    val error: ErrorToUser = exceptionHandler.execute(event.error)

                    updateUiState {
                        it.copy(
                            displayError = error.message,
                            errorShown = true
                        )
                    }
                }
            }

            is UiEvents.ClearErrorMessage -> {
                updateUiState { it.copy(displayError = null) }
            }
        }
    }

    private fun observeCatImagePager(catBreedId: String) {
        val catImagePagingFlow: Flow<PagingData<CatImage>> =
            catRepo.observeCatImagePager(catBreedId).cachedIn(viewModelScope)

        updateUiState { it.copy(images = catImagePagingFlow) }
    }

    private fun initialUiState() = UiState(
        images = emptyFlow(),
        displayError = null,
        errorShown = false
    )
}
