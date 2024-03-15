package com.tinnovakovic.catcataloguer.presentation

import com.tinnovakovic.catcataloguer.shared.NavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navManager: NavManager,
) : HomeContract.ViewModel() {


    override val _uiState: MutableStateFlow<HomeContract.UiState> =
        MutableStateFlow(initialUiState())

    override fun onUiEvent(event: HomeContract.UiEvents) {
        TODO("Not yet implemented")
    }

    companion object {
        fun initialUiState() = HomeContract.UiState(
            x = ""
        )
    }
}