package com.example.mrokey.besttrip.detail.recommend

import android.util.Log
import com.example.mrokey.besttrip.entities.Taxi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RecommendDetailPresenter(internal var view: RecommendDetailContract.View): RecommendDetailContract.Presenter{
    var myRef: DatabaseReference? = null
    var mAuth: FirebaseAuth? = null
    init {
        view.setPresenter(this)
        myRef= FirebaseDatabase.getInstance().reference.child("user")
        mAuth = FirebaseAuth.getInstance()
    }
    override fun storeInfo(taxi: Taxi) {
        myRef?.child(mAuth?.currentUser?.uid.toString())?.child("recent_trip")?.setValue(taxi)
    }
}