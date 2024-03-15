package com.tinnovakovic.catcataloguer.presentation

import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import com.tinnovakovic.catcataloguer.data.models.local.CatBreed
import com.tinnovakovic.catcataloguer.shared.mvi.BaseUiEvent
import com.tinnovakovic.catcataloguer.shared.mvi.BaseUiState
import com.tinnovakovic.catcataloguer.shared.mvi.BaseViewModel
import kotlinx.coroutines.flow.Flow
import javax.annotation.concurrent.Immutable

interface HomeContract {

    abstract class ViewModel : BaseViewModel<UiEvents, UiState>()

    @Immutable
    data class UiState(
        val cats: Flow<PagingData<CatBreed>>?
    ) : BaseUiState {}

    sealed class UiEvents : BaseUiEvent {
//        data object Initialise : UiEvents()
//        data object ButtonClicked : UiEvents()
    }
}
