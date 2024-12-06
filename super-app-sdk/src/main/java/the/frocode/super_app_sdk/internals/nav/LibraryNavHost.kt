import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import the.frocode.super_app_sdk.WebViewScreen

sealed class Screens(val route: String) {
    object Home : Screens("home")
    data class WebView(val url: String) : Screens("webview/{url}") {
        fun createRoute(): String = "webview/$url"
    }
}

@Composable
fun LibraryNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ) {
        composable(route = Screens.Home.route) {

        }
        composable(Screens.WebView("/{url}").route) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            WebViewScreen(url = url, onClose = { navController.popBackStack() })
        }
    }
}



