package com.example.loginmodule.model

import com.example.networkmodule.data.Result
import com.example.networkmodule.data.UserDetails
import com.example.networkmodule.model.CallBack

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: UserDetails? = null
        private set

    init {
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(
        username: String, password: String,
        callBack: CallBack
    ) {
        // handle login
        dataSource.login(username, password, object :
            CallBack {
            override fun onCallback(result: Result<UserDetails>) {
                if (result is Result.Success) {
                    setLoggedInUser(result.data)
                }
                callBack.onCallback(result)
            }
        })
    }

    private fun setLoggedInUser(loggedInUser: UserDetails) {
        this.user = loggedInUser
    }

}