package com.example.mrokey.besttrip.util

object PriceFormat {
    fun priceFormat(price: Int): String {
        return if (price > 999) {
            val mod = price % 1000
            val div = price / 1000
            when {
                mod in 10..99 -> div.toString() + ".0" + mod.toString()
                mod < 10 -> div.toString() + ".00" + mod.toString()
                else -> div.toString() + "." + mod.toString()
            }
        } else {
            price.toString()
        }
    }
}
