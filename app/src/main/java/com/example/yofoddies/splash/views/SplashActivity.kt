package com.example.yofoddies.splash.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.custompreferences.Constants.ACTIVITY_CODE
import com.example.custompreferences.Constants.LOGIN_PREFERENCE
import com.example.custompreferences.Constants.SPLASH_TIMEOUT
import com.example.custompreferences.Constants.USERDETAILSKEY
import com.example.custompreferences.SharedPreferenceUtils
import com.example.dashboardmodule.DashboardActivity
import com.example.loginmodule.ui.login.LoginActivity
import com.example.networkmodule.data.UserDetails
import com.example.yofoddies.R
import com.google.gson.Gson


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        openRequiredActivity()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_CODE && resultCode == Activity.RESULT_OK) {
            val userDetails: UserDetails? = data?.extras?.getParcelable(USERDETAILSKEY)
            if (userDetails != null)
                launchDashboardActivity(userDetails)
        }
        finish()
    }

    private fun launchDashboardActivity(userName: UserDetails?) {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra(USERDETAILSKEY, userName)
        startActivity(intent)
        finish()
    }

    private fun launchLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, ACTIVITY_CODE)
    }

    private fun openRequiredActivity() {
        val prefs = SharedPreferenceUtils.customPrefs(this, LOGIN_PREFERENCE)
        val gson = Gson()
        val json: String? = prefs.getString(USERDETAILSKEY, getString(R.string.defaut_str))
        val userDetails: UserDetails? = gson.fromJson(json, UserDetails::class.java)
        val delayMillis = SPLASH_TIMEOUT
        Handler().postDelayed({
            if (userDetails == null) {
                launchLoginActivity()
            } else {
                launchDashboardActivity(userDetails)
            }
        }, delayMillis)
    }

}