package com.example.mrokey.besttrip.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Distance {
    @SerializedName("text")
    @Expose
    private var text: String? = null
    @SerializedName("value")
    @Expose
    private var value: Int? = null

    /**
     *
     * @return
     * The text
     */
    fun getText(): String? {
        return text
    }

    /**
     *
     * @param text
     * The text
     */
    fun setText(text: String) {
        this.text = text
    }

    /**
     *
     * @return
     * The value
     */
    fun getValue(): Int? {
        return value
    }

    /**
     *
     * @param value
     * The value
     */
    fun setValue(value: Int?) {
        this.value = value
    }

}