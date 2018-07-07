package com.example.mrokey.besttrip.features.authentication.forgot


interface ForgotPasswordContract {

    interface View {
        fun setPresenter(presenter: Presenter)

        fun showLoading(isShow: Boolean)

        fun showAnnounce(message: String)

        fun getMail()
    }

    interface Presenter{
        fun checkMail(email:String)
    }
}