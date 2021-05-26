package com.example.dashboardmodule

import android.os.Build
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.custompreferences.Constants.USERDETAILSKEY
import com.example.networkmodule.data.UserDetails

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val userName : UserDetails? = intent?.extras?.getParcelable(USERDETAILSKEY)
        Log.i("===insideDActivity===","$userName")

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard, R.id.navigation_cart, R.id.navigation_notifications
            )
        )
        // Sending userName to the target fragment
        val bundle = Bundle()
        userName?.let {  bundle.putParcelable(USERDETAILSKEY, userName) }

        navController.navigate(R.id.navigation_dashboard, bundle)
        navView.setupWithNavController(navController)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this,android.R.color.holo_green_dark)
        }

    }
}