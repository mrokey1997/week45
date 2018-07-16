package com.example.mrokey.besttrip.features.direction

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mrokey.besttrip.R
import com.example.mrokey.besttrip.features.search.SearchActivity
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import kotlinx.android.synthetic.main.activity_direction.*
 class DirectionActivity : AppCompatActivity() {
    var start_latitude: Double? =null
    var start_longitude:Double?=null
    var end_latitude: Double?=null
    var end_longitude:Double?=null
     var start_location:String?=null
     var end_location:String?=null
    var type:String="driving"
     var check:Boolean=false
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_driving -> {
              //  Toast.makeText(this,"home",Toast.LENGTH_SHORT).show()
                type="driving"
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_walking -> {
                type="walking"
             //   Toast.makeText(this,"b",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direction)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val place_start = fragmentManager.findFragmentById(R.id.place_start) as PlaceAutocompleteFragment
        place_start.setHint("Your location")
        val place_end = fragmentManager.findFragmentById(R.id.place_end) as PlaceAutocompleteFragment
        place_end.setHint("Choose destination")
        place_start.setOnPlaceSelectedListener(object:PlaceSelectionListener{
            override fun onPlaceSelected(place: Place?) {
                if (place != null) {
                   // Toast.makeText(this@DirectionActivity,place.latLng.latitude.toString()+place.latLng.longitude.toString(),Toast.LENGTH_SHORT).show()
                    check=true
                    start_latitude=place.latLng.latitude
                    start_longitude=place.latLng.longitude
                    start_location=place.name.toString()
                }
            }

            override fun onError(place: Status?) {
                Log.d("abc","place")
            }

        })
        place_end.setOnPlaceSelectedListener(object:PlaceSelectionListener{
            override fun onPlaceSelected(place: Place?) {
                //  var map:GoogleMap
                if (place != null) {
                //    Toast.makeText(this@DirectionActivity,place.latLng.latitude.toString()+place.latLng.longitude.toString(),Toast.LENGTH_SHORT).show()
                    end_latitude=place.latLng.latitude
                    end_longitude=place.latLng.longitude
                    end_location=place.name.toString()
                }
            }
            override fun onError(place: Status?) {
                Log.d("abc","place")
            }
        })
        img_back.setOnClickListener {
            if(end_latitude==null){
                Toast.makeText(this,"Please choose destination",Toast.LENGTH_SHORT).show()
            }else{
                val intent=Intent(this,SearchActivity::class.java)
                // var bundle:Bundle
                val mBundle = Bundle()
                mBundle.putString("type",type)
                mBundle.putBoolean("check",check)
                mBundle.putDouble("end_latitude",end_latitude!!)
                mBundle.putDouble("end_longitude",end_longitude!!)
                mBundle.putString("end_location",end_location);
                if(check==true){
                    mBundle.putDouble("start_latitude",start_latitude!!)
                    mBundle.putString("start_location",start_location);
                    mBundle.putDouble("start_longitude",start_longitude!!)
                }
                intent.putExtras(mBundle)
                startActivity(intent)
            }
        }
    }
}
