package the.frocode.fusion

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

object ApiClient {

    private const val BASE_URL = "https://ec2-98-81-157-239.compute-1.amazonaws.com/fusion-api/"

    private lateinit var retrofit: Retrofit

    // Call this method once in your app initialization process
    fun initialize(context: Context) {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun getHttpClient(context: Context): OkHttpClient {
        // Load the custom certificate from res/raw
        val certificateInputStream = context.resources.openRawResource(R.raw.fusion)
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply { load(null, null) }
        val certificateFactory = CertificateFactory.getInstance("X.509")
        val certificate = certificateFactory.generateCertificate(certificateInputStream)
        keyStore.setCertificateEntry("custom_ca", certificate)

        val trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(keyStore)
        val trustManager = trustManagerFactory.trustManagers[0] as X509TrustManager

        val sslContext = SSLContext.getInstance("TLS").apply {
            init(null, arrayOf(trustManager), null)
        }

        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .hostnameVerifier { _, _ -> true }  // Skip hostname verification
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }


    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }


}
