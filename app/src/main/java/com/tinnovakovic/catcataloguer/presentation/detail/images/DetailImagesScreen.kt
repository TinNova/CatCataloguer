package com.tinnovakovic.catcataloguer.presentation.detail.images

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.tinnovakovic.catcataloguer.R
import com.tinnovakovic.catcataloguer.data.models.local.CatImage
import com.tinnovakovic.catcataloguer.presentation.ToastErrorMessage
import com.tinnovakovic.catcataloguer.presentation.detail.images.DetailImagesContract.*
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

    if (uiState.displayError != null) {
        ToastErrorMessage(uiState.displayError)
        LaunchedEffect(true) {
            uiAction(UiEvents.ClearErrorMessage)
        }
    }

    val catImagePagingItems: LazyPagingItems<CatImage> =
        uiState.images.collectAsLazyPagingItems()

    val isLoading = remember { mutableStateOf(false) }
    val pullRefreshState: PullRefreshState = rememberPullRefreshState(
        refreshing = isLoading.value,
        onRefresh = { catImagePagingItems.refresh() }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.medium)
            .pullRefresh(pullRefreshState)
    ) {

        if (catImagePagingItems.loadState.refresh is LoadState.Loading) {
            isLoading.value = true
        } else {
            isLoading.value = false
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
                        if (!uiState.errorShown) {
                            uiAction(UiEvents.PagingError((catImagePagingItems.loadState.append as LoadState.Error).error))
                        }
                    }
                }

                item {
                    when (catImagePagingItems.loadState.append) {
                        LoadState.Loading -> {
                            CircularProgressIndicator()
                        }

                        is LoadState.Error -> {
                            if (!uiState.errorShown) {
                                uiAction(UiEvents.PagingError((catImagePagingItems.loadState.append as LoadState.Error).error))
                            }
                        }

                        is LoadState.NotLoading -> { /* no-op */
                        }
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isLoading.value,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@Composable
fun CatImage(
    image: String,
    aspectRatio: Float,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .aspectRatio(aspectRatio),
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(image)
            .scale(Scale.FIT)
            .placeholder(R.drawable.placeholder_image)
            .crossfade(true)
            .build(),
        contentScale = ContentScale.FillWidth,
        contentDescription = "Cat",
        filterQuality = FilterQuality.None
    )
}
