package the.frocode.super_app_sdk.internals.nav

import androidx.navigation.NavController

fun NavController.openWebview(url: String) {
    this.navigate("webview/$url")
}