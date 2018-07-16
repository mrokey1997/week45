package com.example.mrokey.besttrip.home

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.mrokey.besttrip.R
import com.example.mrokey.besttrip.callbacks.GetUserCallback
import com.example.mrokey.besttrip.company.OverviewTaxiActivity
import com.example.mrokey.besttrip.entities.User
import com.example.mrokey.besttrip.features.authentication.AuthenticationActivity
import com.example.mrokey.besttrip.features.search.SearchActivity
import com.example.mrokey.besttrip.requests.UserRequest
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class HomeActivity : AppCompatActivity(),HomeContract.View, NavigationView.OnNavigationItemSelectedListener, GetUserCallback.IGetUserResponse {

    var mAuth: FirebaseAuth? = null
    private var presenter: HomeContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mAuth = FirebaseAuth.getInstance()
        presenter = HomePresenter(this)
        presenter?.getData()
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbarMenu, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        btnWhere.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

    }
    override fun setPresenter(presenter: HomeContract.Presenter) {
        this.presenter = presenter
    }

    override fun setView(name: String, email: String, url: String) {
        tv_name.text = name
        nav_view.tv_name.text = name
        nav_view.tv_email.text = email
        Glide.with(this)
                .load(url)
                .into(nav_view.img_avatar)
    }
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
            mAuth?.signOut()
        }
    }

    override fun onResume() {
        super.onResume()
        UserRequest.makeUserRequest(GetUserCallback(this@HomeActivity).getCallback())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
                Toast.makeText(this@HomeActivity, "home", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_setting -> {
                Toast.makeText(this@HomeActivity, "manage", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_taxi -> {
                startActivity(Intent(this@HomeActivity,OverviewTaxiActivity::class.java))
                Toast.makeText(this@HomeActivity, "taxi", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_logout -> {
                mAuth?.signOut()
                startActivity(Intent(this, AuthenticationActivity::class.java))
                Toast.makeText(this@HomeActivity, "logout", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_share -> {
                Toast.makeText(this@HomeActivity, "share", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_send -> {
                Toast.makeText(this@HomeActivity, "send", Toast.LENGTH_SHORT).show()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    @SuppressLint("SetTextI18n")
    override fun onCompleted(user: User) {
        val name = user.name
        tv_name.text = "$name!"
        nav_view.tv_name.text = name
        Glide.with(this)
                .load(user.picture)
                .into(nav_view.img_avatar)
    }
}
