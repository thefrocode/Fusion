package the.frocode.fusion

import android.app.Application


//@HiltAndroidApp
class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        // Initialize ApiClient with application context
        ApiClient.initialize(this)
    }
}