package com.tinnovakovic.catcataloguer.presentation.detail

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tinnovakovic.catcataloguer.shared.Destination

fun NavGraphBuilder.detailScreen() {
    composable(route = "${Destination.Detail.name}/$CAT_BREED_ID") {
        DetailScreen()
    }
}

const val CAT_BREED_ID = "{catBreedId}"
