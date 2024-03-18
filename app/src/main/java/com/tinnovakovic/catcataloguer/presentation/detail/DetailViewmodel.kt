package com.tinnovakovic.catcataloguer.presentation.detail

import androidx.annotation.MainThread
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tinnovakovic.catcataloguer.data.CatRepo
import com.tinnovakovic.catcataloguer.data.models.local.CatImage
import com.tinnovakovic.catcataloguer.presentation.detail.DetailContract.*
import com.tinnovakovic.catcataloguer.presentation.detail.DetailContract.UiState.Page
import com.tinnovakovic.catcataloguer.shared.NavDirection
import com.tinnovakovic.catcataloguer.shared.NavManager
import com.tinnovakovic.catcataloguer.shared.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val navManager: NavManager,
    private val catRepo: CatRepo,
    private val resourceProvider: ResourceProvider,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var initialiseCalled = false
    private val catBreedId: String = checkNotNull(savedStateHandle["catBreedId"])
    override val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(initialUiState())

    @MainThread
    private fun initialise() {
        if (initialiseCalled) return
        initialiseCalled = true
        observeCatImagePager()
        getCatDetail()
    }

    override fun onUiEvent(event: UiEvents) {
        when (event) {
            is UiEvents.Initialise -> initialise()
            is UiEvents.UpButtonClicked -> {
                navManager.navigate(direction = NavDirection.homeScreen)
            }

            is UiEvents.OnPageSelected -> {
                updateUiState { it.copy(currentPage = event.selectedPage) }
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

    private fun initialUiState() = UiState(
        images = null,
        catDetail = null,
        initialPage = Page.Info,
        currentPage = Page.Info,
        tabRowTitles = Page.values().map { resourceProvider.getString(it.stringRes) }
    )
}
