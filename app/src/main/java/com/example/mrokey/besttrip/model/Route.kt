package com.example.mrokey.besttrip.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class Route {
    @SerializedName("legs")
    @Expose
    private var legs: List<Leg> = ArrayList()
    @SerializedName("overview_polyline")
    @Expose
    private var overviewPolyline: OverviewPolyline? = null

    /**
     *
     * @return
     * The legs
     */
    fun getLegs(): List<Leg> {
        return legs
    }

    /**
     *
     * @param legs
     * The legs
     */
    fun setLegs(legs: List<Leg>) {
        this.legs = legs
    }

    /**
     *
     * @return
     * The overviewPolyline
     */
    fun getOverviewPolyline(): OverviewPolyline? {
        return overviewPolyline
    }

    /**
     *
     * @param overviewPolyline
     * The overview_polyline
     */
    fun setOverviewPolyline(overviewPolyline: OverviewPolyline) {
        this.overviewPolyline = overviewPolyline
    }
}