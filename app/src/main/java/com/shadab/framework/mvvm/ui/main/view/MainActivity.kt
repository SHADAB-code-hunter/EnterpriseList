package com.shadab.framework.mvvm.ui.main.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shadab.framework.mvvm.R
import com.shadab.framework.mvvm.data.api.ApiHelper
import com.shadab.framework.mvvm.data.api.ApiServiceImpl
import com.shadab.framework.mvvm.data.model.EnterpriseModel
import com.shadab.framework.mvvm.ui.base.ViewModelFactory
import com.shadab.framework.mvvm.ui.main.adapter.EnterpriseAdapter
import com.shadab.framework.mvvm.ui.main.viewmodel.MainViewModel
import com.shadab.framework.mvvm.utils.OnLoadMoreListener
import com.shadab.framework.mvvm.utils.RecyclerViewLoadMoreScroll
import com.shadab.framework.mvvm.utils.Status
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"
    private lateinit var mainViewModel: MainViewModel
    private lateinit var enterpriseAdapter: EnterpriseAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll
    private var pageNo: Int = 1
    private var search: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpEnterpriseUI()
        setupViewModel()
        setupEnterpriseAPICall()
        setRVScrollListener()
        setSearchListener()
        // fetch list from server
        mainViewModel.fetchEnterpriseList(pageNo,search)

    }

    private fun setSearchListener() {
        custom_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("MainActivity",""+newText);
                enterpriseAdapter.filter.filter(newText)
                return false
            }

        })
    }

    private fun setUpEnterpriseUI() {

        val searchIcon = custom_search.findViewById<ImageView>(R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.WHITE)

        mLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = mLayoutManager
        enterpriseAdapter = EnterpriseAdapter(arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,(recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = enterpriseAdapter
    }

    private fun setupEnterpriseAPICall() {
        mainViewModel.getEnterpriseList().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    pageNo++
                    progressBar.visibility = View.GONE
                    it.data?.let { enterpriseList -> renderEnterpriseList(enterpriseList) }
                    recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
        //mainViewModel.fetchMovieList(pageNo, search)
        progressBar.visibility = View.GONE
    }

    private fun renderEnterpriseList(renderEnterpriseList: List<EnterpriseModel>) {
        enterpriseAdapter.addData(sortedList(renderEnterpriseList))
        enterpriseAdapter.notifyDataSetChanged()
    }

    private fun sortedList(renderEnterpriseList: List<EnterpriseModel>) : List<EnterpriseModel> {
        return renderEnterpriseList.sortedWith(compareBy({ it.title }))
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(MainViewModel::class.java)
    }

    private  fun setRVScrollListener() {
        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                mainViewModel.fetchMovieList(pageNo, search)
            }
        })
        recyclerView.addOnScrollListener(scrollListener)
    }

}
