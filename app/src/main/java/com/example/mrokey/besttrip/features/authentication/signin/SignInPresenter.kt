package com.example.mrokey.besttrip.features.authentication.signin

import android.content.ContentValues
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignInAccount


class SignInPresenter(internal var view: SignInContract.View, val callbackManager: CallbackManager) : SignInContract.Presenter {

    private var myRef: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null

    init {
        view.setPresenter(this)
        mAuth = FirebaseAuth.getInstance()
        myRef= FirebaseDatabase.getInstance().reference
    }

    override fun currentAccount() {
        if(mAuth?.currentUser!=null)
            view.getSuccess()
    }
    override fun checkAccount(email: String, password: String) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            view.showLoading(true)
            mAuth!!.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener{ task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with signed-in user's information
                            Log.d(ContentValues.TAG, "signInWithEmail:success")
                            view.showLoading(false)
                            //if(mAuth!!.currentUser!!.isEmailVerified)
                            view.getSuccess()
//                           else
//                                view.showError("The account has not been verified.")
                        } else {
                            view.showLoading(false)
                            // If sign in fails, display a message to the user.
                            Log.e(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                            view.showError("Authentication failed.")
                        }
                    }
        } else {
            view.showError("Enter all details")
        }
    }
    //SignIn with Google mail
    override fun authWithGoogle(acct: GoogleSignInAccount) {
        view.showLoading(true)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth?.signInWithCredential(credential)?.addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        Thread{
                            val userId = mAuth?.currentUser?.uid
                            val email = mAuth?.currentUser?.email
                            val name = mAuth?.currentUser?.displayName
                            val url = mAuth?.currentUser?.photoUrl.toString()
                            val currentUserDb = myRef?.child("user")?.child(userId?: return@Thread)
                            currentUserDb?.child("name")?.setValue(name)
                            currentUserDb?.child("email")?.setValue(email)
                            currentUserDb?.child("uid")?.setValue(userId)
                            currentUserDb?.child("url")?.setValue(url)
                            val handler = android.os.Handler(Looper.getMainLooper())
                            handler.post({
                                view.getSuccess()
                                view.showError("success AuthWithGoogle")
                                view.showLoading(false)
                            })
                        }.start()
                    } else {
                        view.showError("fail AuthWithGoogle")
                    }
                }
    }

    override fun authWithFacebook() {
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                view.onSuccessLoginFacebook(result)
            }
            override fun onCancel() {
            }
            override fun onError(error: FacebookException?) {
            }
        })
    }

}