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
                    navManager.commands.collectAsState().value.also { command ->
                        if (command.destinationRoute.isNotEmpty()) navController.navigate(command.destinationRoute)
                    }
                    NavHost(
                        navController = navController,
                        startDestination = Destination.Home.name,
                        Modifier.padding(innerPadding)
                    ) {
                        homeScreen()
                        detailScreen()
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
// - Consider System Process Death Recovery
// - Use the libs.versions.toml file to manage dependencies
// - Add API to Header to avoid code duplication
// - Is LazyColumn Recomposing a lot? Could a key prevent that?
// - Offer option to display list of breed by Breed Name and Country
// - Is there a need to wrap network objects in a Result object?
// - The images appear to jump in order, I think it's because some load faster than others giving that illusion
//   to fix this a placeholder needs to be implemented