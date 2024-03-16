package com.tinnovakovic.catcataloguer.presentation.home

import androidx.paging.PagingData
import com.tinnovakovic.catcataloguer.data.models.local.Cat
import com.tinnovakovic.catcataloguer.shared.mvi.BaseUiEvent
import com.tinnovakovic.catcataloguer.shared.mvi.BaseUiState
import com.tinnovakovic.catcataloguer.shared.mvi.BaseViewModel
import kotlinx.coroutines.flow.Flow
import javax.annotation.concurrent.Immutable

interface HomeContract {

    abstract class ViewModel : BaseViewModel<UiEvents, UiState>()

    @Immutable
    data class UiState(
        val cats: Flow<PagingData<Cat>>?
    ) : BaseUiState {}

    sealed class UiEvents : BaseUiEvent {
        data class CatBreedClicked(val catBreedId: String) : UiEvents()
    }
}
