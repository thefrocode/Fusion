package the.frocode.super_app_sdk

import kotlinx.coroutines.runBlocking
import the.frocode.super_app_sdk.internals.api.MiniAppApi
import the.frocode.super_app_sdk.internals.models.MiniAppModel

object SuperApp {
    // Flag to check if SDK has been initialized
    private var isInitialized = false

    private var _name: String? = null

    val name: String
        get() = _name ?: throw IllegalStateException("Object is not initialized. Call initialize() first.")

    private var miniAppApi: MiniAppApi? = null
    private var miniAppList: List<MiniApp>? = null

    val miniApps: List<MiniApp>
        get() {
            return miniAppList ?: throw IllegalStateException("Mini app list is not initialized.")
        }


    /**
     * Initialize the SuperApp SDK with configuration details.
     *
     * @param superApp The configuration model containing necessary data for SDK (like miniAppsUrl).
     */
    fun initialize(name: String, miniAppApi: MiniAppApi) {
        if (isInitialized) {
            throw IllegalStateException("SuperApp is already initialized.")
        }

        _name = name
        this.miniAppApi = miniAppApi
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
    suspend fun fetchMiniApps(): List<MiniApp> {
        if (!isInitialized) {
            throw IllegalStateException("SuperApp is not initialized. Call initialize() first.")
        }

        // Call the MiniAppApi to fetch mini apps
        miniAppList= miniAppApi?.getMiniApps() ?: throw IllegalStateException("Failed to get MiniAppApi instance.")
        miniAppList!!.forEach { println("MiniApp: $it") }
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
