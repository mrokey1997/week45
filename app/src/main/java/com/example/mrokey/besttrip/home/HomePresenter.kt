package com.example.mrokey.besttrip.home

import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.logging.Handler

class HomePresenter(internal var view: HomeContract.View) : HomeContract.Presenter {
    private var myRef: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null

    init {
        view.setPresenter(this)
        mAuth = FirebaseAuth.getInstance()
        myRef = FirebaseDatabase.getInstance().reference.child("user")
    }

    override fun getData() {
        myRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Thread{
                    val email = dataSnapshot.child(mAuth?.currentUser?.uid.toString()).child("email").value.toString()
                    val name = dataSnapshot.child(mAuth?.currentUser?.uid.toString()).child("name").value.toString()
                    val url = dataSnapshot.child(mAuth?.currentUser?.uid.toString()).child("url").value.toString()
                    val handler = android.os.Handler(Looper.getMainLooper())
                    handler.post({
                        view.setView(name,email,url)
                    })
                }.start()
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}
