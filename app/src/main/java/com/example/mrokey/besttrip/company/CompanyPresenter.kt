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
                        val vehicleChild = child.child("vehicle").child("0")
                        listTaxi.add(Vehicle(
                                vehicleChild.child("name").value.toString(),
                                vehicleChild.child("number_seat").value as Long,
                                vehicleChild.child("_1km").value as Long,
                                vehicleChild.child("over_1km").value as Double,
                                vehicleChild.child("over_30km").value as Double
                        ))
                    }
                    listCompany.add(Company(
                            child.child("name").value.toString(),
                            child.child("address").value.toString(),
                            child.child("phone").value.toString(),
                            listTaxi,
                            child.child("wait_time").value as Long,
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