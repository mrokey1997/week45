package com.example.mrokey.besttrip.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.mrokey.besttrip.R
import com.example.mrokey.besttrip.entities.Company
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_rv_company.view.*
import java.util.*

class CompanyAdapter(private var companies: MutableList<Company>, private val context: Context) :
        RecyclerView.Adapter<CompanyAdapter.ViewHolder>(), Filterable {

    private val clickSubject = PublishSubject.create<Company>()

    private var companiesFiltered : MutableList<Company>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.item_rv_company, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return companiesFiltered!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val company = companiesFiltered?.get(position)
        holder.tvName.text = company?.name
        holder.tvPhone.text = company?.phone
        Glide.with(context)
                .load(company?.logo)
                .into(holder.imgLogo)
        when (position % 3) {
            0 -> holder.parent.setBackgroundColor(context.resources.getColor(R.color.purple))
            1 -> holder.parent.setBackgroundColor(context.resources.getColor(R.color.deepPurple))
            2 -> holder.parent.setBackgroundColor(context.resources.getColor(R.color.indigo))
        }
    }

    fun setData(companies: MutableList<Company>) {
        this.companies = companies
        companiesFiltered = companies
        notifyDataSetChanged()
    }

    fun clearData() {
        companies.clear()
        companiesFiltered?.clear()
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val sequence = charSequence.toString()
                companiesFiltered = if (sequence.isEmpty())
                    companies
                else {
                    val filteredList: MutableList<Company> = ArrayList()
                    for (company: Company in companies)
                        if (company.name.toLowerCase().contains(sequence.toLowerCase()) || company.phone.contains(sequence))
                            filteredList.add(company)
                    filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = companiesFiltered
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, filterResults: FilterResults?) {
                companiesFiltered = filterResults?.values as ArrayList<Company>
                notifyDataSetChanged()
            }
        }
    }

    val clickEvent: Observable<Company> = clickSubject

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgLogo = view.img_logo as ImageView
        val tvName = view.tv_name as TextView
        val tvPhone = view.tv_phone as TextView
        val parent = view.view_parent as CardView

        init {
            view.setOnClickListener{
                companiesFiltered?.get(layoutPosition)?.let { it1 -> clickSubject.onNext(it1) }
            }
        }
    }
}