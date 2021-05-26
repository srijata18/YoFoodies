package com.example.networkmodule.model

import com.example.networkmodule.data.Result
import com.example.networkmodule.data.UserDetails

interface CallBack {
    fun onCallback(result : Result<UserDetails>)
}