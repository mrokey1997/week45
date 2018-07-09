package com.example.mrokey.besttrip.features.search

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.LocationManager
import com.google.android.gms.maps.GoogleMap
import pub.devrel.easypermissions.EasyPermissions

class SearchPresenter(internal var view: SearchContract.View) : SearchContract.Presenter , EasyPermissions.PermissionCallbacks {
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

    @SuppressLint("MissingPermission")
    override fun enableMyLocation(map: GoogleMap, mContext: Context) {
         val LOCATION_PERMISSION_REQUEST_CODE = 1
        var message: String ="Vui lòng bậc gps"
       if(isGPSEnabled(mContext)==false){
           view.showAnnounce(message)
       }
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

        if (EasyPermissions.hasPermissions(mContext, *permissions)) {
            map.isMyLocationEnabled = true

        } else {
            // if permissions are not currently granted, request permissions
            EasyPermissions.requestPermissions(Activity(),
                    "Access to the location service is required to demonstrate the",
                    LOCATION_PERMISSION_REQUEST_CODE, *permissions)
        }
    }
}