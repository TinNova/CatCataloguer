package com.tinnovakovic.catcataloguer.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tinnovakovic.catcataloguer.data.TheCatApi
import com.tinnovakovic.catcataloguer.shared.NavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navManager: NavManager,
    private val theCatApi: TheCatApi
) : HomeContract.ViewModel() {


    override val _uiState: MutableStateFlow<HomeContract.UiState> =
        MutableStateFlow(initialUiState())

    init {
        viewModelScope.launch {
            val cats = theCatApi.getCatBreedDtos()
            val images = theCatApi.getCatImageDtos(page = 0, breedId = "abys")
            Log.d(javaClass.name, "TINTIN, Cats: ${cats.getOrNull()?.first()?.name}")
            Log.d(javaClass.name, "TINTIN, Image Url: ${images.getOrNull()?.first()?.url}")
            Log.d(javaClass.name, "TINTIN, Image Cat Id: ${images.getOrNull()?.first()?.breedDto?.first()?.id}")
            Log.d(javaClass.name, "TINTIN, Image Cat name: ${images.getOrNull()?.first()?.breedDto?.first()?.name}")

        }
    }

    override fun onUiEvent(event: HomeContract.UiEvents) {
        TODO("Not yet implemented")
    }

    companion object {
        fun initialUiState() = HomeContract.UiState(
            x = ""
        )
    }
}