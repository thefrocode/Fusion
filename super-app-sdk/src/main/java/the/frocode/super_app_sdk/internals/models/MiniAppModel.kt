package the.frocode.super_app_sdk.internals.models

data class MiniAppModel(val id: Int,
                        val name: String,
                        val category_id: Int,
                        val category_name: String,
                        val icon_url: String,
                        val build_url: String?)
