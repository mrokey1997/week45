package com.example.mrokey.besttrip.company.data

import com.example.mrokey.besttrip.entities.Vehicle
import com.ramotion.expandingcollection.ECCardData

class CardData : ECCardData<Vehicle> {
    var headTitle: String? = null
    private var headBackgroundResource: Int? = null
    private var mainBackgroundResource: Int? = null

    var companyName: String? = null
    var companyPhone: String? = null
    private var listItems: MutableList<Vehicle>? = null

    override fun getHeadBackgroundResource(): Int? {
        return headBackgroundResource
    }

    fun setHeadBackgroundResource(headBackgroundResource: Int?) {
        this.headBackgroundResource = headBackgroundResource
    }

    override fun getMainBackgroundResource(): Int? {
        return mainBackgroundResource
    }

    fun setMainBackgroundResource(mainBackgroundResource: Int?) {
        this.mainBackgroundResource = mainBackgroundResource
    }

    override fun getListItems(): MutableList<Vehicle>? {
        return listItems
    }

    fun setListItems(listItems: MutableList<Vehicle>) {
        this.listItems = listItems
    }

}