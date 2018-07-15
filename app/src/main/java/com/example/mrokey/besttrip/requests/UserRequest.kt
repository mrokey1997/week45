package com.example.mrokey.besttrip.requests

import android.os.Bundle
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod

class UserRequest() {
    companion object {
        private val ME_ENDPOINT = "/me"

        fun makeUserRequest(callback: GraphRequest.Callback) {
            val params = Bundle()
            params.putString("fields", "picture, name, id, email, permissions")

            val request = GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    ME_ENDPOINT,
                    params,
                    HttpMethod.GET,
                    callback
            )

            request.executeAsync()
        }

    }

}