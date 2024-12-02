package the.frocode.super_app_sdk.internals.webview

import android.net.http.SslError
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient

class CustomWebViewClient : WebViewClient() {
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        view?.evaluateJavascript("""
            window.onerror = function(message, source, lineno, colno, error) {
                var errorMsg = 'Error: ' + message + ' at ' + source + ':' + lineno + ':' + colno;
                if (error && error.stack) {
                    errorMsg += '\\nStack: ' + error.stack;
                }
                window.Android.handleJsError(errorMsg);
                return true;
            };
        """.trimIndent(), null)
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        Log.e("WebView", "SSL Error: ${error?.primaryError}")
        handler?.cancel()
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return false
    }

    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
        val response = super.shouldInterceptRequest(view, request)
        response?.responseHeaders?.forEach { header ->
            Log.d("WebView", "Cache-Control: ${header.value}")
        }
        return response
    }
}