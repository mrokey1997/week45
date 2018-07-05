package com.example.mrokey.besttrip.welcome

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.mrokey.besttrip.home.HomeActivity
import com.example.mrokey.besttrip.R


class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        supportActionBar?.hide()
//        txtStart.setOnClickListener {
//            var intent = Intent(this, HomeActivity::class.java)
//           startActivity(intent)
//        }
        val thread = Thread(Runnable {
            try {
                Thread.sleep(3000)
            } catch (e: Exception) {

            } finally {
                startActivity(Intent(this, HomeActivity::class.java))
            }
        })
        thread.start()
    }
}
