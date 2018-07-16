package com.example.mrokey.besttrip.detail.recommend

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mrokey.besttrip.R
import com.example.mrokey.besttrip.entities.Taxi
import com.example.mrokey.besttrip.util.PriceFormat
import kotlinx.android.synthetic.main.activity_recommend_detail.*
import android.content.Intent
import android.net.Uri


class RecommendDetailActivity: AppCompatActivity(),RecommendDetailContract.View{
    private var presenter: RecommendDetailContract.Presenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend_detail)
        presenter = RecommendDetailPresenter(this)
        val bundle = intent.getBundleExtra("myBundle")
        val taxi  = bundle.getParcelable("taxi") as Taxi
        setView(taxi)
        fab.setOnClickListener({
            presenter?.storeInfo(taxi)
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:${removeSpaceInString(taxi.phone)}")
            startActivity(callIntent)
        })
    }
    override fun setPresenter(presenter: RecommendDetailContract.Presenter) {
        this.presenter = presenter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.overridePendingTransition(0,R.anim.back_right)
    }
    @SuppressLint("SetTextI18n")
    override fun setView(taxi: Taxi) {
        setSupportActionBar(toolbarRecommendDetail)
        supportActionBar?.title = taxi.company
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbarRecommendDetail.setNavigationOnClickListener({
            onBackPressed()
            this.overridePendingTransition(0,R.anim.back_right)
        })
        startLocation.text = taxi.start
        endLocation.text = taxi.end
        car.text = taxi.vehicle.name
        seater.text = taxi.vehicle.number_seat.toString() + "seats"
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
    private fun removeSpaceInString(phone: String) : String {
        return phone.replace(" ", "")
    }
}

