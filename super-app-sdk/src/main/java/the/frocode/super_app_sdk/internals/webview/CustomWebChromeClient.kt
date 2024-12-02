package the.frocode.super_app_sdk.internals.webview

import android.app.AlertDialog
import android.content.Context
import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView

class CustomWebChromeClient(private val context: Context) : WebChromeClient() {
    override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult): Boolean {
        AlertDialog.Builder(context)
            .setTitle("Alert")
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { _, _ -> result.confirm() }
            .setCancelable(false)
            .create()
            .show()
        return true
    }

    override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult): Boolean {
        AlertDialog.Builder(context)
            .setTitle("Confirm")
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { _, _ -> result.confirm() }
            .setNegativeButton(android.R.string.cancel) { _, _ -> result.cancel() }
            .create()
            .show()
        return true
    }

    override fun onJsPrompt(view: WebView?, url: String?, message: String?, defaultValue: String?, result: JsPromptResult): Boolean {
        val input = defaultValue ?: ""
        val editText = android.widget.EditText(context).apply { setText(input) }

        AlertDialog.Builder(context)
            .setTitle("Prompt")
            .setMessage(message)
            .setView(editText)
            .setPositiveButton(android.R.string.ok) { _, _ -> result.confirm(editText.text.toString()) }
            .setNegativeButton(android.R.string.cancel) { _, _ -> result.cancel() }
            .create()
            .show()
        return true
    }
}