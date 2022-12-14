package com.raven.khayam.poemList.view

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.raven.khayam.R
import com.raven.khayam.di.DaggerViewModelComponent
import com.raven.khayam.di.ViewModelFactory
import com.raven.khayam.poemList.ViewModelPoemList
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.io.File
import javax.inject.Inject


class MainActivity : FragmentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ViewModelPoemList

    private lateinit var poemPagerAdapter: PoemPagerAdapter
    private lateinit var poemLayout: FrameLayout
    private lateinit var poemViewPager: ViewPager2
    private lateinit var searchView: SearchView

    private var isRotate = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
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
        initFab()
        initPoemViwPager()

        poemLayout = findViewById(R.id.poemLayout)
        findViewById<FloatingActionButton>(R.id.fabRandom).setOnClickListener { viewModel.randomPoem() }
        searchView = findViewById(R.id.searchView)
        searchView.listener = object : SearchView.Listener {
            override fun onSearchListener(searchPhrase: String) {
                viewModel.findPoem(searchPhrase)
            }

            override fun onSearchCloseListener() {
                viewModel.searchClosed()
            }
        }
    }

    private fun initFab() {
        val fabCopy = findViewById<FloatingActionButton>(R.id.fabCopy)
        val fabImage = findViewById<FloatingActionButton>(R.id.fabImage)
        val fabText = findViewById<FloatingActionButton>(R.id.fabText)
        val fabMain = findViewById<FloatingActionButton>(R.id.fabMain)

        fabMain.setOnClickListener { view ->
            isRotate = ViewAnimation.rotateFab(view, !isRotate);
            if (isRotate) {
                ViewAnimation.showIn(fabCopy, 120f)
                ViewAnimation.showIn(fabImage, 360f)
                ViewAnimation.showIn(fabText, 240f)
            } else {
                ViewAnimation.showOut(fabCopy, 120f)
                ViewAnimation.showOut(fabImage, 360f)
                ViewAnimation.showOut(fabText, 240f)
            }
        }

        ViewAnimation.init(fabCopy)
        ViewAnimation.init(fabImage)
        ViewAnimation.init(fabText)

        fabImage.setOnClickListener {
            viewModel.sharePoemImage(getBitmapOfPoem(), cacheDir)
        }

        fabText.setOnClickListener {
            viewModel.sharePoemText(poemViewPager.currentItem)
        }

        fabCopy.setOnClickListener {
            viewModel.copyPoem(
                poemViewPager.currentItem,
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            )
        }
    }

    private fun initPoemViwPager() {
        poemViewPager = findViewById<ViewPager2>(R.id.pagerPoem)
        poemPagerAdapter = PoemPagerAdapter(this, viewModel.poemList)
        val indicator: ScrollingPagerIndicator = findViewById(R.id.indicator)

        poemViewPager.adapter = poemPagerAdapter
        indicator.attachToPager(poemViewPager)
    }

    private fun setObservers() {
        viewModel.showPoems.observe(this, Observer {
            poemViewPager.adapter = poemPagerAdapter
            poemPagerAdapter.notifyDataSetChanged()
        })
        viewModel.poemImageFile.observe(
            this,
            Observer { viewModel.sharePoemImageUri(getUriOf(it)) })
        viewModel.shareIntentLive.observe(this, Observer {
            startActivity(
                Intent.createChooser(it, "choose an app")
            )
        })
        viewModel.copied.observe(this, Observer {
            Toast.makeText(this, "poem copied to clipboard.", Toast.LENGTH_SHORT).show()
        })
        viewModel.randomPoemIndex.observe(this, Observer { poemViewPager.currentItem = it })
    }

    private fun getBitmapOfPoem(): Bitmap {
        val bitmap =
            Bitmap.createBitmap(poemViewPager.width, poemViewPager.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        poemViewPager.draw(canvas)
        return bitmap
    }

    private fun getUriOf(file: File) =
        FileProvider.getUriForFile(this, "com.raven.khayam.fileprovider", file)

    override fun onBackPressed() {
        if (searchView.isOpen)
            searchView.closeSearch()
        else
            super.onBackPressed()
    }
}
