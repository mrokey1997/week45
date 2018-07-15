package com.example.mrokey.besttrip.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
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
        setSupportActionBar(toolbarRecommendDetail)
        supportActionBar?.title = taxi.company
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        startLocation.text = taxi.start
        endLocation.text = taxi.end
        car.text = taxi.vehicle.name + " (" + taxi.vehicle.number_seat.toString() + "seats)"
        price.text = taxi.price
        distance.text = taxi.distance.toString() + " Km"
        startingFee.text = PriceFormat.priceFormat(taxi.vehicle._1km.toFloat()) + " VND"
        nextPrice.text = PriceFormat.priceFormat(taxi.vehicle.over_1km.toFloat()) + " VND"
        finalPrice.text = PriceFormat.priceFormat(taxi.vehicle.over_30km.toFloat()) + " VND"
        Glide.with(this).load(taxi.logo).into(img_logo)
        address.text = "Address: " + taxi.address
        phone.text = "Phone: " + taxi.phone
        toolbarRecommendDetail.setOnClickListener({ finish() })
    }
}

