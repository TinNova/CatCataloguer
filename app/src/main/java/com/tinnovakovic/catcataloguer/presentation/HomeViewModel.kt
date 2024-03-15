package com.tinnovakovic.catcataloguer.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tinnovakovic.catcataloguer.data.PagerRepo
import com.tinnovakovic.catcataloguer.data.models.local.CatBreed
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
        val catPagingFlow: Flow<PagingData<CatBreed>> =
            pagerRepo.observeCatPager().cachedIn(viewModelScope)

        updateUiState { it.copy(cats = catPagingFlow) }

    }


    override fun onUiEvent(event: HomeContract.UiEvents) {
        TODO("Not yet implemented")
    }

    companion object {
        fun initialUiState() = HomeContract.UiState(
            cats = null
        )
    }
}
