package the.frocode.super_app_sdk

import android.app.AlertDialog
import android.net.http.SslError
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebView.setWebContentsDebuggingEnabled
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.runtime.rememberUpdatedState
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment

@Composable
fun WebViewScreen(url: String, authToken: String, onClose: () -> Unit,onRequestConsent: () -> Unit) {
    val onCloseUpdated = rememberUpdatedState(onClose)  // Ensure the onClose callback is properly handled
    var webView: WebView? = remember { null }
    // Set up the WebView
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { context ->
            WebView(context).apply {
                this.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.userAgentString =
                    "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Mobile Safari/537.36"
                settings.cacheMode = WebSettings.LOAD_NO_CACHE
                settings.setAllowFileAccess(false)
                settings.setAllowContentAccess(false)

                // Enable debugging (can be removed in production)
                setWebContentsDebuggingEnabled(true)

                // Handle SSL errors (this part should be customized for production)
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)

                        // Inject the JavaScript bridge into the WebView (super app side)
                        val jsBridgeCode = """
                        window.superAppBridge = {

                            sendData: function(data) {
                                // Ensure that postMessage is available
                                if (window.postMessage) {
                                    console.log('Sending data to super app: ' + JSON.stringify(data));
                                    window.postMessage(data, '*');  // Send data to the super app
                                }
                            },
                            onReceiveData: function(callback) {
                                // Listen for messages from the miniapp
                                window.addEventListener('message', function(event) {


                                    callback(event.data);  // Invoke the callback with the received data
                                });
                            }


                        };
                        window.superAppBridge.onReceiveData(function(data) {
                            // Log the incoming message using Android Log.i
                            console.log("Received data in super app: " + JSON.stringify(data));
                            if (window.Android && window.Android.logMessage) {
                                window.Android.logMessage("Received data from miniapp: " + JSON.stringify(event.data));
                            }
                            if(data.type === 'auth') {
                                window.Android.getUserConsent();
                            }
                            console.log("Received data in super app: " + JSON.stringify(data));
                        });
                    """.trimIndent()

                        // Inject the JavaScript into the loaded WebView page
                        view?.evaluateJavascript(jsBridgeCode, null)
                        // Inject global error handler
                        view?.evaluateJavascript(
                            """
                        window.onerror = function(message, source, lineno, colno, error) {
                            var errorMsg = 'Error: ' + message + ' at ' + source + ':' + lineno + ':' + colno;
                            if (error && error.stack) {
                                errorMsg += '\\nStack: ' + error.stack;
                            }
                            window.Android.handleJsError(errorMsg);
                            return true;  // Prevent the error from propagating further
                        };
                    """.trimIndent(), null
                        )
                    }

                    override fun onReceivedSslError(
                        view: WebView?,
                        handler: SslErrorHandler?,
                        error: SslError?
                    ) {
                        // SSL error handling for development (avoid in production)
                        handler?.proceed()  // Allow invalid SSL certificates (for development)
                        Log.e("WebView", "SSL Error: ${error?.primaryError}")
                    }

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        // Ensure all URLs open in the same WebView
                        return false
                    }

                    override fun shouldInterceptRequest(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): WebResourceResponse? {
                        // Intercept requests (for debugging/logging)
                        val response = super.shouldInterceptRequest(view, request)
                        response?.responseHeaders?.forEach { header ->
                            Log.d("WebView", "Cache-Control: ${header.value}")
                        }
                        return response
                    }
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onJsAlert(
                        view: WebView?,
                        url: String?,
                        message: String?,
                        result: JsResult
                    ): Boolean {
                        // Display alert dialog
                        AlertDialog.Builder(context)
                            .setTitle("Alert")
                            .setMessage(message)
                            .setPositiveButton(android.R.string.ok) { _, _ -> result.confirm() }
                            .setCancelable(false)
                            .create()
                            .show()
                        return true
                    }

                    override fun onJsConfirm(
                        view: WebView?,
                        url: String?,
                        message: String?,
                        result: JsResult
                    ): Boolean {
                        // Display confirm dialog
                        AlertDialog.Builder(context)
                            .setTitle("Confirm")
                            .setMessage(message)
                            .setPositiveButton(android.R.string.ok) { _, _ -> result.confirm() }
                            .setNegativeButton(android.R.string.cancel) { _, _ -> result.cancel() }
                            .create()
                            .show()
                        return true
                    }

                    override fun onJsPrompt(
                        view: WebView?,
                        url: String?,
                        message: String?,
                        defaultValue: String?,
                        result: JsPromptResult
                    ): Boolean {
                        // Display prompt dialog
                        val input = defaultValue ?: ""
                        val editText = android.widget.EditText(context).apply { setText(input) }

                        AlertDialog.Builder(context)
                            .setTitle("Prompt")
                            .setMessage(message)
                            .setView(editText)
                            .setPositiveButton(android.R.string.ok) { _, _ ->
                                result.confirm(
                                    editText.text.toString()
                                )
                            }
                            .setNegativeButton(android.R.string.cancel) { _, _ -> result.cancel() }
                            .create()
                            .show()
                        return true
                    }
                }

                addJavascriptInterface(WebAppInterface(this, onRequestConsent), "Android")
                loadUrl("https://${url}")
            }

        }, modifier = Modifier.fillMaxSize())
    }
     BackHandler(enabled = webView?.canGoBack() == true) {
         webView?.goBack() ?: onCloseUpdated.value()  // If can't go back, close WebView
     }

}
class WebAppInterface(val webView: WebView,private val onRequestConsent: () -> Unit) {

    @JavascriptInterface
    fun getUserConsent() {
        Log.i("SuperAppBridge", "Received consent request from miniapp")
        webView.post{
            onRequestConsent()
        }
    }
    @JavascriptInterface
    fun logMessage(message: String) {
        Log.i("SuperAppBridge", message)  // Log the message with Log.i
    }
    @JavascriptInterface
    fun handleJsError(errorMessage: String) {
        Log.e("WebView", "JavaScript Error: $errorMessage")
    }
    private fun injectAuthToken(webView: WebView, authToken: String) {
        val jsCode = """
            window.superAppBridge.receiveAuthToken("$authToken");
        """.trimIndent()
        webView.evaluateJavascript(jsCode, null)
    }
}


// JavaScript error handler
class JsErrorHandler {
    @JavascriptInterface
    fun logMessage(message: String) {
        Log.i("SuperAppBridge", message)  // Log the message with Log.i
    }
    @JavascriptInterface
    fun handleJsError(errorMessage: String) {
        Log.e("WebView", "JavaScript Error: $errorMessage")
    }
}
