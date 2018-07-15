package com.example.mrokey.besttrip.recommend

import com.example.mrokey.besttrip.entities.Taxi

interface RecommendContract {
    interface View {
        fun setPresenter(presenter: Presenter)

        fun showError(message: String)

        fun setView(taxiFourSeats: ArrayList<Taxi>, taxiFiveSeats: ArrayList<Taxi>,
                    taxiSevenSeats: ArrayList<Taxi>, taxiEightSeats: ArrayList<Taxi> )
        fun loadMore(seat: Int,taxiSeats: ArrayList<Taxi>, size: Int)

        fun setToolbar()

        fun setRecyclerView()

        fun showLoading(isShow: Boolean)
    }
    interface Presenter {
        fun getData(start: String, end: String, distance: Float)

        fun getMore(seat: Int)
    }
}