package com.example.mrokey.besttrip.company

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.mrokey.besttrip.R
import com.example.mrokey.besttrip.adapter.CompanyArrayAdapter
import com.example.mrokey.besttrip.company.data.CardData
import com.example.mrokey.besttrip.company.data.DataSet
import com.example.mrokey.besttrip.entities.Company
import com.google.firebase.database.DatabaseError
import com.ramotion.expandingcollection.ECCardData
import com.ramotion.expandingcollection.ECPagerViewAdapter
import kotlinx.android.synthetic.main.activity_overview.*

class OverviewTaxiActivity : AppCompatActivity(), CompanyContract.View {

    private var presenter: CompanyContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        presenter = CompanyPresenter(this)

        presenter?.getDatabase(this)

    }

    override fun setPresenter(presenter: CompanyContract.Presenter) {
        this.presenter = presenter
    }

    override fun onDataChange(companies: ArrayList<Company>) {
        val adapter = object : ECPagerViewAdapter(this, DataSet(companies).getDataSet()) {
            @SuppressLint("SetTextI18n")
            override fun instantiateCard(inflaterService: LayoutInflater, head: ViewGroup, list: ListView, data: ECCardData<*>) {
                val cardData = data as CardData

                // Create adapter for list inside a card and set adapter to card content
                val commentArrayAdapter = CompanyArrayAdapter(applicationContext, cardData.listItems!!)
                list.adapter = commentArrayAdapter
                list.divider = resources.getDrawable(R.drawable.list_divider)
                list.dividerHeight = pxFromDp(applicationContext, 0.5f).toInt()
                list.setSelector(R.color.transparent)
                list.setBackgroundColor(Color.WHITE)
                list.cacheColorHint = Color.TRANSPARENT

                // Add gradient to root header view
                //val gradient = View(applicationContext)
                //gradient.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT)
                //gradient.setBackgroundDrawable(resources.getDrawable(R.drawable.card_head_gradient))
                //head.addView(gradient)

                // Inflate main header layout and attach it to header root view
                inflaterService.inflate(R.layout.card_head, head)

                // Set header data from data object
                val name = head.findViewById<View>(R.id.tv_name_company) as TextView
                name.text = cardData.companyName
                val phone = head.findViewById<View>(R.id.tv_phone) as TextView
                phone.text = cardData.companyPhone
                val phoneCall = head.findViewById<View>(R.id.img_phone_call) as ImageView
                phoneCall.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:${removeSpaceInString(phone.text.toString())}")
                    startActivity(intent)
                }

                // Add onclick listener to card header for toggle card state
                head.setOnClickListener { ec_pager_element.toggle() }
            }
        }

        ec_pager_element.setPagerViewAdapter(adapter)
        ec_pager_element.setBackgroundSwitcherView(findViewById(R.id.ec_bg_switcher_element))

        ec_pager_element.setOnCardSelectedListener {
            newPosition, oldPosition, totalElements -> items_count_view.update(newPosition, oldPosition, totalElements)
        }
    }

    override fun onCancelled(p0: DatabaseError) {
        Toast.makeText(this, "Cannot get data from server", Toast.LENGTH_SHORT).show()
    }

    fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    fun removeSpaceInString(phone: String) : String {
        return phone.replace(" ", "")
    }
}