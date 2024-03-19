package com.tinnovakovic.catcataloguer.presentation.detail.info

import com.tinnovakovic.catcataloguer.data.models.local.CatDetail
import com.tinnovakovic.catcataloguer.shared.mvi.BaseUiEvent
import com.tinnovakovic.catcataloguer.shared.mvi.BaseUiState
import com.tinnovakovic.catcataloguer.shared.mvi.BaseViewModel
import javax.annotation.concurrent.Immutable

interface DetailInfoContract {

    abstract class ViewModel : BaseViewModel<UiEvents, UiState>()

    @Immutable
    data class UiState(
        val catDetail: CatDetail?
    ) : BaseUiState {}


    sealed class UiEvents : BaseUiEvent {
        data object Initialise : UiEvents()
        data object UpButtonClicked : UiEvents()
    }
}
