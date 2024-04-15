package com.tinnovakovic.catcataloguer.presentation.detail.images

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.tinnovakovic.catcataloguer.data.models.local.CatBreedImage
import com.tinnovakovic.catcataloguer.presentation.CatImage
import com.tinnovakovic.catcataloguer.presentation.CentredCircularLoadingIndicator
import com.tinnovakovic.catcataloguer.presentation.ItemCircularLoadingIndicator
import com.tinnovakovic.catcataloguer.presentation.ToastErrorMessage
import com.tinnovakovic.catcataloguer.presentation.detail.images.DetailImagesContract.*
import com.tinnovakovic.catcataloguer.presentation.isFirstLoad
import com.tinnovakovic.catcataloguer.ui.theme.spacing

@Composable
fun DetailImagesScreen() {
    val viewModel = hiltViewModel<DetailImagesViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetailImagesContent(
        uiState = uiState,
        uiAction = viewModel::onUiEvent,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailImagesContent(
    uiState: UiState,
    uiAction: (UiEvents) -> Unit,
) {
    LaunchedEffect(true) {
        // This instead on using init{} in viewModel to prevent race condition
        uiAction(UiEvents.Initialise)
    }

    val catImagePagingItems: LazyPagingItems<CatBreedImage> =
        uiState.images.collectAsLazyPagingItems()

    val pullRefreshState: PullRefreshState = rememberPullRefreshState(
        refreshing = false,
        onRefresh = { catImagePagingItems.refresh() }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.medium)
            .pullRefresh(pullRefreshState)
    ) {

        if (catImagePagingItems.loadState.refresh is LoadState.Loading
            || isFirstLoad(catImagePagingItems)
        ) {
            CentredCircularLoadingIndicator()
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
            ) {
                item {}
                items(
                    catImagePagingItems,
                    key = { it.id }
                ) { image ->
                    if (image != null) {
                        CatImage(
                            image = image.url,
                            aspectRatio = (image.width / image.height)
                        )
                    }

                    if (catImagePagingItems.loadState.refresh is LoadState.Error) {
                        (catImagePagingItems.loadState.refresh as LoadState.Error).error.message?.let {
                            ToastErrorMessage(it)
                        }
                    }
                }

                item {
                    when (catImagePagingItems.loadState.append) {
                        LoadState.Loading -> {
                            ItemCircularLoadingIndicator()
                        }

                        is LoadState.Error -> {
                            (catImagePagingItems.loadState.append as LoadState.Error).error.message?.let {
                                ToastErrorMessage(it)
                            }
                        }

                        is LoadState.NotLoading -> { /* no-op */
                        }
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = false,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}
