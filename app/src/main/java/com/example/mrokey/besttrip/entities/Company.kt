package com.example.mrokey.besttrip.entities

data class Company(
        val name: String,
        val address: String,
        val phone: String,
        val vehicles: ArrayList<Taxi>,
        val wait_time: Long,
        val logo: String
)


data class Taxi(
        val name: String,
        val number_seat: Long,
        val _1km: Long,
        val over_1km: Double,
        val over_31km: Double
)