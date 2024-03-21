package com.tinnovakovic.catcataloguer.presentation.detail.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tinnovakovic.catcataloguer.R
import com.tinnovakovic.catcataloguer.presentation.AnimatedLinearProgressIndicator
import com.tinnovakovic.catcataloguer.presentation.ExpandingCard
import com.tinnovakovic.catcataloguer.presentation.SubTitle
import com.tinnovakovic.catcataloguer.presentation.detail.info.DetailInfoContract.*
import com.tinnovakovic.catcataloguer.ui.theme.spacing

@Composable
fun DetailInfoScreen() {
    val viewModel = hiltViewModel<DetailInfoViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetailScreenContent(
        uiState = uiState,
        uiAction = viewModel::onUiEvent,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailScreenContent(
    uiState: UiState,
    uiAction: (UiEvents) -> Unit,
) {
    LaunchedEffect(true) {
        // This instead on using init{} in viewModel to prevent race condition
        uiAction(UiEvents.Initialise)
    }

    if (uiState.catDetail != null) {
        val catDetail = uiState.catDetail

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium)
        ) {
            item {
                ExpandingCard(
                    title = stringResource(id = R.string.description_title),
                    description = catDetail.description
                )

                if (catDetail.altNames.isNotEmpty()) {
                    SubTitle(text = stringResource(id = R.string.other_names_title))
                    Text(text = catDetail.altNames)
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraLarge),
                ) {
                    Column {
                        SubTitle(text = stringResource(id = R.string.origin_title))
                        Text(text = catDetail.origin)
                    }
                    Column {
                        SubTitle(text = stringResource(id = R.string.life_span_title))
                        Text(
                            text = stringResource(
                                id = R.string.life_span_years,
                                catDetail.lifeSpan
                            )
                        )
                    }
                    Column {
                        SubTitle(text = stringResource(id = R.string.weight_title))
                        Text(
                            text = stringResource(
                                id = R.string.weight_metric_imperial,
                                catDetail.weightMetric,
                                catDetail.weightImperial
                            )
                        )
                    }
                }

                if (catDetail.temperament.isNotEmpty()) {
                    SubTitle(text = stringResource(id = R.string.temperament_title))
                    FlowRow {
                        catDetail.temperament.forEach { temperament ->
                            SuggestionChip(
                                modifier = Modifier.padding(end = MaterialTheme.spacing.medium),
                                onClick = { /* No action */ },
                                label = {
                                    Text(text = temperament)
                                }
                            )
                        }
                    }
                }
            }

            item { SubTitle(text = stringResource(id = R.string.personality_traits_title)) }
            item {
                catDetail.personalityScores.forEach { personalityScore ->
                    Column(
                        modifier = Modifier.padding(top = MaterialTheme.spacing.small)
                    ) {
                        Text(
                            text = personalityScore.personality,
                            modifier = Modifier.padding(bottom = MaterialTheme.spacing.extraSmall)
                        )
                        AnimatedLinearProgressIndicator(
                            indicatorProgress = personalityScore.score.toFloat().div(5)
                        )
                    }
                }
            }

            item { SubTitle(text = stringResource(id = R.string.features_title)) }
            item {
                FlowRow(
                    modifier = Modifier.padding(bottom = MaterialTheme.spacing.large)
                ) {
                    catDetail.features.forEach { feature ->
                        Row {
                            SuggestionChip(
                                modifier = Modifier.padding(end = MaterialTheme.spacing.medium),
                                onClick = { /* No action */ },
                                icon = {
                                    if (feature.hasFeature) {
                                        Image(
                                            imageVector = Icons.Outlined.Check,
                                            colorFilter = ColorFilter.tint(Color.Green),
                                            contentDescription = stringResource(id = R.string.content_desc_true),
                                            modifier = Modifier.size(MaterialTheme.spacing.large)
                                        )
                                    } else {
                                        Image(
                                            imageVector = Icons.Outlined.Close,
                                            colorFilter = ColorFilter.tint(Color.Red),
                                            contentDescription = stringResource(id = R.string.content_desc_false),
                                            modifier = Modifier.size(MaterialTheme.spacing.large)
                                        )
                                    }
                                },
                                label = {
                                    Text(
                                        text = feature.feature,
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
