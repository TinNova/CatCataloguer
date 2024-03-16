package com.tinnovakovic.catcataloguer.presentation.detail

import androidx.annotation.MainThread
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tinnovakovic.catcataloguer.data.CatRepo
import com.tinnovakovic.catcataloguer.data.models.local.CatImage
import com.tinnovakovic.catcataloguer.shared.NavDirection
import com.tinnovakovic.catcataloguer.shared.NavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val navManager: NavManager,
    private val catRepo: CatRepo,
    savedStateHandle: SavedStateHandle,
) : DetailContract.ViewModel() {

    private var initialiseCalled = false
    private val catBreedId: String = checkNotNull(savedStateHandle["catBreedId"])
    override val _uiState: MutableStateFlow<DetailContract.UiState> =
        MutableStateFlow(initialUiState())

    @MainThread
    private fun initialise() {
        if (initialiseCalled) return
        initialiseCalled = true
        observeCatImagePager()
        getCatDetail()
    }

    override fun onUiEvent(event: DetailContract.UiEvents) {
        when (event) {
            is DetailContract.UiEvents.Initialise -> initialise()
            DetailContract.UiEvents.UpButtonClicked -> {
                navManager.navigate(direction = NavDirection.homeScreen)
            }
        }
    }

    private fun observeCatImagePager() {
        val catImagePagingFlow: Flow<PagingData<CatImage>> =
            catRepo.observeCatImagePager(catBreedId).cachedIn(viewModelScope)

        updateUiState { it.copy(images = catImagePagingFlow) }
    }

    private fun getCatDetail() {
        viewModelScope.launch {
            val catDetail = catRepo.getCatDetail(catBreedId)
            updateUiState { it.copy(catDetail = catDetail) }
        }
    }

    companion object {
        fun initialUiState() = DetailContract.UiState(
            images = null,
            catDetail = null
        )
    }
}
