package com.example.mrokey.besttrip.detail

import com.example.mrokey.besttrip.entities.Taxi

interface RecommendDetailContract {
    interface View {
        fun setPresenter(presenter: Presenter)

        fun setView(taxi: Taxi)

    }

    interface Presenter {
        fun storeInfo(taxi: Taxi)

    }
}