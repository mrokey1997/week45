package com.example.mrokey.besttrip.home

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.mrokey.besttrip.R
import com.example.mrokey.besttrip.callbacks.GetUserCallback
import com.example.mrokey.besttrip.entities.User
import com.example.mrokey.besttrip.features.authentication.AuthenticationActivity
import com.example.mrokey.besttrip.features.search.SearchActivity
import com.example.mrokey.besttrip.requests.UserRequest
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, GetUserCallback.IGetUserResponse {
    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mAuth = FirebaseAuth.getInstance()

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

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
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
                Toast.makeText(this@HomeActivity, "taxi", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_logout -> {
                mAuth?.signOut()
                startActivity(Intent(this, AuthenticationActivity::class.java))
                Toast.makeText(this@HomeActivity, "logout", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@HomeActivity, mAuth?.currentUser.toString(), Toast.LENGTH_SHORT).show()
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

    override fun onCompleted(user: User) {
        tv_name.text = user.name
        Glide.with(this)
                .load(user.picture)
                .into(img_avatar)
        nav_view.tv_name.text = user.name
        Glide.with(this)
                .load(user.picture)
                .into(nav_view.img_avatar)
    }
}
