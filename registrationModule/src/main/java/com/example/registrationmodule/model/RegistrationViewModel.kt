package com.example.registrationmodule.model

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.networkmodule.data.Result
import com.example.networkmodule.data.UserDetails
import com.example.networkmodule.model.CallBack
import com.example.registrationmodule.R
import com.example.registrationmodule.data.RegistrationFormState
import com.example.registrationmodule.data.RegistrationResult
import com.example.registrationmodule.data.RegistrationUserView
import java.util.regex.Pattern

class RegistrationViewModel(private val registrationRepository: RegistrationRepository) :
    ViewModel() {

    private val _registrationForm = MutableLiveData<RegistrationFormState>()
    val registrationFormState: LiveData<RegistrationFormState> = _registrationForm

    private val _registrationResult = MutableLiveData<RegistrationResult>()
    val registrationResult: LiveData<RegistrationResult> = _registrationResult

    fun register(
        username: String,
        email: String,
        phno: String,
        password: String,
        imageStr: String? = null
    ) {
        // can be launched in a separate asynchronous job
        registrationRepository.register(username, email, phno.toLong(), password, imageStr, object :
            CallBack {
            override fun onCallback(result: Result<UserDetails>) {
                if (result is Result.Success) {
                    _registrationResult.value =
                        RegistrationResult(
                            success = RegistrationUserView(
                                displayName = result.data.u_name
                            )
                        )
                } else {
                    _registrationResult.value =
                        RegistrationResult(error = R.string.registration_failed)
                }
            }
        })
    }

    fun registrationDataChanged(username: String, email: String, phno: String, password: String) {
        if (!isUserNameValid(username)) {
            _registrationForm.value =
                RegistrationFormState(usernameError = R.string.invalid_username)
        } else if (!isEmailValid(email)) {
            _registrationForm.value =
                RegistrationFormState(useremailError = R.string.invalid_email)
        } else if (!isPhNoValid(phno)) {
            _registrationForm.value =
                RegistrationFormState(userphnoError = R.string.invalid_phno)
        } else if (!isPasswordValid(password)) {
            _registrationForm.value =
                RegistrationFormState(passwordError = R.string.invalid_password)
        } else {
            _registrationForm.value =
                RegistrationFormState(isDataValid = true)
        }
    }

    // A placeholder email validation check
    fun isEmailValid(email: String): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    // A placeholder username validation check
    fun isUserNameValid(username: String?): Boolean {
        username?.let {
            val regex = "^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]+$"
            val x = "^\\d*[a-zA-Z][a-zA-Z\\d]*\$"
            val pattern = Pattern.compile(x)
            val matcher = pattern.matcher(username)
            return it.isNotBlank() && matcher.matches()
        }
        return false
    }

    // A placeholder password validation check
    fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    fun isPhNoValid(phno: String): Boolean {
        return phno.isNotBlank() && Patterns.PHONE.matcher(phno).matches()
    }

}