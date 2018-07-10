package com.example.mrokey.besttrip.features.trip_results.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mrokey.besttrip.entities.Taxi
import com.example.mrokey.besttrip.R
import kotlinx.android.synthetic.main.item_recommend.view.*

class SeatsAdapter(val items : ArrayList<Taxi>, val context: Context) : RecyclerView.Adapter<SeatsAdapter.ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recommend, parent, false))
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = items[position].nameCar
        holder.company.text = items[position].company
        holder.price.text = items[position].price.toString()
        holder.phone.text = items[position].phone
    }
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.tvName
        val company : TextView = view.tvCompany
        val phone: TextView = view.tvPhone
        val price: TextView = view.tvPrice
    }
}

