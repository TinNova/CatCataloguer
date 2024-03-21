package com.tinnovakovic.catcataloguer.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.tinnovakovic.catcataloguer.data.models.local.CatBreed
import com.tinnovakovic.catcataloguer.presentation.home.HomeContract.UiEvents
import com.tinnovakovic.catcataloguer.presentation.home.HomeContract.UiState
import androidx.paging.compose.items
import com.tinnovakovic.catcataloguer.R
import com.tinnovakovic.catcataloguer.presentation.CatItem
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreenContent(
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

    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            LargeTopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = {
                    Text(
                        text = "Cat Catalogue",
                        style = MaterialTheme.typography.headlineLarge
                    )
                },
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            imageVector = Icons.Filled.FilterList,
                            contentDescription = "Filter" // Provide a descriptive content description for accessibility
                        )
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = {
                                showMenu = false
                            }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(id = R.string.sort_order, uiState.sortOrder)) },
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

        val catBreedLazyPagingItems: LazyPagingItems<CatBreed> =
            uiState.cats.collectAsLazyPagingItems()

        val isLoading = remember { mutableStateOf(false) }
        val pullRefreshState: PullRefreshState = rememberPullRefreshState(
            refreshing = isLoading.value,
            onRefresh = { catBreedLazyPagingItems.refresh() }
        )

        Box(
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (catBreedLazyPagingItems.loadState.refresh is LoadState.Loading) {
                    isLoading.value = true
                } else {
                    isLoading.value = false
                    items(
                        catBreedLazyPagingItems,
                        key = { it.id }
                    ) { cat ->
                        if (cat != null) {
                            CatItem(
                                catBreed = cat,
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

                        if (catBreedLazyPagingItems.loadState.refresh is LoadState.Error) {
                            LaunchedEffect(true) {
                                uiAction(UiEvents.PagingError((catBreedLazyPagingItems.loadState.refresh as LoadState.Error).error))
                            }
                            isLoading.value = false
                        }
                    }

                    item {
                        when (catBreedLazyPagingItems.loadState.append) {
                            is LoadState.Loading -> {
                                isLoading.value = true
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }

                            is LoadState.Error -> LaunchedEffect(true) {
                                isLoading.value = false
                                uiAction(UiEvents.PagingError((catBreedLazyPagingItems.loadState.append as LoadState.Error).error))
                            }

                            is LoadState.NotLoading -> {
                                isLoading.value = false
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
}
