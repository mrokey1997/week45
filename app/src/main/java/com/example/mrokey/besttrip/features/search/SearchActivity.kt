package com.example.mrokey.besttrip.features.search


import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import com.example.mrokey.besttrip.R
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions

class SearchActivity : AppCompatActivity(),OnMapReadyCallback ,SearchContract.View {
    private lateinit var presenter: SearchContract.Presenter
    override fun setPresenter(presenter: SearchContract.Presenter) {
        this.presenter = presenter
    }

    override fun showAnnounce(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    private lateinit var map: GoogleMap
    private lateinit var myLocationCheckbox: CheckBox
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return
        if (myLocationCheckbox.isChecked) presenter.enableMyLocation(map,this)
        myLocationCheckbox.setOnClickListener {
            if (!myLocationCheckbox.isChecked) {
                map.isMyLocationEnabled = false
            } else {
                presenter.enableMyLocation(map,this)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        presenter = SearchPresenter(this)
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
    }
}
