package com.example.mrokey.besttrip.features.trip_results

import android.annotation.SuppressLint
import android.os.Bundle
import com.example.mrokey.besttrip.R
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.mrokey.besttrip.entities.Taxi
import com.example.mrokey.besttrip.features.trip_results.adapter.SeatsAdapter
import kotlinx.android.synthetic.main.activity_recommend.*

class RecommendActivity: AppCompatActivity(),RecommendContract.View, View.OnClickListener {
    private lateinit var presenter: RecommendContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)
        presenter = RecommendPresenter(this)
        val more4seats = findViewById<TextView>(R.id.more4seats)
        val more5seats = findViewById<TextView>(R.id.more5seats)
        val more7seats = findViewById<TextView>(R.id.more7seats)
        val more8seats = findViewById<TextView>(R.id.more8seats)
        more4seats.setOnClickListener(this)
        more5seats.setOnClickListener(this)
        more7seats.setOnClickListener(this)
        more8seats.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View) {

        when (v.id) {

            R.id.more4seats -> {
                presenter.getMore(4)
            }

            R.id.more5seats -> {
                presenter.getMore(5)
            }

            R.id.more7seats -> {
                presenter.getMore(7)
            }

            R.id.more8seats -> {
                presenter.getMore(8)
            }
        }
    }
    override fun onResume() {
        super.onResume()
        presenter.getData()
    }

    override fun setPresenter(presenter: RecommendContract.Presenter) {
        this.presenter = presenter
    }

    override fun showError(message: String) {
        Toast.makeText(this@RecommendActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun setView(taxiFourSeats: ArrayList<Taxi>, taxiFiveSeats: ArrayList<Taxi>,
                         taxiSevenSeats: ArrayList<Taxi>, taxiEightSeats: ArrayList<Taxi>) {
        val rv4 = findViewById<RecyclerView>(R.id.rv4_seater)
        rv4.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val rv5 = findViewById<RecyclerView>(R.id.rv5_seater)
        rv5.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val rv7 = findViewById<RecyclerView>(R.id.rv7_seater)
        rv7.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val rv8 = findViewById<RecyclerView>(R.id.rv8_seater)
        rv8.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        if (taxiFourSeats.size < 3 ) more4seats.visibility = View.GONE
        if (taxiFiveSeats.size < 3 ) more5seats.visibility = View.GONE
        if (taxiSevenSeats.size < 3 ) more7seats.visibility = View.GONE
        if (taxiEightSeats.size < 3 ) more8seats.visibility = View.GONE

        var adapter = SeatsAdapter(2,taxiFourSeats, this@RecommendActivity)
        rv4.adapter = adapter
        adapter = SeatsAdapter(2,taxiFiveSeats, this@RecommendActivity)
        rv5.adapter = adapter
        adapter = SeatsAdapter(2,taxiSevenSeats, this@RecommendActivity)
        rv7.adapter = adapter
        adapter = SeatsAdapter(2,taxiEightSeats, this@RecommendActivity)
        rv8.adapter = adapter
    }
    override fun loadMore(seat: Int,taxiSeats: ArrayList<Taxi>,size: Int) {
        when(seat){
            4 -> {
                val rv4 = findViewById<RecyclerView>(R.id.rv4_seater)
                rv4.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
                val adapter = SeatsAdapter(size,taxiSeats, this@RecommendActivity)
                rv4.adapter = adapter
                more4seats.text = if(size == 2) "Hide"
                else "View All"
            }
            5 -> {
                val rv5 = findViewById<RecyclerView>(R.id.rv5_seater)
                rv5.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
                val adapter = SeatsAdapter(size,taxiSeats, this@RecommendActivity)
                rv5.adapter = adapter
                more5seats.text = if(size == 2) "Hide"
                else "View All"
            }
            7 -> {
                val rv7 = findViewById<RecyclerView>(R.id.rv7_seater)
                rv7.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
                val adapter = SeatsAdapter(size,taxiSeats, this@RecommendActivity)
                rv7.adapter = adapter
                more7seats.text = if(size == 2) "Hide"
                else "View All"
            }
            8 -> {
                val rv8 = findViewById<RecyclerView>(R.id.rv8_seater)
                rv8.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
                val adapter = SeatsAdapter(size,taxiSeats, this@RecommendActivity)
                rv8.adapter = adapter
                more8seats.text = if(size == 2) "Hide"
                else "View All"
            }
        }
    }
}