package com.example.mrokey.besttrip.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OverviewPolyline {
    @SerializedName("points")
    @Expose
    private var points: String? = null

    /**
     *
     * @return
     * The points
     */
    fun getPoints(): String? {
        return points
    }

    /**
     *
     * @param points
     * The points
     */
    fun setPoints(points: String) {
        this.points = points
    }

}