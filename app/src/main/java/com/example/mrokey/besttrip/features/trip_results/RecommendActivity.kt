package com.example.mrokey.besttrip.features.trip_results

import android.os.Bundle
import com.example.mrokey.besttrip.R
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.LinearLayout
import com.example.mrokey.besttrip.entities.Taxi
import com.example.mrokey.besttrip.features.trip_results.adapter.SeatsAdapter
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase




class RecommendActivity: AppCompatActivity(){
    var myRef: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)
        myRef= FirebaseDatabase.getInstance().reference.child("trip").child("company")
        getData()
    }
    private fun getData() {
        myRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val taxiFourSeats = ArrayList<Taxi>()
                val taxiFiveSeats = ArrayList<Taxi>()
                val taxiSevenSeats = ArrayList<Taxi>()
                val taxiEightSeats = ArrayList<Taxi>()
                val data = dataSnapshot
                var i = 0
                while (i < data.childrenCount)
                {
                    Log.d("AAA",Thread.currentThread().name)
                    val phone = data.child(i.toString()).child("phone").value.toString()
                    val company = data.child(i.toString()).child("name").value.toString()
                    val child = data.child(i.toString()).child("vehicle")
                    var j = 0
                    while(j<child.childrenCount) {
                        val name = child.child(j.toString()).child("name").value.toString()
                        val unitPrice =  child.child(j.toString()).child("over_1km").value.toString()
                        val price = unitPrice.toFloat()*2
                        when (child.child(j.toString()).child("number_seat").value.toString()) {
                            "4" -> taxiFourSeats.add(Taxi(company, name, phone, price))
                            "5" -> taxiFiveSeats.add(Taxi(company, name, phone, price))
                            "7" -> taxiSevenSeats.add(Taxi(company, name, phone, price))
                            "8" -> taxiEightSeats.add(Taxi(company, name, phone, price))
                        }
                        j += 1
                    }
//                    }
                    i += 1
                }
                setAdapter(taxiFourSeats,taxiFiveSeats,taxiSevenSeats,taxiEightSeats)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }
    private fun setAdapter(taxiFourSeats: ArrayList<Taxi>,taxiFiveSeats: ArrayList<Taxi>,
                           taxiSevenSeats: ArrayList<Taxi>,taxiEightSeats: ArrayList<Taxi> ) {
        val rv4 = findViewById<RecyclerView>(R.id.rv4_seater)
        rv4.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val rv5 = findViewById<RecyclerView>(R.id.rv5_seater)
        rv5.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val rv7 = findViewById<RecyclerView>(R.id.rv7_seater)
        rv7.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val rv8 = findViewById<RecyclerView>(R.id.rv8_seater)
        rv8.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        
        var adapter = SeatsAdapter(taxiFourSeats,this@RecommendActivity)
        rv4.adapter = adapter
        adapter = SeatsAdapter(taxiFiveSeats,this@RecommendActivity)
        rv5.adapter = adapter
        adapter = SeatsAdapter(taxiSevenSeats,this@RecommendActivity)
        rv7.adapter = adapter
        adapter = SeatsAdapter(taxiEightSeats,this@RecommendActivity)
        rv8.adapter = adapter
    }
    }