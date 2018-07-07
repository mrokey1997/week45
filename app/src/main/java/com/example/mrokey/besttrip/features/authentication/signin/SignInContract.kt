package com.example.mrokey.besttrip.features.authentication.signin

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface SignInContract {
    interface View {
        fun setPresenter(presenter: Presenter)

        fun showLoading(isShow: Boolean)

        fun showError(message: String)

        fun getAccount()

        fun getSuccess()

    }
    interface Presenter{
        fun checkAccount(email:String, password: String)

        fun authWithGoogle(acct: GoogleSignInAccount)
    }
}