package com.example.loginmodule.model

import android.util.Log
import com.example.networkmodule.data.Result
import com.example.networkmodule.data.UserDetails
import com.example.networkmodule.model.CallBack
import com.example.networkmodule.retrofit.RequestUrl
import com.example.networkmodule.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String, callback: CallBack) {
        val retrofitClient = RetrofitClient.create()
        val url =
            String.format("${RequestUrl.BASE_URL}${RequestUrl.LOGIN_EXT}$username${RequestUrl.JSON_EXT}")
        val call = retrofitClient.getUser(
            url
        )

        call.enqueue(object : Callback<UserDetails?> {
            override fun onFailure(call: Call<UserDetails?>, t: Throwable) {
                if (t is HttpException) {
                    callback.onCallback(
                        Result.Error(
                            t.message()
                        )
                    )
                } else
                    t.message?.let { callback.onCallback(
                        Result.Error(
                            it
                        )
                    ) }
            }

            override fun onResponse(call: Call<UserDetails?>, response: Response<UserDetails?>) {
                if (response.body() != null && response.body() is UserDetails) {
                    Log.i("---useretails---", "${(response.body() as? UserDetails)}")
                    callback.onCallback(
                        Result.Success(
                            (response.body() as UserDetails)
                        )
                    )
                } else {
                    callback.onCallback(
                        Result.Error(
                            response.message(),
                            response.code()
                        )
                    )
                }
            }
        })
    }

    fun logout() {
        // TODO: revoke authentication
    }
}
