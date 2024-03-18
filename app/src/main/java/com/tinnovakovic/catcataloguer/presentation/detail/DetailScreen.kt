package com.tinnovakovic.catcataloguer.presentation.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tinnovakovic.catcataloguer.presentation.detail.DetailContract.*
import kotlinx.coroutines.flow.drop

@Composable
fun DetailScreen() {
    val viewModel = hiltViewModel<DetailViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetailScreenContent(
        uiState = uiState,
        uiAction = viewModel::onUiEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenContent(
    uiState: UiState,
    uiAction: (UiEvents) -> Unit,
) {
    LaunchedEffect(true) {
        // This instead on using init{} in viewModel to prevent race condition
        uiAction(UiEvents.Initialise)
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(title = { Text(text = uiState.catDetail?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = { uiAction(UiEvents.UpButtonClicked) }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
        ) {

            DetailHorizontalPager(uiState, uiAction)

        }
    }
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun DetailHorizontalPager(uiState: UiState, uiAction: (UiEvents) -> Unit) {
    val pagerState = rememberPagerState(
        initialPage = uiState.initialPage.index, // The initial page to show
        pageCount = { uiState.tabRowTitles.size }
    )

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .drop(1)
            .collect { currentPageIndex ->
                uiAction(UiEvents.OnPageSelected(UiState.Page.getPageForIndex(currentPageIndex)))
            }
    }

    PrimaryTabRow(
        selectedTabIndex = uiState.currentPage.index,
    ) {
        uiState.tabRowTitles.forEachIndexed { index, currentTab ->
            Tab(
                selected = uiState.currentPage.index == index,
                onClick = {
                    uiAction(UiEvents.OnPageSelected(UiState.Page.getPageForIndex(index)))
                },
                icon = {
                    Image(
                        imageVector = UiState.Page.getPageForIndex(index).imageVector,
                        contentDescription = stringResource(id = UiState.Page.getPageForIndex(index).stringRes)
                    )
                },
                text = {
                    Text(
                        text = stringResource(id = UiState.Page.getPageForIndex(index).stringRes),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
            )
        }
    }

    LaunchedEffect(uiState.currentPage) {
        pagerState.animateScrollToPage(uiState.currentPage.index)
    }

    HorizontalPager(
        state = pagerState,
    ) { pageIndex ->
        when (UiState.Page.getPageForIndex(pageIndex)) {
            UiState.Page.Info -> {
                DetailInfoContent(catDetail = uiState.catDetail)
            }

            UiState.Page.Images -> {
                DetailImagesContent(images = uiState.images)
            }
        }
    }
}
