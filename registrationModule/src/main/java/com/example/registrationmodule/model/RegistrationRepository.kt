package com.example.registrationmodule.model

import com.example.networkmodule.data.Result
import com.example.networkmodule.data.UserDetails
import com.example.networkmodule.model.CallBack

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class RegistrationRepository(val dataSource: RegistrationDataSource) {

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

    fun register(
        username: String,
        email: String,
        phno: Long,
        password: String,
        imageStr: String?,
        callBack: CallBack
    ) {
        // handle registration
        dataSource.register(username, email, phno, password, imageStr, object :
            CallBack {
            override fun onCallback(result: Result<UserDetails>) {
                callBack.onCallback(result)
            }
        })
    }

}