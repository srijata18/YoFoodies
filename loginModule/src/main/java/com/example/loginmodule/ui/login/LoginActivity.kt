package com.example.loginmodule.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.custompreferences.Constants.LOGIN_PREFERENCE
import com.example.custompreferences.Constants.USERDETAILSKEY
import com.example.custompreferences.SharedPreferenceUtils
import com.example.custompreferences.SharedPreferenceUtils.set
import com.example.custompreferences.views.afterTextChanged
import com.example.custompreferences.views.gone
import com.example.loginmodule.R
import com.example.loginmodule.data.LoginFormState
import com.example.loginmodule.data.LoginResult
import com.example.loginmodule.model.LoginViewModel
import com.example.loginmodule.model.LoginViewModelFactory
import com.example.networkmodule.data.UserDetails
import com.example.registrationmodule.ui.registration.RegistrationActivity
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private var username: EditText? = null
    private var password: EditText? = null
    private var launchRegistration: TextView? = null
    private var loading: ProgressBar? = null
    private var login: Button? = null
    private var switch: Switch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        username = findViewById(R.id.et_username)
        password = findViewById(R.id.et_password)
        login = findViewById(R.id.login)
        launchRegistration = findViewById(R.id.tv_login_register)
        loading = findViewById(R.id.loading)
        switch = findViewById(R.id.rememberMeSwitch)

        loginViewModel = ViewModelProviders.of(
            this,
            LoginViewModelFactory()
        )
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer
            checkForLoginFormState(loginState)
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer
            displayResult(loginResult)
        })

        setClickListeners()
    }

    private fun checkForLoginFormState(loginState: LoginFormState) {
        // disable login button unless both username / password is valid
        login?.apply {
            isClickable = loginState.isDataValid
            login?.isEnabled = loginState.isDataValid
        }
        if (loginState.usernameError != null) {
            username?.error = getString(loginState.usernameError)
        }
        if (loginState.passwordError != null) {
            password?.error = getString(loginState.passwordError)
        }
    }

    private fun displayResult(loginResult: LoginResult) {
        loading?.gone()
        when {
            loginResult.error != null -> {
                showLoginFailed(loginResult.error)
            }
            loginResult.success != null -> {
                updateUiWithUser(loginResult.success.u_name)
                if (switch?.isChecked == true)
                    saveUserDetails(loginResult.success)

                val intent = Intent()
                intent.putExtra(USERDETAILSKEY, loginResult.success)
                setResult(Activity.RESULT_OK, intent)
                //Complete and destroy login activity once successful
                finish()
            }
        }
    }

    fun setClickListeners() {
        username?.afterTextChanged {
            checkForDataChange()
        }
        password?.apply {
            afterTextChanged {
                checkForDataChange()
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        performLogin()
                }
                false
            }
        }
        login?.setOnClickListener {
            performLogin()
        }
        launchRegistration?.setOnClickListener {
            launchRegistrationActivity()
        }

    }

    fun performLogin() {
        GlobalScope.launch(Dispatchers.Main) {
            loginViewModel.login(
                username?.text.toString(),
                password?.text.toString()
            )
        }
    }

    private fun launchRegistrationActivity() {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }

    private fun updateUiWithUser(model: String) {
        Toast.makeText(
            applicationContext,
            "Login Successful",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun saveUserDetails(userDetails: UserDetails) {
        val prefs = SharedPreferenceUtils.customPrefs(this, LOGIN_PREFERENCE)
        val json = Gson().toJson(userDetails)
        prefs.set(USERDETAILSKEY, json)
    }

    fun checkForDataChange() {
        loginViewModel.loginDataChanged(
            username?.text.toString(),
            password?.text.toString()
        )
    }
}
