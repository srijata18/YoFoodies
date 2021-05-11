package com.example.yofoddies.splash.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.custompreferences.SharedPreferenceUtils
import com.example.loginmodule.ui.login.LoginActivity
import com.example.custompreferences.Constants.LOGIN_PREFERENCE
import com.example.custompreferences.Constants.USERNAMEKEY
import com.example.yofoddies.MainActivity
import com.example.yofoddies.R
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {
    private val ACTIVITY_CODE = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        openRequiredActivity()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val userName = data?.getStringExtra(USERNAMEKEY)
            if (userName?.isBlank() == false)
                launchDashboardActivity(userName)
        }
        finish()
    }

    private fun launchDashboardActivity(userName: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(USERNAMEKEY, userName)
        startActivity(intent)
        finish()
    }

    private fun launchLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, ACTIVITY_CODE)
    }

    private fun openRequiredActivity() {
        val prefs = SharedPreferenceUtils.customPrefs(this, LOGIN_PREFERENCE)
        val userName = prefs.getString(USERNAMEKEY, "")
        val delayMillis = 1500L
        Handler().postDelayed({
            if (userName.isNullOrEmpty()) {
                launchLoginActivity()
            } else {
                launchDashboardActivity(userName)
            }
        }, delayMillis)
        Glide.with(this)
            .load(R.drawable.splash)
            .into(iv_background)
    }

}