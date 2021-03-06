package com.example.mrokey.besttrip.company

import com.example.mrokey.besttrip.entities.Company
import com.example.mrokey.besttrip.entities.Vehicle
import com.google.firebase.database.*

class CompanyPresenter(internal var view: CompanyContract.View) : CompanyContract.Presenter {

    private var mReference: DatabaseReference? = null
    private var listCompany = ArrayList<Company>()

    init {
        view.setPresenter(this)
        mReference = FirebaseDatabase.getInstance().reference.child("company")
    }

    override fun getDatabase(listener: CompanyContract.View) {

        mReference?.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val size = dataSnapshot.childrenCount

                for (i in 0 until size) {
                    val child = dataSnapshot.child(i.toString())
                    var listTaxi = ArrayList<Vehicle>()

                    for (j in 0 until child.child("vehicle").childrenCount) {
                        val vehicleChild = child.child("vehicle").child(j.toString())
                        listTaxi.add(Vehicle(
                                vehicleChild.child("name").value.toString(),
                                vehicleChild.child("number_seat").value.toString().toLong(),
                                vehicleChild.child("_1km").value.toString().toLong(),
                                vehicleChild.child("over_1km").value.toString().toDouble(),
                                vehicleChild.child("over_30km").value.toString().toDouble()
                        ))
                    }
                    listCompany.add(Company(
                            child.child("name").value.toString(),
                            child.child("address").value.toString(),
                            child.child("phone").value.toString(),
                            listTaxi,
                            child.child("wait_time").value.toString().toLong(),
                            child.child("logo").value.toString()
                    ))
                }
                listener.onDataChange(listCompany)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

}