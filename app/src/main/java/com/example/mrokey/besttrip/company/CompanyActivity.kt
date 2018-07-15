package com.example.mrokey.besttrip.company

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.mrokey.besttrip.entities.Company

import com.example.mrokey.besttrip.R
import com.example.mrokey.besttrip.adapter.CompanyAdapter
import com.google.firebase.database.DatabaseError
import io.reactivex.disposables.Disposable

import kotlinx.android.synthetic.main.activity_company.*

class CompanyActivity : AppCompatActivity(), CompanyContract.View {

    private var mListCompany = ArrayList<Company>()
    private var mCompanyAdapter = CompanyAdapter(mListCompany, this)
    private var presenter: CompanyContract.Presenter? = null
    private var mSearchView : SearchView? = null
    private var subcrise: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company)

        presenter = CompanyPresenter(this)

        initRecyclerView()

        initToolbar()

        setupItemClick()

        presenter?.getDatabase(this)
    }

    /**
     * On click item
     */
    private fun setupItemClick() {
        subcrise = mCompanyAdapter.clickEvent.subscribe {
            Toast.makeText(this, "Click on ${it.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        subcrise?.dispose()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Taxi List"
    }
    private fun initRecyclerView() {
        rv_taxi.hasFixedSize()
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_taxi.layoutManager = layoutManager
        rv_taxi.adapter = mCompanyAdapter
        mCompanyAdapter.setData(mListCompany)
    }

    override fun setPresenter(presenter: CompanyContract.Presenter) {
        this.presenter = presenter
    }

    override fun onDataChange(companies: ArrayList<Company>) {
        mCompanyAdapter.setData(companies)
    }


    override fun onCancelled(p0: DatabaseError) {
        Toast.makeText(this, "Cannot get data from server", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        mSearchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        mSearchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        mSearchView?.maxWidth = Integer.MAX_VALUE

        mSearchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mCompanyAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                mCompanyAdapter.filter.filter(query)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_search -> return true
        }
        return super.onOptionsItemSelected(item)
    }

}