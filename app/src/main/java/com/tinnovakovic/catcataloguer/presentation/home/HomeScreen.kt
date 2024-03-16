package com.tinnovakovic.catcataloguer.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
import com.tinnovakovic.catcataloguer.ui.theme.spacing


@Composable
fun HomeScreen() {
    val viewModel = hiltViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        uiState = uiState,
        uiAction = viewModel::onUiEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    uiState: UiState,
    uiAction: (UiEvents) -> Unit,
) {

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(text = "Cat Catalogue") },
            )
        }
    ) { scaffoldPadding ->
        Box(
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxWidth()
        ) {
            uiState.cats?.let { catPagingFlow ->
                val catLazyPagingItems: LazyPagingItems<Cat> =
                    catPagingFlow.collectAsLazyPagingItems()


                if (catLazyPagingItems.loadState.refresh is LoadState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(catLazyPagingItems) { cat ->
                            if (cat != null) {
                                CatItem(
                                    cat = cat,
                                    modifier = Modifier
                                        .clickable {
                                            uiAction(UiEvents.CatBreedClicked(cat.id))
                                        }
                                        .fillMaxSize()
                                        .padding(
                                            top = MaterialTheme.spacing.medium,
                                            bottom = MaterialTheme.spacing.medium,
                                            start = MaterialTheme.spacing.large,
                                            end = MaterialTheme.spacing.medium
                                        )

                                )
                                HorizontalDivider(modifier = Modifier.padding(start = MaterialTheme.spacing.large))
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
}

@Composable
fun CatItem(
    cat: Cat,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = cat.name,
            fontWeight = FontWeight.W600,
        )

        Column(horizontalAlignment = Alignment.End) {
            Text(text = cat.countryEmoji)
            Text(text = cat.origin)
        }
    }

}