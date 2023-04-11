package com.coding_titans.uptodate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "my_channel_id"
    private val notificationId = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //notifications

        createNotificationChannel()

        val intent = Intent(this@MainActivity, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this@MainActivity,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Welcome Back!")
            .setContentText("Read Latest News On UptoDate News.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(null)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }

        // check if user is logged in
        val dbHelper = DBHelper(this)

        // setting top action bar
        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar()?.setCustomView(R.layout.toolbar_title_layout);

        // Navigation Host
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Bottom Navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)


        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                }
                R.id.countryBasedNewsFragment -> {
                    navController.navigate(R.id.countryBasedNewsFragment)
                }
                R.id.aboutUsFragment -> {
                    navController.navigate(R.id.aboutUsFragment)
                }
                R.id.profileFragment -> {
                    val isLoggedIn = dbHelper.checkLoggedIn(this)
                    if(isLoggedIn == "") {
                        navController.navigate(R.id.signInFragment)
                    } else {
                        val bundle = Bundle()
                        bundle.putString("email", isLoggedIn)
                        Log.v("Main Act", bundle.get("email").toString())

                        val frg = ProfileFragment()
                        frg.arguments = bundle
                        // supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, frg).commit()
                        // supportFragmentManager.setFragmentResult("requestKey", bundle)

                        navController.navigate(R.id.profileFragment, bundle)
                    }

                }
            }
            true
        }

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean{
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.nav_search -> {
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController = navHostFragment.navController
                navController.navigate(R.id.searchFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Notification when user opens app again
private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "My App Notification Channel"
        val descriptionText = "My App Notification Description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
}