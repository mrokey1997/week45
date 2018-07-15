package com.example.mrokey.besttrip.util

object PriceFormat {
    fun priceFormat(price: Float): String {
        var format:Int = (price*1000).toInt()
        var mod: Int
        var result = ""
        while(format>999){
            mod = format % 1000
            result = when {
                mod in 10..99 -> result + ".0" + mod.toString()
                mod < 10 -> result + ".00" + mod.toString()
                else -> result + "." + mod.toString()
            }
            format /= 1000
        }
        result = format.toString() + result
        return result
    }
}
