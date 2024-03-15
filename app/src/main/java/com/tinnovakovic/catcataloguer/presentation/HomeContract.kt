package com.tinnovakovic.catcataloguer.presentation

import com.tinnovakovic.catcataloguer.shared.mvi.BaseUiEvent
import com.tinnovakovic.catcataloguer.shared.mvi.BaseUiState
import com.tinnovakovic.catcataloguer.shared.mvi.BaseViewModel
import javax.annotation.concurrent.Immutable

interface HomeContract {

    abstract class ViewModel : BaseViewModel<UiEvents, UiState>()

    @Immutable
    data class UiState(
        val x: String
    ) : BaseUiState {}

    sealed class UiEvents : BaseUiEvent {
//        data object Initialise : UiEvents()
//        data object ButtonClicked : UiEvents()
    }
}
