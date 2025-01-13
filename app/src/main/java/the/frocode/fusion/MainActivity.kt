package the.frocode.fusion

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import the.frocode.fusion.ui.theme.FusionTheme
import the.frocode.super_app_sdk.SuperApp
import the.frocode.super_app_sdk.WebViewScreen
import the.frocode.super_app_sdk.internals.ConsentScreen
import the.frocode.super_app_sdk.internals.api.MiniAppApi


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FusionTheme {
                val navController = rememberNavController()
                val url = "https://google.com"
                SuperApp.initialize(
                        "Round Robin",
                    ApiClient.createService(MiniAppApi::class.java)
                )

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "screen1") {
                        composable("screen1") {
                            Column {
                                Text("Welcome to Screen 1")
                                Button(onClick = {
                                    // Navigate to webview route with URL
                                    val url = "daaf-41-90-181-180.ngrok-free.app"
                                    navController.navigate("webview/${url}")
                                }) {
                                    Text("Open WebView")
                                }
                            }
                        }
                        composable("screen2") {
                            //put items in a listview
                            val categorizedMiniapps = SuperApp.miniApps.groupBy { it.category_name }
                            Log.i("MainActivity", "Categorized Miniapps: $categorizedMiniapps")

                            Column {
                                categorizedMiniapps.forEach { (categoryName, miniapps) ->
                                    Text(
                                        text = "Category: $categoryName",
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                                    )
                                    Text(miniapps.toString())
                                    //MiniappsRow(miniapps = miniapps, navController = navController)
                                }
                            }
                        }

                        composable("webview/{url}") { backStackEntry ->
                            val url = backStackEntry.arguments?.getString("url") ?: ""
                            WebViewScreen(url = url, "ghg",
                                onClose = { navController.popBackStack() },
                                onRequestConsent = {
                                    navController.navigate("consent")
                            },)
                        }
                        composable("consent") {
                            ConsentScreen(
                                onCancel = {
                                    // Handle cancel action
                                },
                                onLogin = {
                                    // Handle login action
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}
sealed class Screens(val route : String) {
    object Login : Screens("login")
    object Home : Screens("home")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navigateTo: (route: String) -> Unit) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Home") },
        )
    }) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                navigateTo(Screens.Login.route)
            }) {
                Text(text = "Navigate to Login")
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
    FusionTheme {
        Greeting("Android")
    }
}