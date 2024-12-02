package the.frocode.super_app_sdk

import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import the.frocode.super_app_sdk.internals.api.MiniAppApi
import the.frocode.super_app_sdk.internals.api.provideRetrofit
import the.frocode.super_app_sdk.internals.models.SuperAppModel
import retrofit2.Retrofit
import the.frocode.super_app_sdk.internals.models.MiniAppModel

object SuperApp {
    // Flag to check if SDK has been initialized
    private var isInitialized = false

    // SuperAppModel holds the initialized configuration (e.g., URL for mini apps)
    private var _superApp: SuperAppModel? = null

    // Public property to access the SuperAppModel (throws exception if not initialized)
    val superApp: SuperAppModel
        get() = _superApp ?: throw IllegalStateException("Object is not initialized. Call initialize() first.")

    // The Retrofit instance for making network requests
    private var retrofit: Retrofit? = null
    private var miniAppApi: MiniAppApi? = null
    private var miniAppList: List<MiniAppModel>? = null

    val miniApps: List<MiniAppModel>
        get() {
            return miniAppList ?: throw IllegalStateException("Mini app list is not initialized.")
        }


    /**
     * Initialize the SuperApp SDK with configuration details.
     *
     * @param superApp The configuration model containing necessary data for SDK (like miniAppsUrl).
     */
    fun initialize(superApp: SuperAppModel, okHttpClient: OkHttpClient?=null) {
        if (isInitialized) {
            throw IllegalStateException("SuperApp is already initialized.")
        }

        _superApp = superApp
        retrofit = provideRetrofit(superApp.miniAppsUrl, okHttpClient)  // Provide Retrofit instance with the URL
        miniAppApi = retrofit?.create(MiniAppApi::class.java)  // Create API service instance
        isInitialized = true
        runBlocking {
            fetchMiniApps()
            println("Fetched Mini Apps: $miniApps")
        }

    }

    /**
     * Fetch the list of mini apps from the API.
     *
     * @return List of mini apps.
     * @throws IllegalStateException if the SDK is not initialized.
     */
    suspend fun fetchMiniApps(): List<MiniAppModel> {
        if (!isInitialized) {
            throw IllegalStateException("SuperApp is not initialized. Call initialize() first.")
        }

        // Call the MiniAppApi to fetch mini apps
        miniAppList= miniAppApi?.getMiniApps() ?: throw IllegalStateException("Failed to get MiniAppApi instance.")
        return miniAppList!!
    }

    /**
     * Open a mini app by passing its URL and load into a WebView.
     *
     * @param context Context to start the WebView screen.
     * @param miniApp The mini app to load in the WebView.
     * @throws IllegalStateException if the SDK is not initialized.
     */
//    fun openMiniApp(context: Context, miniApp: MiniApp) {
//        if (!isInitialized) {
//            throw IllegalStateException("SuperApp is not initialized. Call initialize() first.")
//        }
//
//        // Here you can use a method to start a WebView with the miniApp URL.
//        // Example: MiniAppApi.openMiniApp(context, miniApp.manifestUrl) (Implement WebView handling here)
//    }
}
