package com.raven.khayam.poemList.view

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.raven.khayam.R
import com.raven.khayam.di.DaggerViewModelComponent
import com.raven.khayam.di.ViewModelFactory
import com.raven.khayam.poemList.ViewModelPoemList
import javax.inject.Inject

class MainActivity : FragmentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ViewModelPoemList

    private lateinit var viewPager: ViewPager2
    private lateinit var poemPagerAdapter: PoemFragStateAdapter


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
        viewPager = findViewById(R.id.pagerPoem)
        poemPagerAdapter = PoemFragStateAdapter(this, viewModel.poemList)
        viewPager.adapter = poemPagerAdapter
    }

    private fun setObservers() {
        viewModel.showPoems.observe(this, Observer { poemPagerAdapter.notifyDataSetChanged() })
    }
}
