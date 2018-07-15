package com.example.mrokey.besttrip.company.data

import com.example.mrokey.besttrip.R
import com.example.mrokey.besttrip.entities.Company
import com.ramotion.expandingcollection.ECCardData
import java.util.*

class DataSet(listCompany: MutableList<Company>) {
    private val dataSet: MutableList<ECCardData<*>>

    private val listResource = listOf(R.drawable.i, R.drawable.ii, R.drawable.iii,
            R.drawable.iv, R.drawable.v, R.drawable.vi, R.drawable.vii, R.drawable.viii)

    private val listResourceTaxi = listOf(R.drawable.vinasun, R.drawable.mailinh,
            R.drawable.vinataxi, R.drawable.saigonair, R.drawable.hoanglong, R.drawable.petrolimex,
            R.drawable.savico, R.drawable.daukhi)

    private val MAX_NUMBER_CAR = 8

    init {
        dataSet = ArrayList(MAX_NUMBER_CAR)

        for (i in 0 until MAX_NUMBER_CAR) {
            val item = CardData()
            item.mainBackgroundResource = listResource[i]
            item.headBackgroundResource = listResourceTaxi[i]
            item.headTitle = listCompany[i].name
            item.companyName = listCompany[i].name
            item.companyPhone = listCompany[i].phone
            item.setListItems(listCompany[i].vehicles)
            dataSet.add(item)
        }
    }

    fun getDataSet(): List<ECCardData<*>> {
        dataSet.shuffle()
        return dataSet
    }
}