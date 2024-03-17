package com.tinnovakovic.catcataloguer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tinnovakovic.catcataloguer.presentation.detail.detailScreen
import com.tinnovakovic.catcataloguer.presentation.home.homeScreen
import com.tinnovakovic.catcataloguer.shared.Destination
import com.tinnovakovic.catcataloguer.shared.NavManager
import com.tinnovakovic.catcataloguer.ui.theme.CatCataloguerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navManager: NavManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatCataloguerTheme {
                val navController = rememberNavController()

                Scaffold { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Destination.Home.name,
                        Modifier.padding(innerPadding)
                    ) {
                        homeScreen()
                        detailScreen()
                    }
                    navManager.commands.collectAsState().value.also { command ->
                        if (command.destinationRoute.isNotEmpty()) navController.navigate(command.destinationRoute)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CatCataloguerTheme {
        Greeting("Android")
    }
}

//TODO:
// - Consider System Process Death Recovery & Retain state of the filter option the user selected when returning to homeScreen!!
// - Consider DarkMode/LightMode
// - Use the libs.versions.toml file to manage dependencies
// - Add API to Header to avoid code duplication
// - Is LazyColumn Recomposing a lot? Could a key prevent that?
// - Offer option to display list of breed by Breed Name and Country
// - Is there a need to wrap network objects in a Result object?
// - The images appear to jump in order, I think it's because some load faster than others giving that illusion
//   to fix this a placeholder needs to be implemented
// - Can CatMappers be improved? They are extension function except for one of them...
// - What happens if any of the DTO values are null? App Crash...Can Result object prevent that? To test this remove the @Serialized param from country_code and it'll crash due to null object or pass an object via Retrofit with a null value
// - Consider Accessibility and what app looks like with enlarged fonts and zoom
// - Remove all hardcoded numbers and strings
// - Double check if companion object need to be private or if their const vals need to be private
// - Fix the terrible transition from Home to Detail
// - Make all Dto values nullable for defensive programming
// - Make the theme look like the one in Material 3: https://m3.material.io/components/tabs/guidelines