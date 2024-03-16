package com.tinnovakovic.catcataloguer.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.tinnovakovic.catcataloguer.data.models.local.Cat
import com.tinnovakovic.catcataloguer.presentation.home.HomeContract.UiEvents
import com.tinnovakovic.catcataloguer.presentation.home.HomeContract.UiState
import androidx.paging.compose.items


@Composable
fun HomeScreen() {
    val viewModel = hiltViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        uiState = uiState,
        uiAction = viewModel::onUiEvent,
    )
}

@Composable
fun HomeScreenContent(
    uiState: UiState,
    uiAction: (UiEvents) -> Unit,
) {

    Box(modifier = Modifier.fillMaxWidth()) {
        uiState.cats?.let { catPagingFlow ->
            val catLazyPagingItems: LazyPagingItems<Cat> = catPagingFlow.collectAsLazyPagingItems()


            if (catLazyPagingItems.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(catLazyPagingItems) { cat ->
                        if (cat != null) {
                            CatItem(
                                cat = cat,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        uiAction(UiEvents.CatBreedClicked(cat.id))
                                    }
                            )
                        }
                    }
                    item {
                        if (catLazyPagingItems.loadState.append is LoadState.Loading) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CatItem(
    cat: Cat,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = cat.name)
        Text(text = cat.origin)
    }

}