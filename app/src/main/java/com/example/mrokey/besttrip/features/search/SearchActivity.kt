package com.example.mrokey.besttrip.features.search


import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.example.mrokey.besttrip.R
import com.example.mrokey.besttrip.model.Example
import com.example.mrokey.besttrip.model.RetrofitMaps
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class SearchActivity : AppCompatActivity(),OnMapReadyCallback ,SearchContract.View {
     lateinit var origin: LatLng
     lateinit var dest: LatLng
     lateinit var MarkerPoints: ArrayList<LatLng>
     lateinit var btnDriving: Button
     lateinit var btnWalking: Button
     var line: Polyline? = null
    private lateinit var map: GoogleMap
    private lateinit var myLocationCheckbox: CheckBox
    private lateinit var presenter: SearchContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        presenter = SearchPresenter(this)
        btnWalking=findViewById(R.id.btnWalking)
        btnDriving=findViewById(R.id.btnDriving)
        MarkerPoints = ArrayList()
        myLocationCheckbox = findViewById(R.id.myLocationCheckbox)
        val mapFragment : SupportMapFragment?=supportFragmentManager.findFragmentById(R.id.map)
                as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        val autocompleteFragment = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                map.clear()
                map.addMarker(MarkerOptions().position(place.latLng).title(place.name.toString()))
                map.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 12.0f))
            }
            override fun onError(status: Status) {
                Log.d("abc", "chinh")
            }
        })
        btnDriving.setOnClickListener(View.OnClickListener {
            build_retrofit_and_get_response("driving")
        })
        btnWalking.setOnClickListener(View.OnClickListener {
            build_retrofit_and_get_response("walking")
        })

    }
    override fun setPresenter(presenter: SearchContract.Presenter) {
        this.presenter = presenter
    }

    override fun showAnnounce(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return
        val sydney = LatLng(10.835307, 106.687726)
        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        map.animateCamera(CameraUpdateFactory.zoomTo(11f))
        if (myLocationCheckbox.isChecked) presenter.enableMyLocation(map,this)
        myLocationCheckbox.setOnClickListener {
            if (!myLocationCheckbox.isChecked) {
                map.isMyLocationEnabled = false
            } else {
                presenter.enableMyLocation(map,this)
            }
        }

        map.setOnMapClickListener(GoogleMap.OnMapClickListener { point ->
            if (MarkerPoints.size > 1) {
                map.clear()
                MarkerPoints.clear()
            }
            MarkerPoints.add(point)
            //Creating MarkerOptions
            val options = MarkerOptions()
            //Setting the piosition of the marker
            options.position(point)
            if (MarkerPoints.size == 1) {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            } else if (MarkerPoints.size == 2) {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            }

            //Add new marker to the Google Map Android API V2
            map.addMarker(options)
            //Checks ,whether start and end locations are captured
            if (MarkerPoints.size >= 2) {
                origin = MarkerPoints[0]
                Toast.makeText(this@SearchActivity, MarkerPoints[0].latitude.toString(), Toast.LENGTH_SHORT).show()
                dest = MarkerPoints[1]
            }
        })

    }


    private fun build_retrofit_and_get_response(type: String) {

        val url = "https://maps.googleapis.com/maps/"

        val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create<RetrofitMaps>(RetrofitMaps::class.java!!)

        val call = service.getDistanceDuration("metric", origin.latitude.toString() + ", " + origin.longitude, dest.latitude.toString() + ", " + dest.longitude, type)
        call.enqueue(object : Callback<Example> {
            override fun onResponse(call: Call<Example>, response: Response<Example>) {
                Log.d("abc", "t")
                try {
                    //Remove previous line from map
                    if (line != null) {
                        line!!.remove()
                    }
                    // This loop will go through all the results and add marker on each location.
                    for (i in 0 until response.body()!!.getRoutes().size) {
                        val distance = response.body()!!.getRoutes().get(i).getLegs().get(i).getDistance()!!.getText()
                        val time = response.body()!!.getRoutes().get(i).getLegs().get(i).getDuration()!!.getText()
                        Toast.makeText(this@SearchActivity, distance + time, Toast.LENGTH_SHORT).show()
                        val encodedString = response.body()!!.getRoutes().get(0).getOverviewPolyline()!!.getPoints()
                        val list = decodePoly(encodedString!!)
                        line = map.addPolyline(PolylineOptions()
                                .addAll(list)
                                .width(8f)
                                .color(Color.RED)
                                .geodesic(true)
                        )
                    }
                } catch (e: Exception) {
                    Log.d("onResponse", "There is an error")
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<Example>, t: Throwable) {
                Log.d("abc", "f")
            }
        })

    }

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5,
                    lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }
}
