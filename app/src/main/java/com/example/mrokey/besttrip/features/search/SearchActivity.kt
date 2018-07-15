package com.example.mrokey.besttrip.features.search


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mrokey.besttrip.R
import com.example.mrokey.besttrip.features.direction.DirectionActivity
import com.example.mrokey.besttrip.model.Example
import com.example.mrokey.besttrip.recommend.RecommendActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Response
import java.util.ArrayList


@Suppress("DEPRECATION")
class SearchActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener,SearchContract.View {

    private lateinit var presenter: SearchContract.Presenter
    lateinit var markerpoint: ArrayList<LatLng>
    lateinit var current_location:LatLng
    var start_location:String?=null
    var end_location:String?=null
    var check:Boolean=true
    var line: Polyline? = null
    var type:String?=null
    var distance:String?=null
    private var service: LocationManager? = null
    private var enabled: Boolean? = null
    private var mLocationRequest: LocationRequest? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLastLocation: Location? = null
    private var mCurrLocationMarker: Marker? = null
    private lateinit var map: GoogleMap
    private var REQUEST_LOCATION_CODE = 101
    lateinit var bottomSheetBehavior:BottomSheetBehavior<View>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        bottomSheetBehavior= BottomSheetBehavior.from(bottom_sheet)
        bottomSheetBehavior.isHideable=true
        bottomSheetBehavior.state=BottomSheetBehavior.STATE_HIDDEN
        presenter = SearchPresenter(this)
        markerpoint= ArrayList()
        service = this.getSystemService(LOCATION_SERVICE) as LocationManager
        enabled = service!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val autocompleteFragment = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                bottomSheetBehavior.state=BottomSheetBehavior.STATE_HIDDEN
                markerpoint.add(place.latLng)
                map.clear()
                map.addMarker(MarkerOptions().position(markerpoint.get(0)).title(place.name.toString()))
                map.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 12.0f))
            }
            override fun onError(status: Status) {
                Log.d("abc", "chinh")
            }
        })
        val bundle = intent.extras
        if(bundle!=null){
            type=bundle.getString("type")
        }
        if(type!=null){
//           var start_latitude:Double=bundle.getDouble("start_latitude")
            markerpoint.clear()
            check=bundle.getBoolean("check")
            if(check==true){
                start_location=bundle.getString("start_location")
                markerpoint.add(LatLng(bundle.getDouble("start_latitude"),bundle.getDouble("start_longitude")))
                txt_start.setText(start_location)
            }
            else{
               // markerpoint.add(current_location)
            }

            end_location=bundle.getString("end_location")
            markerpoint.add(LatLng(bundle.getDouble("end_latitude"),bundle.getDouble("end_longitude")))
            bottomSheetBehavior.state=BottomSheetBehavior.STATE_EXPANDED
            txt_end.setText(end_location)
        }
        ic_direction.setOnClickListener {
            startActivity(Intent(this,DirectionActivity::class.java))

        }
        img_start.setOnClickListener {
            if(type!=null){
                val intent=Intent(this,RecommendActivity::class.java)
                val mBundle = Bundle()
                mBundle.putString("distance", distance)
                if(check==true){
                    mBundle.putString("start_location",start_location)
                }else{
                    mBundle.putString("start_location","Vị trí của bạn")
                }
                mBundle.putString("end_location",end_location)
                intent.putExtras(mBundle)
                startActivity(intent)
            }
        }

    }
    override fun setPresenter(presenter: SearchContract.Presenter) {
        this.presenter=presenter
    }

    override fun showAnnounce(message: String) {
    }

    override fun onGetStatusesSuccess(data: Response<Example>?) {
        Log.d("abc", "t")
        try {
            //Remove previous line from map
            if (line != null) {
                line!!.remove()
            }
            // This loop will go through all the results and add marker on each location.
            if (data != null) {
                for (i in 0 until data.body()!!.getRoutes().size) {

                    distance = data.body()!!.getRoutes().get(i).getLegs().get(i).getDistance()!!.getText()
                    val time = data.body()!!.getRoutes().get(i).getLegs().get(i).getDuration()!!.getText()
                    txt_Time.setText(distance+" (" +time+")")
                    //  Toast.makeText(this@SearchActivity, distance + time, Toast.LENGTH_SHORT).show()
                    val encodedString = data.body()!!.getRoutes().get(0).getOverviewPolyline()!!.getPoints()
                    val list = presenter.decodePoly(encodedString!!)
                    line = map.addPolyline(PolylineOptions()
                            .addAll(list)
                            .width(8f)
                            .color(Color.BLUE)
                            .geodesic(true)
                    )
                }
            }
        } catch (e: Exception) {
            Log.d("onResponse", "There is an error")
            e.printStackTrace()
        }
    }
    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap!!
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.setPadding(0,200,0,0)

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient()
                map.isMyLocationEnabled = true
            } else {
                //Request Location Permission
                checkLocationPermission()
            }
        } else {
            buildGoogleApiClient()
            map.isMyLocationEnabled = true
        }
        if(markerpoint.size==2){
            map.clear()
            //Setting the piosition of the marker
            map.addMarker(MarkerOptions().position(markerpoint.get(0)).title(start_location))
            map.addMarker(MarkerOptions().position(markerpoint.get(0)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
            map.addMarker(MarkerOptions().position(markerpoint.get(1)).title(end_location))
            map.moveCamera(CameraUpdateFactory.newLatLng(markerpoint.get(0)))
            map.animateCamera(CameraUpdateFactory.zoomTo(15f))
            presenter.getDataFromMap(markerpoint.get(0).latitude.toString(),markerpoint.get(0).longitude.toString(),
                    markerpoint.get(1).latitude.toString(),  markerpoint.get(1).longitude.toString(),type!!)
            //Add new marker to the Google Map Android API V2

            //Checks ,whether start and end locations are captured
        }
    }

    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 1000
        mLocationRequest!!.fastestInterval = 1000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        // Check if enabled and if not send user to the GPS settings
        if (!enabled!!) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        // Check if permission is granted or not
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this)
        }
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onLocationChanged(location: Location?) {
        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }
        if(markerpoint.size==0){
            //Place current location marker
            current_location = LatLng(location!!.latitude, location.longitude)
            Log.d("abc",location.latitude.toString()+"  "+location.longitude.toString())
            val markerOptions = MarkerOptions()
            markerOptions.position(current_location)
            markerOptions.title("Current Position")
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            mCurrLocationMarker = map.addMarker(markerOptions)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(current_location, 15f))
        }
        else{
            current_location = LatLng(location!!.latitude, location.longitude)
            if(check==false)
            {
                markerpoint.add(current_location)
            }
            if(markerpoint.size==2){
                map.clear()
                //Setting the piosition of the marker
                map.addMarker(MarkerOptions().position(markerpoint.get(0)).title(start_location))
                map.addMarker(MarkerOptions().position(markerpoint.get(0)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
                map.addMarker(MarkerOptions().position(markerpoint.get(1)).title(end_location))
                map.moveCamera(CameraUpdateFactory.newLatLng(markerpoint.get(0)))
                map.animateCamera(CameraUpdateFactory.zoomTo(15f))
                presenter.getDataFromMap(markerpoint.get(0).latitude.toString(),markerpoint.get(0).longitude.toString(),
                        markerpoint.get(1).latitude.toString(),  markerpoint.get(1).longitude.toString(),type!!)
                //Add new marker to the Google Map Android API V2

                //Checks ,whether start and end locations are captured
            }
        }

    }

    @Synchronized
    fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

        mGoogleApiClient!!.connect()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_LOCATION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient()
                        }
                        map.isMyLocationEnabled = true
                    }
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK") { _, which ->
                            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_CODE)
                        }
                        .create()
                        .show()

            } else ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_CODE)
        }
    }
}
