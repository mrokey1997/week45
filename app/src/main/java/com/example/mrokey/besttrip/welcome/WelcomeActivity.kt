package com.example.mrokey.besttrip.welcome

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import com.example.mrokey.besttrip.home.HomeActivity
import com.example.mrokey.besttrip.R
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase




class WelcomeActivity : AppCompatActivity() {
   // var database = FirebaseDatabase.getInstance()
  //  var myRef = database.getReference()
  //  var dataRef = myRef.child("trip")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
//        myRef.push().setValue("1")
//        supportActionBar?.hide()
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
