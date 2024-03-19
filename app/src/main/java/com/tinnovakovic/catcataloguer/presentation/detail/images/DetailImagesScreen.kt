package com.tinnovakovic.catcataloguer.presentation.detail.images

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tinnovakovic.catcataloguer.R
import com.tinnovakovic.catcataloguer.data.models.local.CatImage
import com.tinnovakovic.catcataloguer.presentation.ToastErrorMessage
import com.tinnovakovic.catcataloguer.presentation.detail.images.DetailImagesContract.*
import com.tinnovakovic.catcataloguer.ui.theme.spacing
import kotlinx.coroutines.flow.Flow

@Composable
fun DetailImagesScreen() {
    val viewModel = hiltViewModel<DetailImagesViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetailImagesContent(
        uiState = uiState,
        uiAction = viewModel::onUiEvent,
    )
}

@Composable
fun DetailImagesContent(
    uiState: UiState,
    uiAction: (UiEvents) -> Unit,
) {
    LaunchedEffect(true) {
        // This instead on using init{} in viewModel to prevent race condition
        uiAction(UiEvents.Initialise)
    }

    if (uiState.displayError != null) {
        ToastErrorMessage(uiState.displayError)
        LaunchedEffect(true) {
            uiAction(UiEvents.ClearErrorMessage)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.medium)
    ) {
        uiState.images?.let { imagePagingFlow: Flow<PagingData<CatImage>> ->
            val catImageLazyPagingItems = imagePagingFlow.collectAsLazyPagingItems()

            when (catImageLazyPagingItems.loadState.refresh) {
                LoadState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is LoadState.Error -> {
                    LaunchedEffect(true) {
                        uiAction(UiEvents.PagingError((catImageLazyPagingItems.loadState.refresh as LoadState.Error).error))
                    }
                }

                is LoadState.NotLoading -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        item {}
                        items(
                            catImageLazyPagingItems,
                            key = { it.id }
                        ) { image ->
                            if (image != null) {
                                CatImage(
                                    image = image.url,
                                )
                            }
                        }
                        item {
                            when (catImageLazyPagingItems.loadState.append) {
                                LoadState.Loading -> {
                                    CircularProgressIndicator()
                                }

                                is LoadState.Error -> {
                                    if (!uiState.errorShown) {
                                        uiAction(UiEvents.PagingError((catImageLazyPagingItems.loadState.append as LoadState.Error).error))
                                    }
                                }

                                is LoadState.NotLoading -> { /* no-op */
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CatImage(
    image: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.wrapContentHeight()
    ) {
        AsyncImage(
            modifier = modifier
                .fillMaxWidth(),
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(image)
                .placeholder(R.drawable.placeholder_image)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.FillWidth,
            contentDescription = "Cat",
            filterQuality = FilterQuality.None
        )
    }
}
