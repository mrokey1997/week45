package com.example.mrokey.besttrip.features.direction

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mrokey.besttrip.R
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity_direction.*

class DirectionActivity : AppCompatActivity() {
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                Toast.makeText(this,"home",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                Toast.makeText(this,"b",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direction)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        img_back.setOnClickListener(View.OnClickListener {
            finish()
        })
        val place_start = fragmentManager.findFragmentById(R.id.place_start) as PlaceAutocompleteFragment
        place_start.setHint("Chọn điểm khỏi hành")
        val place_end = fragmentManager.findFragmentById(R.id.place_end) as PlaceAutocompleteFragment
        place_end.setHint("Chọn điểm đến")
        place_start.setOnPlaceSelectedListener(object:PlaceSelectionListener{
            override fun onPlaceSelected(place: Place?) {
                if (place != null) {

                    Toast.makeText(this@DirectionActivity,place.latLng.latitude.toString()+place.latLng.longitude.toString(),Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this@DirectionActivity,place.latLng.latitude.toString()+place.latLng.longitude.toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(place: Status?) {
                Log.d("abc","place")
            }

        })
    }
}
