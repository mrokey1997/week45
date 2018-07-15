package com.example.mrokey.besttrip.company

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.mrokey.besttrip.R
import com.example.mrokey.besttrip.adapter.ViewPagerAdapter
import com.example.mrokey.besttrip.entities.Company
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.activity_card_company.*

class CardCompanyActivity : AppCompatActivity(), CompanyContract.View {

    private val NUM_PAGE = 8

    private var presenter: CompanyContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_company)

        presenter = CompanyPresenter(this)

        presenter?.getDatabase(this)
    }

    override fun setPresenter(presenter: CompanyContract.Presenter) {
        this.presenter = presenter
    }

    override fun onDataChange(companies: ArrayList<Company>) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        for (i in 0 until companies.size) {
            val bundle = Bundle()
            bundle.putString("logo", companies[i].logo)
            val obj = CardCompanyFragment()
            obj.arguments = bundle
            adapter.addFragment(obj, "Taxi")
        }
        view_pager_test.adapter = adapter
        view_pager_test.offscreenPageLimit = NUM_PAGE
        view_pager_test.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(position: Int) {
                val company = companies[position]
                tv_name.text = company.name
                tv_phone.text = company.phone
                tv_address.text = company.address
            }
        })
    }

    override fun onCancelled(p0: DatabaseError) {
        Toast.makeText(this, "Cannot get data from server", Toast.LENGTH_SHORT).show()
    }
}