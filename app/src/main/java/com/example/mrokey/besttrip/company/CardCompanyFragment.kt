package com.example.mrokey.besttrip.company

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.mrokey.besttrip.R


class CardCompanyFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_card_company, container, false)

        val imgLogo = view.findViewById(R.id.img_logo_test) as ImageView

        val logo = arguments?.getString("logo")

        Glide.with(this)
                .load(logo)
                .into(imgLogo)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}