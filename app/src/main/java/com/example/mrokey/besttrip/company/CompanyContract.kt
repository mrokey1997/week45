package com.example.mrokey.besttrip.company

import com.example.mrokey.besttrip.entities.Company
import com.example.mrokey.besttrip.features.authentication.signin.SignInContract

interface CompanyContract {
    interface View {
        fun setPresenter(presenter: Presenter)

        fun onDataChange(companies: ArrayList<Company>)
    }

    interface Presenter {
        fun getDatabase(listener: View)
    }
}