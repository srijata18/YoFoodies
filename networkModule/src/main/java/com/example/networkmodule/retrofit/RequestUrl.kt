package com.example.networkmodule.retrofit

class RequestUrl {

    companion object {
        // replace with your app name
        const val BASE_URL = "https://${AppName.app_name}.firebaseio.com/"
        const val LOGIN_EXT = "users/"
        const val JSON_EXT = ".json"
    }
}