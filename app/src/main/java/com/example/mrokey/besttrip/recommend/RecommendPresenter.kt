package com.example.mrokey.besttrip.recommend

import android.util.Log
import com.example.mrokey.besttrip.entities.Taxi
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
        myRef= FirebaseDatabase.getInstance().reference.child("trip").child("company")
    }
    override fun getData() {
        myRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var i = 0
                while (i < dataSnapshot.childrenCount)
                {
                    val phone = dataSnapshot.child(i.toString()).child("phone").value.toString()
                    val company = dataSnapshot.child(i.toString()).child("name").value.toString()
                    val child = dataSnapshot.child(i.toString()).child("vehicle")
                    var j = 0
                    while(j<child.childrenCount) {
                        val name = child.child(j.toString()).child("name").value.toString()
                        val price1km =  child.child(j.toString()).child("_1km").value.toString()
                        val priceOver1km =  child.child(j.toString()).child("over_1km").value.toString()
                        val priceOver30km =  child.child(j.toString()).child("over_30km").value.toString()
                        var price = 0F
                        var FAR = 100F
                        if(FAR > 30.0) {
                            price += ((FAR - 30.0) * priceOver30km.toFloat()).toFloat()
                            FAR = 30F
                        }
                        if (FAR > 1) {
                            price += ((FAR - 1) * priceOver1km.toFloat())
                            FAR = 1F
                        }
                        if( FAR > 0) price += FAR * price1km.toFloat()
                        val priceFormat = PriceFormat.priceFormat(price.toInt()) + ".000 VND"
                        when (child.child(j.toString()).child("number_seat").value.toString()) {
                            "4" -> taxiFourSeats.add(Taxi(company, name, phone, priceFormat))
                            "5" -> taxiFiveSeats.add(Taxi(company, name, phone, priceFormat))
                            "7" -> taxiSevenSeats.add(Taxi(company, name, phone, priceFormat))
                            "8" -> taxiEightSeats.add(Taxi(company, name, phone, priceFormat))
                        }
                        j += 1
                    }
                    i += 1
                }
                view.setView(taxiFourSeats,taxiFiveSeats,taxiSevenSeats,taxiEightSeats)
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