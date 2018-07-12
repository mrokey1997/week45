package com.example.mrokey.besttrip.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class Example {
    @SerializedName("routes")
    @Expose
    private var routes: List<Route> = ArrayList()

    /**
     *
     * @return
     * The routes
     */
    fun getRoutes(): List<Route> {
        return routes
    }

    /**
     *
     * @param routes
     * The routes
     */
    fun setRoutes(routes: List<Route>) {
        this.routes = routes
    }

}