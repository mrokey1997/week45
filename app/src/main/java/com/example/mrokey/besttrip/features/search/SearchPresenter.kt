package com.example.mrokey.besttrip.features.search

import android.content.Context
import android.location.LocationManager
import com.example.mrokey.besttrip.model.Example
import com.example.mrokey.besttrip.api.RetrofitMaps
import com.google.android.gms.maps.model.LatLng
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList
import javax.security.auth.callback.Callback

class SearchPresenter(internal var view: SearchContract.View) : SearchContract.Presenter , EasyPermissions.PermissionCallbacks {
    override fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5,
                    lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }
    override fun getDataFromMap(start_latitude: String, start_longitude: String, end_latitude: String, end_longitude: String, type: String) {
        val url = "https://maps.googleapis.com/maps/"

        val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create<RetrofitMaps>(RetrofitMaps::class.java!!)
       var call= service.getDistanceDuration("metric", "$start_latitude,$start_longitude"
        , "$end_latitude,$end_longitude",type)
        call.enqueue(object : Callback, retrofit2.Callback<Example> {

            override fun onResponse(call: Call<Example>?, response: Response<Example>?) {
                view.onGetStatusesSuccess(response)
            }
            override fun onFailure(call: Call<Example>?, t: Throwable?) {

            }
        })
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

    }
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onRequestPermissionsResult(p0: Int, p1: Array<out String>, p2: IntArray) {

    }

    init {
        view.setPresenter(this)
    }
    override fun isGPSEnabled(mContext: Context): Boolean {
        val lm = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}