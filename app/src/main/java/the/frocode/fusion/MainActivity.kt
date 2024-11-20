package the.frocode.fusion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import the.frocode.super_app_sdk.TestMain2
import the.frocode.super_app_sdk.WebViewScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FusionTheme {
                val navController = rememberNavController()
                val url = "https://google.com"

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
                                    navController.navigate("webview/$url")
                                }) {
                                    Text("Open WebView")
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