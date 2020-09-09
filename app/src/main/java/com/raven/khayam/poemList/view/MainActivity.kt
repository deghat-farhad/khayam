package com.raven.khayam.poemList.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raven.khayam.R
import com.raven.khayam.di.DaggerViewModelComponent
import com.raven.khayam.di.ViewModelFactory
import com.raven.khayam.poemList.ViewModelPoemList
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ViewModelPoemList

    private lateinit var recViwPoemList: RecyclerView
    private lateinit var poemAdapter: PoemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        injectThisToDagger()
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ViewModelPoemList::class.java)

        initiate()
        setObservers()
        viewModel.viewIsReady()
    }

    private fun injectThisToDagger() {
        DaggerViewModelComponent.builder().application(application).build().injectActivity(this)
    }

    private fun initiate() {
        recViwPoemList = findViewById(R.id.RecViwPoemList)
        poemAdapter = PoemAdapter(viewModel.poemList)
        initiateUsersRecyclerView()
    }

    private fun initiateUsersRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        recViwPoemList.apply {
            layoutManager = linearLayoutManager
            adapter = poemAdapter
        }
    }

    private fun setObservers() {
        viewModel.showPoems.observe(this, Observer { poemAdapter.notifyDataSetChanged() })
    }
}
