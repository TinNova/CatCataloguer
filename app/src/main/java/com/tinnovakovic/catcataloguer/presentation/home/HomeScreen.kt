package com.tinnovakovic.catcataloguer.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.tinnovakovic.catcataloguer.data.models.local.Cat
import com.tinnovakovic.catcataloguer.presentation.home.HomeContract.UiEvents
import com.tinnovakovic.catcataloguer.presentation.home.HomeContract.UiState
import androidx.paging.compose.items
import com.tinnovakovic.catcataloguer.presentation.ToastErrorMessage
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
    LaunchedEffect(true) {
        // This instead on using init{} in viewModel to prevent race condition
        uiAction(UiEvents.Initialise)
    }

    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(text = "Cat Catalogue") },
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            imageVector = Icons.Filled.FilterList,
                            contentDescription = "Filter" // Provide a descriptive content description for accessibility
                        )
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Filter By: ${uiState.sortOrder}") },
                                onClick = {
                                    showMenu = false
                                    uiAction(UiEvents.FilterOptionClicked(uiState.sortOrder))
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { scaffoldPadding ->

        if (uiState.displayError != null) {
            ToastErrorMessage(uiState.displayError)
            LaunchedEffect(true) {
                uiAction(UiEvents.ClearErrorMessage)
            }
        }

        Box(
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize()
        ) {
            uiState.cats?.let { catPagingFlow ->
                val catLazyPagingItems: LazyPagingItems<Cat> =
                    catPagingFlow.collectAsLazyPagingItems()

                when (catLazyPagingItems.loadState.refresh) {
                    is LoadState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    is LoadState.Error -> {
                        LaunchedEffect(true) {
                            uiAction(UiEvents.PagingError((catLazyPagingItems.loadState.refresh as LoadState.Error).error))
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            items(
                                catLazyPagingItems,
                                key = { it.id }) { cat ->
                                if (cat != null) {
                                    CatItem(
                                        cat = cat,
                                        modifier = Modifier
                                            .clickable {
                                                uiAction(UiEvents.CatBreedClicked(cat.id, cat.name))
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
                                when (catLazyPagingItems.loadState.append) {
                                    is LoadState.Loading -> {
                                        CircularProgressIndicator(
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                    }

                                    is LoadState.Error -> LaunchedEffect(true) {
                                        uiAction(UiEvents.PagingError((catLazyPagingItems.loadState.append as LoadState.Error).error))
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
