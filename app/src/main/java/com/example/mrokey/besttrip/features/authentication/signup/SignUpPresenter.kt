
package com.example.mrokey.besttrip.features.authentication.signup
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.util.Log
class SignUpPresenter(internal var view: SignUpContract.View) : SignUpContract.Presenter {
    var myRef: DatabaseReference? = null
    var mAuth: FirebaseAuth? = null

    init {
        view.setPresenter(this)
        mAuth = FirebaseAuth.getInstance()
        myRef= FirebaseDatabase.getInstance().reference
    }

    override fun checkAccount(name:String, email: String, password: String) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            view.showLoading(true)
            mAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    view.showLoading(false)
                    if(password.length < 6){
                        view.showError("The password must be at least 6 characters long")
                    }
                    else {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("AAA", "createUserWithEmail:success")
                        val userId = mAuth?.currentUser!!.uid
                        //Verify Email
                        //verifyEmail()
                        //update user profile information
                        val currentUserDb = myRef?.child("user")?.child(userId)
                        currentUserDb?.child("name")?.setValue(name)
                        currentUserDb?.child("email")?.setValue(email)
                        currentUserDb?.child("url")?.setValue(null)
                        currentUserDb?.child("uid")?.setValue(userId)
                    }
                } else {
                    view.showLoading(false)
                    // If sign in fails, display a message to the user.
                    Log.w("BBB", "createUserWithEmail:failure", task.exception)
                    view.showError("Authentication failed.") //unreasonable or already exists
                }
            }
        }
        else {
            view.showError("Enter all details")
        }
    }
//    private fun verifyEmail() {
//        val mUser = mAuth?.currentUser
//        mUser!!.sendEmailVerification()
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        view.showError("Verification email sent to " + mUser.email)
//                    } else {
//                        Log.e("AAA", "sendEmailVerification", task.exception)
//                        view.showError("Failed to send verification email.")
//                    }
//                }
//    }
}