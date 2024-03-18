package com.tinnovakovic.catcataloguer.presentation.detail

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.paging.PagingData
import com.tinnovakovic.catcataloguer.R
import com.tinnovakovic.catcataloguer.data.models.local.CatDetail
import com.tinnovakovic.catcataloguer.data.models.local.CatImage
import com.tinnovakovic.catcataloguer.shared.mvi.BaseUiEvent
import com.tinnovakovic.catcataloguer.shared.mvi.BaseUiState
import com.tinnovakovic.catcataloguer.shared.mvi.BaseViewModel
import kotlinx.coroutines.flow.Flow
import javax.annotation.concurrent.Immutable

interface DetailContract {

    abstract class ViewModel : BaseViewModel<UiEvents, UiState>()

    @Immutable
    data class UiState(
        val images: Flow<PagingData<CatImage>>?,
        val catDetail: CatDetail?,
        val initialPage: Page,
        val currentPage: Page,
        val tabRowTitles: List<String>,
    ) : BaseUiState {

        enum class Page(
            val index: Int,
            @StringRes val stringRes: Int,
            val imageVector: ImageVector,
        ) {
            Info(
                index = 0,
                stringRes = R.string.info_tab,
                imageVector = Icons.Outlined.Info,
            ),
            Images(
                index = 1,
                stringRes = R.string.image_tab,
                imageVector = Icons.Outlined.Photo
            );

            companion object {
                fun getPageForIndex(index: Int): Page {
                    return Page.values().find { it.index == index }
                        ?: throw IllegalArgumentException("No page exists for index $index")
                }
            }
        }
    }


    sealed class UiEvents : BaseUiEvent {
        data object Initialise : UiEvents()
        data object UpButtonClicked : UiEvents()
        data class OnPageSelected(val selectedPage: UiState.Page) : UiEvents()
    }
}
