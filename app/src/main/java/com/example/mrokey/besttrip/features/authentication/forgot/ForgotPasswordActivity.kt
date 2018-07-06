package com.example.mrokey.besttrip.features.authentication.forgot

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.mrokey.besttrip.R
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        setSupportActionBar(toolbarForgot)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
}