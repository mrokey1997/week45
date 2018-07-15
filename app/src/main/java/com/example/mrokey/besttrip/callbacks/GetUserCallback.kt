package com.example.mrokey.besttrip.callbacks

import android.net.Uri
import com.example.mrokey.besttrip.entities.User
import com.facebook.GraphRequest
import org.json.JSONObject

class GetUserCallback() {
    interface IGetUserResponse {
        fun onCompleted(user: User)
    }

    private var mGetUserResponse : IGetUserResponse? = null
    private var mCallback: GraphRequest.Callback? = null

    constructor(getUserResponse: IGetUserResponse) : this() {
        mGetUserResponse = getUserResponse
        mCallback = GraphRequest.Callback {
            val user : User?
            val userObj = it.jsonObject ?: return@Callback
            user = jsonToUser(userObj)
            mGetUserResponse!!.onCompleted(user!!)
        }
    }

    private fun jsonToUser(user: JSONObject?): User? {
        val picture = Uri.parse(user?.getJSONObject("picture")?.getJSONObject("data")?.getString("url"))
        val name = user?.getString("name")
        val id = user?.getString("id")
        var email : String? = null
        if (user?.has("email")!!) {
            email = user.getString("email")
        }

        val permission = ""

        return User(picture, name!!, id!!, email, permission)
    }

    fun getCallback() : GraphRequest.Callback {
        return mCallback!!

    }
}