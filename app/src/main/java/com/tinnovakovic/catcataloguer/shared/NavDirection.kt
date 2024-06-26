package com.tinnovakovic.catcataloguer.shared

import androidx.navigation.NamedNavArgument

object NavDirection {

    val Default = object : NavCommand {

        override val arguments = emptyList<NamedNavArgument>()

        override val destinationRoute = ""

    }

    val homeScreen = object : NavCommand {

        override val arguments = emptyList<NamedNavArgument>()

        override val destinationRoute = NavDestination.Home.name

    }

    fun catBreedDetailScreen(
        catBreedId: String? = null,
        catBreedName: String? = null
    ) = object : NavCommand {

        override val arguments = emptyList<NamedNavArgument>()

        override val destinationRoute = "${NavDestination.Detail.name}/$catBreedId/$catBreedName"
    }

}