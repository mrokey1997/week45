package com.example.mrokey.besttrip.features.search

import android.content.Context
import com.example.mrokey.besttrip.model.Example
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import retrofit2.Response

interface SearchContract {
    interface View{
        fun setPresenter(presenter: Presenter)
        fun showAnnounce(message: String)
        fun onGetStatusesSuccess(data: Response<Example>?)
    }
    interface Presenter{
        fun decodePoly(encoded: String): List<LatLng>
        fun enableMyLocation(map: GoogleMap ,mContext: Context )
        fun isGPSEnabled(mContext: Context): Boolean
        fun getDataFromMap(start_latitude:String,start_longitude:String,end_latitude:String,end_longitude:String,type:String)
    }
}