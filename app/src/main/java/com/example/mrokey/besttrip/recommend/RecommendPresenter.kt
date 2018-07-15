package com.example.mrokey.besttrip.recommend

import android.os.Looper
import com.example.mrokey.besttrip.entities.Taxi
import com.example.mrokey.besttrip.entities.Vehicle
import com.example.mrokey.besttrip.util.PriceFormat
import com.google.firebase.database.*

class RecommendPresenter(internal var view: RecommendContract.View): RecommendContract.Presenter{
    val taxiFourSeats = ArrayList<Taxi>()
    val taxiFiveSeats = ArrayList<Taxi>()
    val taxiSevenSeats = ArrayList<Taxi>()
    val taxiEightSeats = ArrayList<Taxi>()
    private var size4seats: Int = 2
    private var size5seats: Int = 2
    private var size7seats: Int = 2
    private var size8seats: Int = 2
    var myRef: DatabaseReference? = null
    init {
        view.setPresenter(this)
        myRef= FirebaseDatabase.getInstance().reference.child("company")
    }
    override fun getData(start: String, end: String, distance: Float) {
        view.showLoading(true)
        myRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var startToEnd = distance
                Thread{
                    var i = 0
                    while (i < dataSnapshot.childrenCount)
                    {
                        val data = dataSnapshot.child(i.toString())
                        val phone = data.child("phone").value.toString()
                        val company = data.child("name").value.toString()
                        val address = data.child("address").value.toString()
                        val logo = data.child("logo").value.toString()
                        val child = data.child("vehicle")
                        var j = 0

                        while(j<child.childrenCount) {
                            val vehicle = child.child(j.toString()).getValue<Vehicle>(Vehicle::class.java)
                            if (vehicle!=null) {
                                val price1km = vehicle._1km.toFloat()
                                val priceOver1km = vehicle.over_1km.toFloat()
                                val priceOver30km = vehicle.over_30km.toFloat()
                                var price = 0F
                                if(startToEnd<1){
                                    price = price1km
                                }
                                else {
                                    if (startToEnd > 30.0) {
                                        price += ((startToEnd - 30.0) * priceOver30km).toFloat()
                                        startToEnd = 30F
                                    }
                                    //if (startToEnd > 1) {
                                    price += ((startToEnd - 1) * priceOver1km)
                                    startToEnd = 1F
                                    //}
                                    price += startToEnd * price1km
                                }
                                val priceFormat = PriceFormat.priceFormat(price) + " VND"
                                when (vehicle.number_seat.toString()) {
                                    "4" -> taxiFourSeats.add(Taxi(start,end,company, phone, priceFormat,distance, address, logo, vehicle))
                                    "5" -> taxiFiveSeats.add(Taxi(start,end,company, phone, priceFormat,distance, address, logo, vehicle))
                                    "7" -> taxiSevenSeats.add(Taxi(start,end,company, phone, priceFormat,distance, address, logo, vehicle))
                                    "8" -> taxiEightSeats.add(Taxi(start,end,company, phone, priceFormat,distance, address, logo, vehicle))
                                }
                                startToEnd = distance
                            }
                            j += 1
                        }
                        i += 1
                    }
                   val handler = android.os.Handler(Looper.getMainLooper())
                    handler.post {
                        view.showLoading(false)
                        view.setView(taxiFourSeats,taxiFiveSeats,taxiSevenSeats,taxiEightSeats)}

                }.start()

            }
            override fun onCancelled(databaseError: DatabaseError) {
                view.showError("The read failed: " + databaseError.code)
            }
        })
    }
    override fun getMore(seat: Int) {
        when(seat){
            4 -> {
                size4seats = if(size4seats == 2) taxiFourSeats.size
                else 2
                view.loadMore(seat,taxiFourSeats,size4seats)
            }
            5 -> {
                size5seats = if(size5seats == 2) taxiFiveSeats.size
                else 2
                view.loadMore(seat,taxiFiveSeats,size5seats)
            }
            7 -> {
                size7seats = if(size7seats == 2) taxiSevenSeats.size
                else 2
                view.loadMore(seat,taxiSevenSeats,size7seats)
            }
            8 -> {
                size8seats = if(size8seats == 2) taxiEightSeats.size
                else 2
                view.loadMore(seat,taxiEightSeats,size8seats)
            }
        }
    }
}