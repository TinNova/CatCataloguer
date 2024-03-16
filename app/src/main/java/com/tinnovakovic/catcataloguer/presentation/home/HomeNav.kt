package com.tinnovakovic.catcataloguer.presentation.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tinnovakovic.catcataloguer.shared.Destination

fun NavGraphBuilder.homeScreen() {
    composable(route = Destination.Home.name) {
        HomeScreen()
    }
}