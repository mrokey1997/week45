package com.example.mrokey.besttrip.features.authentication.signup

interface SignUpContract {
    interface View {
        fun setPresenter(presenter: Presenter)

        fun showLoading(isShow: Boolean)

        fun showNotification(message: String)

        fun getAccount()

    }
    interface Presenter{

        fun checkAccount(name: String, email:String, password: String)
    }
}