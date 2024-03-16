package com.tinnovakovic.catcataloguer.presentation.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tinnovakovic.catcataloguer.data.PagerRepo
import com.tinnovakovic.catcataloguer.data.models.local.CatImage
import com.tinnovakovic.catcataloguer.shared.NavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val navManager: NavManager,
    private val pagerRepo: PagerRepo,
    savedStateHandle: SavedStateHandle,
) : DetailContract.ViewModel() {

    private val catBreedId: String = checkNotNull(savedStateHandle["catBreedId"])
    override val _uiState: MutableStateFlow<DetailContract.UiState> =
        MutableStateFlow(initialUiState())

    init {
        Log.d(javaClass.name, "TINTIN catBreedId: $catBreedId")
        val catImagePagingFlow: Flow<PagingData<CatImage>> =
            pagerRepo.observeCatImagePager(catBreedId).cachedIn(viewModelScope)

        updateUiState { it.copy(images = catImagePagingFlow) }
    }

    override fun onUiEvent(event: DetailContract.UiEvents) {
        TODO("Not yet implemented")
    }

    companion object {
        fun initialUiState() = DetailContract.UiState(
            images = null
        )
    }
}