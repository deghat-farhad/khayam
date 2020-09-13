package com.raven.khayam.poemList.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.raven.khayam.R
import com.raven.khayam.di.DaggerViewModelComponent
import com.raven.khayam.di.ViewModelFactory
import com.raven.khayam.poemList.ViewModelPoemList
import javax.inject.Inject
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator


class MainActivity : FragmentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ViewModelPoemList

    private lateinit var viewPager: ViewPager2
    private lateinit var poemPagerAdapter: PoemFragStateAdapter

    private var isRotate = false


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
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        viewPager = findViewById(R.id.pagerPoem)

        val fabCopy = findViewById<FloatingActionButton>(R.id.fabCopy)
        val fabImage = findViewById<FloatingActionButton>(R.id.fabImage)
        val fabText = findViewById<FloatingActionButton>(R.id.fabText)
        findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener { view ->
            isRotate = ViewAnimation.rotateFab(view, !isRotate);
            if(isRotate){
                ViewAnimation.showIn(fabCopy, 120f)
                ViewAnimation.showIn(fabImage, 360f)
                ViewAnimation.showIn(fabText, 240f)
            }else{
                ViewAnimation.showOut(fabCopy, 120f)
                ViewAnimation.showOut(fabImage, 360f)
                ViewAnimation.showOut(fabText, 240f)
            }
        }

        ViewAnimation.init(fabCopy)
        ViewAnimation.init(fabImage)
        ViewAnimation.init(fabText)

        poemPagerAdapter = PoemFragStateAdapter(this, viewModel.poemList)
        viewPager.adapter = poemPagerAdapter
        val indicator: ScrollingPagerIndicator = findViewById(R.id.indicator)
        indicator.attachToPager(viewPager)
    }

    private fun setObservers() {
        viewModel.showPoems.observe(this, Observer { poemPagerAdapter.notifyDataSetChanged() })
    }
}
