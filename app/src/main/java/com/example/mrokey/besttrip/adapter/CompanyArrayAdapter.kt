package com.example.mrokey.besttrip.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ramotion.expandingcollection.ECCardContentListItemAdapter
import com.example.mrokey.besttrip.R
import com.example.mrokey.besttrip.entities.Vehicle


class CompanyArrayAdapter(context: Context, objects: MutableList<Vehicle>)
    : ECCardContentListItemAdapter<Vehicle>(context, R.layout.list_element, objects) {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: ViewHolder
        var rowView: View? = convertView

        if (rowView == null) {
            val inflater = LayoutInflater.from(context)

            rowView = inflater.inflate(R.layout.list_element, null)
            // configure view holder
            viewHolder = ViewHolder()
            viewHolder.tvNameCar = rowView!!.findViewById(R.id.tv_name_car)
            viewHolder.tvNumberSeat = rowView!!.findViewById(R.id.tv_number_seat)
            viewHolder.tvOpenDoor = rowView!!.findViewById(R.id.tv_open_door)
            viewHolder.tvOver1km = rowView!!.findViewById(R.id.tv_over_1km)
            viewHolder.tvOver30km = rowView!!.findViewById(R.id.tv_over_30km)
            rowView.tag = viewHolder
        } else {
            viewHolder = rowView.tag as ViewHolder
        }

        val objectItem = getItem(position)
        if (objectItem != null) {
            viewHolder.tvNameCar?.text = objectItem.name
            viewHolder.tvNumberSeat?.text = objectItem.number_seat.toString()
            viewHolder.tvOpenDoor?.text = (objectItem._1km * 1000).toString()
            viewHolder.tvOver1km?.text = (objectItem.over_1km * 1000).toInt().toString()
            viewHolder.tvOver30km?.text = (objectItem.over_30km * 1000).toInt().toString()
        }

        return rowView
    }

    internal class ViewHolder {
        var tvNameCar: TextView? = null
        var tvNumberSeat: TextView? = null
        var tvOpenDoor: TextView? = null
        var tvOver1km: TextView? = null
        var tvOver30km: TextView? = null

    }

}