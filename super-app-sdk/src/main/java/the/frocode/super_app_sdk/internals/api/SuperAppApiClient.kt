package the.frocode.super_app_sdk.internals.api

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

fun provideRetrofit(endpoint: String, okHttpClient: OkHttpClient?): Retrofit {
    val builder = Retrofit.Builder()
        .baseUrl(endpoint)
        .addConverterFactory(GsonConverterFactory.create()) // You can replace Gson with another converter

    // Add OkHttpClient if it's not null
    okHttpClient?.let {
        builder.client(it)
    }

    return builder.build()
}

