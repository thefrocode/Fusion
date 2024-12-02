package the.frocode.fusion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import the.frocode.fusion.ui.theme.FusionTheme
import the.frocode.super_app_sdk.SuperApp
import the.frocode.super_app_sdk.WebViewScreen
import the.frocode.super_app_sdk.internals.models.SuperAppModel
import the.frocode.fusion.getHttpClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FusionTheme {
                val navController = rememberNavController()
                val url = "https://google.com"
                SuperApp.initialize(
                    SuperAppModel(
                        "Round Robin",
                        "https://ec2-98-81-157-239.compute-1.amazonaws.com/fusion-api/"
                    ),
                    getHttpClient(this)
                )

                // A surface container using the 'background' color from the theme
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
                                    val url = "google.com"
                                    navController.navigate("screen2")
                                }) {
                                    Text("Open WebView")
                                }
                            }
                        }
                        composable("screen2") {
                            //put items in a listview
                            //SuperApp.miniApps.groupBy { it.category_id }
                            LazyColumn {
                                items(items=SuperApp.miniApps) {
                                    Text("Item ${it.name}")
                                }
                            }
                        }

                        composable("webview/{url}") { backStackEntry ->
                            val url = backStackEntry.arguments?.getString("url") ?: ""
                            WebViewScreen(url = url, onClose = { navController.popBackStack() })
                        }
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
    FusionTheme {
        Greeting("Android")
    }
}