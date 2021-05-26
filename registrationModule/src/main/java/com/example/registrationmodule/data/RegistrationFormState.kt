package com.example.registrationmodule.data

/**
 * Data validation state of the login form.
 */
data class RegistrationFormState(
    val usernameError: Int? = null,
    val useremailError: Int? = null,
    val userphnoError: Int? = null,
    val userimgError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)