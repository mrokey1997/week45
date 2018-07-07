package com.example.mrokey.besttrip.features.authentication.forgot

import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ForgotPasswordPresenter(internal var view: ForgotPasswordContract.View): ForgotPasswordContract.Presenter{
    var myRef: DatabaseReference? = null
    var mAuth: FirebaseAuth? = null
    init {
        view.setPresenter(this)
        mAuth = FirebaseAuth.getInstance()
        myRef= FirebaseDatabase.getInstance().reference
    }
    override fun checkMail(email: String) {
        var message: String
        if (!TextUtils.isEmpty(email)) {
            mAuth!!
                    .sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            message = "Email sent."
                            view.showAnnounce(message)
                        } else {
                            message = "No user found with this email."
                            view.showAnnounce(message)
                        }
                    }
        } else {
            message = "Enter Email"
            view.showAnnounce(message)
        }
    }

}