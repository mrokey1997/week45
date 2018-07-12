package com.example.mrokey.besttrip.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Leg {

    @SerializedName("distance")
    @Expose
    private var distance: Distance? = null
    @SerializedName("duration")
    @Expose
    private var duration: Duration? = null

    /**
     *
     * @return
     * The distance
     */
    fun getDistance(): Distance? {
        return distance
    }

    /**
     *
     * @param distance
     * The distance
     */
    fun setDistance(distance: Distance) {
        this.distance = distance
    }

    /**
     *
     * @return
     * The duration
     */
    fun getDuration(): Duration? {
        return duration
    }

    /**
     *
     * @param duration
     * The duration
     */
    fun setDuration(duration: Duration) {
        this.duration = duration
    }
}