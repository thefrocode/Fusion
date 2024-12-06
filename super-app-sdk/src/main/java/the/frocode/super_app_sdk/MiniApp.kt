package the.frocode.super_app_sdk

import androidx.navigation.NavController
import the.frocode.super_app_sdk.internals.nav.openWebview

class MiniApp(val app_url: String, val name: String) {

    fun openMiniApp(navController: NavController) {
        println("Opening Mini App")
        navController.openWebview(app_url)
    }
}