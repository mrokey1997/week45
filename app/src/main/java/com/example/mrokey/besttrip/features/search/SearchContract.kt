package com.example.mrokey.besttrip.features.search

import android.content.Context
import com.google.android.gms.maps.GoogleMap

interface SearchContract {
    interface View{
        fun setPresenter(presenter: Presenter)
        fun showAnnounce(message: String)
    }
    interface Presenter{
       fun enableMyLocation(map: GoogleMap ,mContext: Context )
        fun isGPSEnabled(mContext: Context): Boolean
    }
}