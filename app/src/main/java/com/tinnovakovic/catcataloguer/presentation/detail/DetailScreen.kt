package com.tinnovakovic.catcataloguer.presentation.detail

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tinnovakovic.catcataloguer.R
import com.tinnovakovic.catcataloguer.presentation.detail.DetailContract.*
import com.tinnovakovic.catcataloguer.presentation.detail.images.DetailImagesScreen
import com.tinnovakovic.catcataloguer.presentation.detail.info.DetailInfoScreen
import kotlinx.coroutines.launch

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
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            MediumTopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = {
                    Text(text = uiState.catBreedName ?: "",
                        style = MaterialTheme.typography.headlineMedium)},
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

            DetailHorizontalPager()

        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun DetailHorizontalPager() {
    val pagerState = rememberPagerState(
        pageCount = { Page.entries.size }
    )

    val coroutineScope = rememberCoroutineScope()

    PrimaryTabRow(
        selectedTabIndex = pagerState.currentPage,
    ) {
        Page.entries.forEachIndexed { index, currentTab ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = {
                    Text(
                        text = stringResource(id = Page.entries[index].stringRes),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
            )
        }
    }

    HorizontalPager(
        state = pagerState,
    ) { pageIndex ->
        when (Page.entries[pageIndex]) {
            Page.Info -> {
                DetailInfoScreen()
            }
            Page.Images -> {
                DetailImagesScreen()
            }
        }
    }
}

enum class Page(
    @StringRes val stringRes: Int,
) {
    Info(
        stringRes = R.string.info_tab,
    ),
    Images(
        stringRes = R.string.image_tab,
    )
}
