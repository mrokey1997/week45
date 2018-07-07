package com.example.mrokey.besttrip

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.drawee.backends.pipeline.Fresco

class BestTripApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FacebookSdk.sdkInitialize(applicationContext)
        Fresco.initialize(this)
    }
}