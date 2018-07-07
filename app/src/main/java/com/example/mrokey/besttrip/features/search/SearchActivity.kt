package com.example.mrokey.besttrip.features.search

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.example.mrokey.besttrip.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class SearchActivity : AppCompatActivity(), LocationListener {
    override fun onLocationChanged(p0: Location?) {
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }

    private val MYTAG = "MYTAG"

    // Mã yêu cầu uhỏi người dùng cho phép xem vị trí hiện tại của họ (***).
    // Giá trị mã 8bit (value < 256).
    val REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100
    private lateinit var myMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        // Tạo Progress Bar
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap -> onMyMapReady(googleMap) }
    }

    private fun onMyMapReady(googleMap: GoogleMap) {

        // Lấy đối tượng Google Map ra:
        myMap = googleMap

        // Thiết lập sự kiện đã tải Map thành công
        myMap.setOnMapLoadedCallback {
            // Đã tải thành công thì tắt Dialog Progress đi
            // Hiển thị vị trí người dùng.

            askPermissionsAndShowMyLocation()
        }
        myMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        myMap.uiSettings.isZoomControlsEnabled = true
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        myMap.isMyLocationEnabled = true
    }

    private fun askPermissionsAndShowMyLocation() {


        // Với API >= 23, bạn phải hỏi người dùng cho phép xem vị trí của họ.
        if (Build.VERSION.SDK_INT >= 21) {
            val accessCoarsePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            val accessFinePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)


            if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED || accessFinePermission != PackageManager.PERMISSION_GRANTED) {

                // Các quyền cần người dùng cho phép.
                val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

                // Hiển thị một Dialog hỏi người dùng cho phép các quyền trên.
                ActivityCompat.requestPermissions(this, permissions,
                        REQUEST_ID_ACCESS_COURSE_FINE_LOCATION)

                //   return;
            }
        }

        // Hiển thị vị trí hiện thời trên bản đồ.
        this.showMyLocation()
    }


    // Khi người dùng trả lời yêu cầu cấp quyền (cho phép hoặc từ chối).
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //
        when (requestCode) {
            REQUEST_ID_ACCESS_COURSE_FINE_LOCATION -> {


                // Chú ý: Nếu yêu cầu bị bỏ qua, mảng kết quả là rỗng.
                if (grantResults.size > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show()

                    // Hiển thị vị trí hiện thời trên bản đồ.
                    this.showMyLocation()
                } else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show()
                }//                if (grantResults.length > 0
                //                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //                    mM = true;
                //                }
                // Hủy bỏ hoặc từ chối.
            }
        }
    }

    // Tìm một nhà cung cấp vị trị hiện thời đang được mở.
    private fun getEnabledLocationProvider(): String? {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Tiêu chí để tìm một nhà cung cấp vị trí.
        val criteria = Criteria()

        // Tìm một nhà cung vị trí hiện thời tốt nhất theo tiêu chí trên.
        // ==> "gps", "network",...
        val bestProvider = locationManager.getBestProvider(criteria, true)

        val enabled = locationManager.isProviderEnabled(bestProvider)

        if (!enabled) {
            Toast.makeText(this, "No location provider enabled!", Toast.LENGTH_LONG).show()
            Log.i(MYTAG, "No location provider enabled!")
            return null
        }
        return bestProvider
    }

    // Chỉ gọi phương thức này khi đã có quyền xem vị trí người dùng.
    private fun showMyLocation() {

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationProvider = this.getEnabledLocationProvider() ?: return

// Millisecond
        val MIN_TIME_BW_UPDATES: Long = 1000
        // Met
        val MIN_DISTANCE_CHANGE_FOR_UPDATES = 1f

        val myLocation: Location?
        try {

            // Đoạn code nay cần người dùng cho phép (Hỏi ở trên ***).
            locationManager.requestLocationUpdates(
                    locationProvider,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this as LocationListener)

            // Lấy ra vị trí.
            myLocation = locationManager
                    .getLastKnownLocation(locationProvider)
        } catch (e: SecurityException) {
            Toast.makeText(this, "Show My Location Error: " + e.message, Toast.LENGTH_LONG).show()
            Log.e(MYTAG, "Show My Location Error:" + e.message)
            e.printStackTrace()
            return
        }
        // Với Android API >= 23 phải catch SecurityException.

        if (myLocation != null) {

            val latLng = LatLng(myLocation.latitude, myLocation.longitude)
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))

            val cameraPosition = CameraPosition.Builder()
                    .target(latLng)             // Sets the center of the map to location user
                    .zoom(15f)                   // Sets the zoom
                    .bearing(90f)                // Sets the orientation of the camera to east
                    .tilt(40f)                   // Sets the tilt of the camera to 30 degrees
                    .build()                   // Creates a CameraPosition from the builder
            myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            // Thêm Marker cho Map:
            val option = MarkerOptions()
            option.title("My Location")
            option.snippet("....")
            option.position(latLng)
            val currentMarker = myMap.addMarker(option)
            currentMarker.showInfoWindow()
        } else {
            Toast.makeText(this, "Location not found!", Toast.LENGTH_LONG).show()
            Log.i(MYTAG, "Location not found")
        }
    }
}
