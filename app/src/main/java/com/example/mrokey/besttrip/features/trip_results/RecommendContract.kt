package com.example.mrokey.besttrip.features.trip_results

import com.example.mrokey.besttrip.entities.Taxi

interface RecommendContract {
    interface View {
        fun setPresenter(presenter: Presenter)

        fun showError(message: String)

        fun setView(taxiFourSeats: ArrayList<Taxi>, taxiFiveSeats: ArrayList<Taxi>,
                    taxiSevenSeats: ArrayList<Taxi>, taxiEightSeats: ArrayList<Taxi> )
        fun loadMore(seat: Int,taxiSeats: ArrayList<Taxi>, size: Int)

    }
    interface Presenter {
        fun getData()

        fun getMore(seat: Int)
    }
}