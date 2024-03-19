package com.tinnovakovic.catcataloguer.presentation.detail.images

import androidx.paging.PagingData
import com.tinnovakovic.catcataloguer.data.models.local.CatImage
import com.tinnovakovic.catcataloguer.shared.mvi.BaseUiEvent
import com.tinnovakovic.catcataloguer.shared.mvi.BaseUiState
import com.tinnovakovic.catcataloguer.shared.mvi.BaseViewModel
import kotlinx.coroutines.flow.Flow
import javax.annotation.concurrent.Immutable

interface DetailImagesContract {

    abstract class ViewModel : BaseViewModel<UiEvents, UiState>()

    @Immutable
    data class UiState(
        val images: Flow<PagingData<CatImage>>?,
    ) : BaseUiState {}


    sealed class UiEvents : BaseUiEvent {
        data object Initialise : UiEvents()
        data class PagingError(val error: Throwable): UiEvents()
        data object ClearErrorMessage : UiEvents()
    }
}
