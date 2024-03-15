package com.tinnovakovic.catcataloguer.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.map
import com.tinnovakovic.catcataloguer.data.TheCatApi
import com.tinnovakovic.catcataloguer.data.models.db.CatEntity
import com.tinnovakovic.catcataloguer.data.models.local.CatBreed
import com.tinnovakovic.catcataloguer.data.models.toCatBreed
import com.tinnovakovic.catcataloguer.shared.NavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navManager: NavManager,
    private val theCatApi: TheCatApi,
    pager: Pager<Int, CatEntity> // move this to data layer if possible, worse case scenario to domain layer
) : HomeContract.ViewModel() {

    override val _uiState: MutableStateFlow<HomeContract.UiState> =
        MutableStateFlow(initialUiState())

    init {
        val catPagingFlow: Flow<PagingData<CatBreed>> = pager
            .flow
            .map { pagingData ->
                pagingData.map { it.toCatBreed() }
            }
            .cachedIn(viewModelScope)

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
