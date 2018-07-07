package com.example.mrokey.besttrip.features.authentication.forgot

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.mrokey.besttrip.R
import com.example.mrokey.besttrip.features.authentication.signin.SignInPresenter
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity(),ForgotPasswordContract.View {
    private lateinit var presenter: ForgotPasswordContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        setSupportActionBar(toolbarForgot)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        presenter = ForgotPasswordPresenter(this)
        val btForgot = findViewById(R.id.btForgot) as Button
        btForgot.setOnClickListener {
            getMail()
        }
    }
    override fun setPresenter(presenter: ForgotPasswordContract.Presenter) {
        this.presenter = presenter
    }

    override fun showLoading(isShow: Boolean) {
        loadForgot.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun showAnnounce(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun getMail() {
        val email = edtEmail?.text.toString()
        presenter.checkMail(email)
    }
}