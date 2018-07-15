package com.example.mrokey.besttrip.company

import com.example.mrokey.besttrip.entities.Company
import com.google.firebase.database.DatabaseError

interface CompanyContract {
    interface View {
        fun setPresenter(presenter: Presenter)

        fun onDataChange(companies: ArrayList<Company>)

        fun onCancelled(p0: DatabaseError)
    }

    interface Presenter {
        fun getDatabase(listener: View)
    }
}