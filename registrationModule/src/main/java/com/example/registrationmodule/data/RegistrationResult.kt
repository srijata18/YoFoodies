package com.example.registrationmodule.data

/**
 * Authentication result : success (user details) or error message.
 */
data class RegistrationResult(
    val success: RegistrationUserView? = null,
    val error: Int? = null
)