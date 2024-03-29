package com.tinnovakovic.catcataloguer.presentation.detail

import com.tinnovakovic.catcataloguer.shared.mvi.BaseUiEvent
import com.tinnovakovic.catcataloguer.shared.mvi.BaseUiState
import com.tinnovakovic.catcataloguer.shared.mvi.BaseViewModel
import javax.annotation.concurrent.Immutable

interface DetailContract {

    abstract class ViewModel : BaseViewModel<UiEvents, UiState>()

    @Immutable
    data class UiState(
        val catBreedName: String?
    ) : BaseUiState


    sealed class UiEvents : BaseUiEvent {
        data object Initialise : UiEvents()
        data object UpButtonClicked : UiEvents()
    }
}
