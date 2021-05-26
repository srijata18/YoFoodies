package com.example.loginmodule.data

import com.example.networkmodule.data.UserDetails

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: UserDetails? = null,
    val error: Int? = null
)