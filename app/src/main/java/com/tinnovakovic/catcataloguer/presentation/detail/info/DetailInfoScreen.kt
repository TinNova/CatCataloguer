package com.tinnovakovic.catcataloguer.presentation.detail.info

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tinnovakovic.catcataloguer.presentation.ExpandingCard
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
                ExpandingCard(title = "Description", description = catDetail.description)

                if (catDetail.altNames.isNotEmpty()) {
                    SubTitle(text = "Other Names")
                    Text(text = catDetail.altNames)
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraLarge),
                ) {
                    Column {
                        SubTitle(text = "Origin")
                        Text(text = catDetail.origin)
                    }
                    Column {
                        SubTitle(text = "Life Span")
                        Text(text = "${catDetail.lifeSpan} years")
                    }
                    Column {
                        SubTitle(text = "Weight")
                        Text(text = "${catDetail.weightMetric}kg | ${catDetail.weightImperial}lbs")
                    }
                }

                if (catDetail.temperament.isNotEmpty()) {
                    SubTitle(text = "Temperament")
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

            item { SubTitle(text = "Personality Traits") }
            item {
                FlowRow(
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                    maxItemsInEachRow = 4,
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    catDetail.personalityScores.forEach { personalityScore ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = personalityScore.personality,
                                modifier = Modifier.fillMaxWidth(0.32f)
                            )
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = if (personalityScore.score == -1) {
                                            "N/A"
                                        } else {
                                            "${personalityScore.score}/5"
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }

            item { SubTitle(text = "Features") }
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
                                            contentDescription = "true",
                                            modifier = Modifier.size(MaterialTheme.spacing.large)
                                        )
                                    } else {
                                        Image(
                                            imageVector = Icons.Outlined.Close,
                                            colorFilter = ColorFilter.tint(Color.Red),
                                            contentDescription = "false",
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
