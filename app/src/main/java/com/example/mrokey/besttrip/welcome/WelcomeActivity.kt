package com.example.mrokey.besttrip.welcome

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.mrokey.besttrip.R
import com.example.mrokey.besttrip.features.search.SearchActivity


class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val thread = Thread(Runnable {
            try {
                Thread.sleep(1000)
            } catch (e: Exception) {

            } finally {
                startActivity(Intent(this, SearchActivity::class.java))
            }
        })
        thread.start()
    }
}
