package com.example.yofoddies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.custompreferences.Constants.USERNAMEKEY
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val userName = intent?.getStringExtra(USERNAMEKEY)
        Toast.makeText(this, "Welcome $userName", Toast.LENGTH_SHORT).show()
        tv_userName?.text = String.format(getString(R.string.welcome), userName)
    }
}