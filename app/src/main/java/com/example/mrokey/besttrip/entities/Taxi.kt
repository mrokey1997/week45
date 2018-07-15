package com.example.mrokey.besttrip.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Taxi(
        val start: String,
        val end: String,
        val company: String,
        val phone: String,
        val price: String,
        val distance: Float,
        val address: String,
        val logo: String,
        val vehicle: Vehicle) : Parcelable