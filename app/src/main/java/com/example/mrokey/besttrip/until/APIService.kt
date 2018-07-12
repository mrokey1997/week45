package com.example.mrokey.besttrip.until

import com.example.mrokey.besttrip.model.RetrofitMaps
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APIService {
    var url ="https://maps.googleapis.com/maps/"
    private var instance: RetrofitMaps? = null
    fun getInstance(): RetrofitMaps? {
        if (instance == null) {
            synchronized(APIService::class.java) {
                if (instance == null) {
                    val httpClient = OkHttpClient.Builder()
                    httpClient.writeTimeout((15 * 60 * 1000).toLong(), TimeUnit.MILLISECONDS)
                    httpClient.readTimeout((60 * 1000).toLong(), TimeUnit.MILLISECONDS)
                    httpClient.connectTimeout((20 * 1000).toLong(), TimeUnit.MILLISECONDS)

                    val client = httpClient.build()

                    val retrofit = Retrofit.Builder()
                            .baseUrl(url)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(client)
                            .build()
                    instance = retrofit.create(RetrofitMaps::class.java!!)
                }
            }
        }
        return instance
    }
}