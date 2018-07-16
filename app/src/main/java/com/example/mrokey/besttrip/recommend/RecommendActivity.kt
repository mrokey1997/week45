package com.example.mrokey.besttrip.recommend

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
import com.example.mrokey.besttrip.adapter.SeatsAdapter
import kotlinx.android.synthetic.main.activity_recommend.*

class RecommendActivity: AppCompatActivity(),RecommendContract.View, View.OnClickListener {
    var rv4 : RecyclerView? = null
    var rv5 : RecyclerView? = null
    var rv7 : RecyclerView? = null
    var rv8 : RecyclerView? = null
    private var presenter: RecommendContract.Presenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)
        setToolbar()
        presenter = RecommendPresenter(this)
        setRecyclerView()
        val more4seats = findViewById<TextView>(R.id.more4seats)
        val more5seats = findViewById<TextView>(R.id.more5seats)
        val more7seats = findViewById<TextView>(R.id.more7seats)
        val more8seats = findViewById<TextView>(R.id.more8seats)
        more4seats.setOnClickListener(this)
        more5seats.setOnClickListener(this)
        more7seats.setOnClickListener(this)
        more8seats.setOnClickListener(this)
        val bundle = intent.extras
        val start = bundle.getString("start_location")
        val end = bundle.getString("end_location")
        val distance = bundle.getString("distance")
        val startToEnd = distance.replace(" km", "")
                .replace(".", "")
                .replace(",", "").toFloat()
        startLocation.text = start
        endLocation.text = end
        presenter?.getData(start, end, startToEnd)
    }

    override fun setRecyclerView() {
        rv4 = findViewById(R.id.rv4_seater)
        rv4?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rv4?.setHasFixedSize(true)
        rv4?.isNestedScrollingEnabled = false
        rv5 = findViewById(R.id.rv5_seater)
        rv5?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rv5?.setHasFixedSize(true)
        rv5?.isNestedScrollingEnabled = false
        rv7 = findViewById(R.id.rv7_seater)
        rv7?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rv7?.setHasFixedSize(true)
        rv7?.isNestedScrollingEnabled = false
        rv8 = findViewById(R.id.rv8_seater)
        rv8?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rv8?.setHasFixedSize(true)
        rv8?.isNestedScrollingEnabled = false
    }

    override fun onClick(v: View) {

        when (v.id) {

            R.id.more4seats -> {
                presenter?.getMore(4)
            }

            R.id.more5seats -> {
                presenter?.getMore(5)
            }

            R.id.more7seats -> {
                presenter?.getMore(7)
            }

            R.id.more8seats -> {
                presenter?.getMore(8)
            }
        }
    }

    override fun setToolbar() {
        setSupportActionBar(tbRecommend)
        supportActionBar?.title = "Result"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        tbRecommend.setNavigationOnClickListener {
            onBackPressed()
            this.overridePendingTransition(0, R.anim.back_right)
        }
    }
    override fun setPresenter(presenter: RecommendContract.Presenter) {
        this.presenter = presenter
    }

    override fun showError(message: String) {
        Toast.makeText(this@RecommendActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun setView(taxiFourSeats: ArrayList<Taxi>, taxiFiveSeats: ArrayList<Taxi>,
                         taxiSevenSeats: ArrayList<Taxi>, taxiEightSeats: ArrayList<Taxi>) {
        if (taxiFourSeats.size < 3 ) more4seats.visibility = View.GONE
        if (taxiFiveSeats.size < 3 ) more5seats.visibility = View.GONE
        if (taxiSevenSeats.size < 3 ) more7seats.visibility = View.GONE
        if (taxiEightSeats.size < 3 ) more8seats.visibility = View.GONE

        var adapter = SeatsAdapter(2, taxiFourSeats, this@RecommendActivity,0)
        rv4?.adapter = adapter
        adapter = SeatsAdapter(2, taxiFiveSeats, this@RecommendActivity,0)
        rv5?.adapter = adapter
        adapter = SeatsAdapter(2, taxiSevenSeats, this@RecommendActivity,0)
        rv7?.adapter = adapter
        adapter = SeatsAdapter(2, taxiEightSeats, this@RecommendActivity,0)
        rv8?.adapter = adapter
        constraint.visibility = View.VISIBLE
    }

    override fun showLoading(isShow: Boolean) {
        loadRecommend.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun loadMore(seat: Int,taxiSeats: ArrayList<Taxi>,size: Int) {
        when(seat){
            4 -> {
                if(size == 2)
                {
                    val adapter = SeatsAdapter(size, taxiSeats, this@RecommendActivity,0)
                    rv4?.adapter = adapter
                    more4seats.text =getString(R.string.view)
                    more4seats.isSelected = false
                }
                else
                {
                    val adapter = SeatsAdapter(size, taxiSeats, this@RecommendActivity,1)
                    rv4?.adapter = adapter
                    more4seats.text =getString(R.string.hide)
                    more4seats.isSelected = true
                }
            }
            5 -> {
                if(size == 2)
                {
                    val adapter = SeatsAdapter(size, taxiSeats, this@RecommendActivity,0)
                    rv5?.adapter = adapter
                    more5seats.text =getString(R.string.view)
                    more5seats.isSelected = false
                }
                else
                {
                    val adapter = SeatsAdapter(size, taxiSeats, this@RecommendActivity,1)
                    rv5?.adapter = adapter
                    more5seats.text =getString(R.string.hide)
                    more5seats.isSelected = true
                }
            }
            7 -> {
                if(size == 2)
                {
                    val adapter = SeatsAdapter(size, taxiSeats, this@RecommendActivity,0)
                    rv7?.adapter = adapter
                    more7seats.text =getString(R.string.view)
                    more7seats.isSelected = false
                }
                else
                {
                    val adapter = SeatsAdapter(size, taxiSeats, this@RecommendActivity,1)
                    rv7?.adapter = adapter
                    more7seats.text =getString(R.string.hide)
                    more7seats.isSelected = true
                }
            }
            8 -> {
                if(size == 2)
                {
                    val adapter = SeatsAdapter(size, taxiSeats, this@RecommendActivity,0)
                    rv8?.adapter = adapter
                    more8seats.text =getString(R.string.view)
                    more8seats.isSelected = false
                }
                else
                {
                    val adapter = SeatsAdapter(size, taxiSeats, this@RecommendActivity,1)
                    rv8?.adapter = adapter
                    more8seats.text =getString(R.string.hide)
                    more8seats.isSelected = true
                }
            }
        }
    }
}