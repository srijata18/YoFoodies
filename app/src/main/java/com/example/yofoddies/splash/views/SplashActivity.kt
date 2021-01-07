package com.example.yofoddies.splash.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.loginmodule.ui.login.LoginActivity
import com.example.loginmodule.utils.Constants.USERNAMEKEY
import com.example.yofoddies.MainActivity

class SplashActivity : AppCompatActivity() {
    private val ACTIVITY_CODE = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, ACTIVITY_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val userName = data?.getStringExtra(USERNAMEKEY)
            if (userName?.isBlank() == false)
                launchDashboardActivity(userName)
        }
    }

    private fun launchDashboardActivity(userName : String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(USERNAMEKEY, userName)
        startActivity(intent)
    }

}