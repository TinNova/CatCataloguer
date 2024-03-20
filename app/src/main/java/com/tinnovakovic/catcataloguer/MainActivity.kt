package com.tinnovakovic.catcataloguer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
        enableEdgeToEdge()
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
//  MAPPERS IMPROVEMENT
// - Can CatMappers be improved? They are extension function except for one of them...
//  LINTING
// - Remove all hardcoded numbers and strings
//  UNIT TESTING
// - Write some quick unit tests
// - GITHUB README, take screenshots with large and small font

//TODO EXTRAS:
// - Add API to Header to avoid code duplication
//  GRADLE
// - Use the libs.versions.toml file to manage dependencies


//DONE:
// - [FAIL, didn't solve the clipping issue] Coil sometimes clipping is not applied, could adding a Key to the lazyColumn solve this?
// - Prevent rotation
// - Is LazyColumn Recomposing a lot? Could a key prevent that?
// - Offer option to display list of breed by Breed Name and Country
// - The images appear to jump in order, I think it's because some load faster than others giving that illusion
//   to fix this a placeholder needs to be implemented
// - Error Handling on HomeScreen
// - Consider System Process Death Recovery & Retain state of the filter option the user selected when returning to homeScreen!!
// - Error Handling - DetailScreen - Images with Page 3 Mediator - Info data, handle this with Result like you have in Hiking and EWallet
// - Is there a need to wrap network objects in a Result object? -> Nah Paging 3 Handles it for us.
// - For handling errors from Paging 3, to remove the error handling from Screen, consider sending a UIEvent to the ViewModel, map the error there and send back an error message to display?
// - Pull To Refresh doesn't work when there is an empty list, because it's nested with a list != null
//   - Look into how to set it up properly
// - Add Pull to refresh to detailImage
// - Set up pull to refresh as when offline first you won't get results
//  DEFENSIVE API PROGRAMMING
// - Make all Dto values nullable for defensive programming
// - What happens if any of the DTO values are null? App Crash...Can Result object prevent that? To test this remove the @Serialized param from country_code and it'll crash due to null object or pass an object via Retrofit with a null value
//  MISSING
// - LifeSpan and Weight missing from info screen, add another sharedPref for weight in lbs vs kgs
//  COIL CACHING
// - Caching in Coil is automatic but setup your own to access logging
//  MATERIAL DESIGN
// - Make the theme look like the one in Material 3: https://m3.material.io/components/tabs/guidelines
// - See if this colour change when scroll under TopAppBar can be enabled: https://m3.material.io/components/top-app-bar/guidelines#4eab4f50-4a3e-4189-bce2-a46514cde1da
// - Consider DarkMode/LightMode
//  ACCESSIBILITY AND LARGE FONT/ZOOMED
// - Consider Accessibility and what app looks like with enlarged fonts and zoom


