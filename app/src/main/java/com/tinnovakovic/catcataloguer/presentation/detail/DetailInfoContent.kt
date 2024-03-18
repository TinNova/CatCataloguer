package com.tinnovakovic.catcataloguer.presentation.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import com.tinnovakovic.catcataloguer.data.models.local.CatDetail
import com.tinnovakovic.catcataloguer.ui.theme.spacing

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailInfoContent(catDetail: CatDetail?) {
    if (catDetail != null) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium)
        ) {
            item {
                SubTitle(text = "Description")
                Text(text = catDetail.description)

                if (catDetail.altNames.isNotEmpty()) {
                    SubTitle(text = "Other Names")
                    Text(text = catDetail.altNames)
                }
                SubTitle(text = "Origin")
                Text(text = catDetail.origin)

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
                                    Text(text = "${personalityScore.score}/5")
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
