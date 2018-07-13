package com.example.mrokey.besttrip.RecommendDetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mrokey.besttrip.R
import com.example.mrokey.besttrip.entities.Taxi
import com.example.mrokey.besttrip.util.PriceFormat
import kotlinx.android.synthetic.main.activity_recommend_detail.*

class RecommendDetailActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend_detail)
        val bundle = intent.getBundleExtra("myBundle")
        val taxi  = bundle.getParcelable("taxi") as Taxi
        setView(taxi)
    }

    @SuppressLint("SetTextI18n")
    private fun setView(taxi: Taxi) {
        car.text = taxi.vehicle.name
        price.text = taxi.price
        distance.text = taxi.distance.toString() + " Km"
        startingFee.text = PriceFormat.priceFormat(taxi.vehicle._1km.toFloat()) + " VND"
        nextPrice.text = PriceFormat.priceFormat(taxi.vehicle.over_1km.toFloat()) + " VND"
        finalPrice.text = PriceFormat.priceFormat(taxi.vehicle.over_30km.toFloat()) + " VND"
        Glide.with(this).load(taxi.logo).into(img_logo)
    }
}