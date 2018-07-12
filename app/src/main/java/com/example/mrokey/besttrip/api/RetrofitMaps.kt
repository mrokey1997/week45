package com.example.mrokey.besttrip.api

import com.example.mrokey.besttrip.model.Example
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitMaps {
    @GET("api/directions/json?key=AIzaSyAM6hZm5eW11iNqdcUS4VNNbO-A1wsyZus")
    abstract fun getDistanceDuration(@Query("units") units: String, @Query("origin") origin: String, @Query("destination") destination: String, @Query("mode") mode: String): Call<Example>
}