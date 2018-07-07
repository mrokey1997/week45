package com.example.mrokey.besttrip.features.authentication.signin
import android.content.ContentValues
import android.text.TextUtils
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
class SignInPresenter(internal var view: SignInContract.View) : SignInContract.Presenter {
    var myRef: DatabaseReference? = null
    var mAuth: FirebaseAuth? = null
    init {
        view.setPresenter(this)
        mAuth = FirebaseAuth.getInstance()
        myRef= FirebaseDatabase.getInstance().reference
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
                            if(mAuth!!.currentUser!!.isEmailVerified)
                                view.getSuccess()
                            else
                                view.showError("The account has not been verified.")
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
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth?.signInWithCredential(credential)?.addOnCompleteListener{ task ->
                    val message: String
                    if (task.isSuccessful) {
                        view.getSuccess()
                        message = "success AuthWithGoogle"
                        view.showError(message)
                    } else {
                        message = "fail AuthWithGoogle"
                        view.showError(message)
                    }
                }
    }

}