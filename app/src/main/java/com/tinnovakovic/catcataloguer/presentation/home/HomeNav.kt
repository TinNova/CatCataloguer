package com.tinnovakovic.catcataloguer.presentation.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tinnovakovic.catcataloguer.shared.NavDestination

fun NavGraphBuilder.homeScreen() {
    composable(route = NavDestination.Home.name) {
        HomeScreen()
    }
}