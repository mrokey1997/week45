package com.example.mrokey.besttrip.welcome

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.mrokey.besttrip.home.HomeActivity
import com.example.mrokey.besttrip.R
import com.example.mrokey.besttrip.features.authentication.AuthenticationActivity


class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        supportActionBar?.hide()
        val thread = Thread(Runnable {
            try {
                Thread.sleep(3000)
            } catch (e: Exception) {

            } finally {
                startActivity(Intent(this, AuthenticationActivity::class.java))
            }
        })
        thread.start()
    }
}
