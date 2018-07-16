package com.example.mrokey.besttrip.home

interface HomeContract {
    interface View{
        fun setPresenter(presenter: Presenter)
        fun setView(name: String,email: String,url: String)
    }
    interface Presenter{
        fun getData()
    }
}