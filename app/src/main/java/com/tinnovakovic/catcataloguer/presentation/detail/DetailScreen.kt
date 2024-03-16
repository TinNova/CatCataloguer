package com.tinnovakovic.catcataloguer.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.tinnovakovic.catcataloguer.data.models.local.CatImage
import com.tinnovakovic.catcataloguer.ui.theme.spacing
import kotlinx.coroutines.flow.Flow

@Composable
fun DetailScreen() {
    val viewModel = hiltViewModel<DetailViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetailScreenContent(
        uiState = uiState,
        uiAction = viewModel::onUiEvent,
    )
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun DetailScreenContent(
    uiState: DetailContract.UiState,
    uiAction: (DetailContract.UiEvents) -> Unit,
) {
    LaunchedEffect(true) {
        // This instead on using init{} in viewModel to prevent race condition
        uiAction(DetailContract.UiEvents.Initialise)
    }

    if (uiState.catDetail != null) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = uiState.catDetail.name) },
                    navigationIcon = {
                        IconButton(onClick = { uiAction(DetailContract.UiEvents.UpButtonClicked) }
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
                    .padding(horizontal = MaterialTheme.spacing.medium)
            ) {
                SubTitle(text = "Description")
                Text(text = uiState.catDetail.description)

                if (uiState.catDetail.altNames.isNotEmpty()) {
                    SubTitle(text = "Other Names")
                    Text(text = uiState.catDetail.altNames)
                }
                SubTitle(text = "Origin")

                Text(text = uiState.catDetail.origin)
                if (uiState.catDetail.temperament.isNotEmpty()) {
                    SubTitle(text = "Temperament")
                    FlowRow() {
                        uiState.catDetail.temperament.forEachIndexed { index: Int, temperament: String ->
                            Chip(modifier = Modifier.padding(end = MaterialTheme.spacing.medium),
                                onClick = {}) {
                                Text(text = temperament)
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    uiState.images?.let { imagePagingFlow: Flow<PagingData<CatImage>> ->
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
        }
    } else {
        CircularProgressIndicator()
    }
}

@Composable
fun CatImage(
    image: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        modifier = Modifier.padding(vertical = MaterialTheme.spacing.small),
        model = image,
        contentDescription = ""
    )
}

@Composable
fun SubTitle(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = MaterialTheme.spacing.large)
    )

}