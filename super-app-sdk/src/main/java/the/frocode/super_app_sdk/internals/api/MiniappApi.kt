package the.frocode.super_app_sdk.internals.api

import retrofit2.http.GET
import the.frocode.super_app_sdk.MiniApp
import the.frocode.super_app_sdk.internals.models.MiniAppModel

interface MiniAppApi {
    @GET("miniapps")  // Adjust the endpoint as needed
    suspend fun getMiniApps(): List<MiniApp>  // Replace MiniApp with your data model
}