package com.tinnovakovic.catcataloguer.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.tinnovakovic.catcataloguer.data.models.local.CatImage
import com.tinnovakovic.catcataloguer.ui.theme.spacing
import kotlinx.coroutines.flow.Flow

@Composable
fun DetailImagesContent(images: Flow<PagingData<CatImage>>?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.medium)
    ) {
        images?.let { imagePagingFlow: Flow<PagingData<CatImage>> ->
            val catImageLazyPagingItems = imagePagingFlow.collectAsLazyPagingItems()

            if (catImageLazyPagingItems.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(catImageLazyPagingItems) { image ->
                        if (image != null) {
                            CatImage(
                                image = image.url,
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                    }
                    item {
                        if (catImageLazyPagingItems.loadState.append is LoadState.Loading) {
                            CircularProgressIndicator()
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
    AsyncImage(
        modifier = modifier.padding(vertical = MaterialTheme.spacing.small),
        model = image,
        contentDescription = "Cat"
    )
}
