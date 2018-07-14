package com.example.mrokey.besttrip.features.search


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.example.mrokey.besttrip.R
import com.example.mrokey.besttrip.features.direction.DirectionActivity
import com.example.mrokey.besttrip.home.HomeActivity
import com.example.mrokey.besttrip.model.Example
import com.example.mrokey.besttrip.recommend.RecommendActivity
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_search.*
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Response
import java.util.ArrayList

class SearchActivity : AppCompatActivity(),OnMapReadyCallback, EasyPermissions.PermissionCallbacks,SearchContract.View {
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }
    lateinit var bottomSheetBehavior:BottomSheetBehavior<View>
    lateinit var origin: LatLng
    lateinit var dest: LatLng
    lateinit var MarkerPoints: ArrayList<LatLng>
    lateinit var markerpoint: ArrayList<LatLng>
    var start_location:String?=null
    var end_location:String?=null
    var line: Polyline? = null
    var type:String?=null
    var distance:String?=null
    private lateinit var map: GoogleMap
    private lateinit var myLocationCheckbox: CheckBox
    private lateinit var presenter: SearchContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        presenter = SearchPresenter(this)
        MarkerPoints = ArrayList()
        markerpoint= ArrayList()
      //  myLocationCheckbox = findViewById(R.id.myLocationCheckbox)
        bottomSheetBehavior= BottomSheetBehavior.from(bottom_sheet)
        bottomSheetBehavior.isHideable=true
        bottomSheetBehavior.state=BottomSheetBehavior.STATE_HIDDEN
//        btnTest.setOnClickListener {
//            if(bottomSheetBehavior.state==BottomSheetBehavior.STATE_HIDDEN){
//                bottomSheetBehavior.state=BottomSheetBehavior.STATE_EXPANDED
//            }
//            else bottomSheetBehavior.state=BottomSheetBehavior.STATE_HIDDEN
//        }
        val bundle = intent.extras
        if(bundle!=null){
            type=bundle.getString("type")
        }
        if(type!=null){
//           var start_latitude:Double=bundle.getDouble("start_latitude")
            start_location=bundle.getString("start_location")
            end_location=bundle.getString("end_location")
            markerpoint.add(LatLng(bundle.getDouble("start_latitude"),bundle.getDouble("start_longitude")))
            markerpoint.add(LatLng(bundle.getDouble("end_latitude"),bundle.getDouble("end_longitude")))
            bottomSheetBehavior.state=BottomSheetBehavior.STATE_EXPANDED
            txt_start.setText(start_location)
            txt_end.setText(end_location)
        }
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
//        btnDriving.setOnClickListener(View.OnClickListener {
//            if(MarkerPoints.size==2){
//                presenter.getDataFromMap( origin.latitude.toString(), origin.longitude.toString(), dest.latitude.toString() ,dest.longitude.toString(), "driving")
//            }
//        })
//        btnWalking.setOnClickListener(View.OnClickListener {
//            if(MarkerPoints.size==2){
//                presenter.getDataFromMap( origin.latitude.toString() , origin.longitude.toString(), dest.latitude.toString() , dest.longitude.toString(), "walking")
//            }
//        })
            ic_direction.setOnClickListener(View.OnClickListener {
                startActivity(Intent(this,DirectionActivity::class.java))

            })
        img_start.setOnClickListener {
            if(type!=null){
                val intent=Intent(this,RecommendActivity::class.java)
                val mBundle = Bundle()
                mBundle.putString("distance", distance)
                mBundle.putString("start_location",start_location)
                mBundle.putString("end_location",end_location)
                intent.putExtras(mBundle)
                startActivity(intent)
            }
        }
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
        enableMyLocation()
        if(markerpoint.size>0){
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
//        map.setOnMapClickListener(GoogleMap.OnMapClickListener { point ->
//            if (MarkerPoints.size > 1) {
//                map.clear()
//                MarkerPoints.clear()
//            }
//            MarkerPoints.add(point)
//            //Creating MarkerOptions
//            val options = MarkerOptions()
//            //Setting the piosition of the marker
//            options.position(point)
//            if (MarkerPoints.size == 1) {
//                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//            } else if (MarkerPoints.size == 2) {
//                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
//            }
//
//            //Add new marker to the Google Map Android API V2
//            map.addMarker(options)
//            //Checks ,whether start and end locations are captured
//            if (MarkerPoints.size >= 2) {
//                origin = MarkerPoints[0]
//                Toast.makeText(this@SearchActivity, MarkerPoints[0].latitude.toString(), Toast.LENGTH_SHORT).show()
//                dest = MarkerPoints[1]
//            }
//        })

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
    @SuppressLint("MissingPermission")
    fun enableMyLocation() {
        val LOCATION_PERMISSION_REQUEST_CODE = 1
        if(presenter.isGPSEnabled(this)==false){
            Toast.makeText(this,"Vui lòng bậc GPS",Toast.LENGTH_SHORT).show()
        }
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

        if (EasyPermissions.hasPermissions(this, *permissions)) {
            map.isMyLocationEnabled = true


        } else {
            // if permissions are not currently granted, request permissions
            EasyPermissions.requestPermissions(this,
                    "Access to the location service is required to demonstrate the",
                    LOCATION_PERMISSION_REQUEST_CODE, *permissions)
        }
    }

}
