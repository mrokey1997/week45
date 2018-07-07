package com.example.mrokey.besttrip.features.authentication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.mrokey.besttrip.R
import com.example.mrokey.besttrip.features.authentication.adapter.ViewPagerAdapter
import com.example.mrokey.besttrip.features.authentication.signin.SignInFragment
import com.example.mrokey.besttrip.features.authentication.signup.SignUpFragment
import kotlinx.android.synthetic.main.activity_authentication.*

class AuthenticationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        setupViewPager()
        tab_layout.setupWithViewPager(view_pager)
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(SignInFragment(), "Sign in")
        adapter.addFragment(SignUpFragment(), "Sign up")
        view_pager.adapter = adapter
        view_pager.offscreenPageLimit = 2
    }
}