package com.example.mrokey.besttrip.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Company(
        val name: String,
        val address: String,
        val phone: String,
        val vehicles: ArrayList<Vehicle>,
        val wait_time: Long,
        val logo: String
)

@Parcelize
data class Vehicle(
        val name: String = "",
        val number_seat: Long = 0,
        val _1km: Long = 0,
        val over_1km: Double = 0.0,
        val over_30km: Double = 0.0
): Parcelable
