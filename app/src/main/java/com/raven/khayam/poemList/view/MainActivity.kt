package com.raven.khayam.poemList.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.raven.khayam.R
import com.raven.khayam.model.PoemItem
import com.raven.khayam.poemList.PoemListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.io.File


@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    val viewModel: PoemListViewModel by viewModels<PoemListViewModel>()

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

        initiate()
        setObservers()
        viewModel.viewIsReady()
    }

    private fun initiate() {
        initFab()

        poemLayout = findViewById(R.id.poemLayout)
        findViewById<FloatingActionButton>(R.id.fabRandom).setOnClickListener { viewModel.randomPoem() }
        searchView = findViewById(R.id.searchView)
        searchView.listener = object : SearchView.Listener {
            override fun onSearchListener(searchPhrase: String) {
                viewModel.findNearestPoem(searchPhrase)
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
            viewModel.sharePoemText()
        }

        fabCopy.setOnClickListener {
            /*viewModel.copyPoem(
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            )*/
        }
    }

    private fun initPoemViwPager(poems: List<PoemItem>) {
        poemViewPager = findViewById<ViewPager2>(R.id.pagerPoem)
        poemPagerAdapter = PoemPagerAdapter(this, poems)
        val indicator: ScrollingPagerIndicator = findViewById(R.id.indicator)

        poemViewPager.adapter = poemPagerAdapter
        indicator.attachToPager(poemViewPager)
    }

    private fun setObservers() {
        viewModel.uiState.onEach { uiState ->
            when (uiState) {
                is PoemListViewModel.UiState.Loaded -> {/*
                    if(!::poemViewPager.isInitialized || poemPagerAdapter.poemList !=  uiState.poems)
                        initPoemViwPager(uiState.poems)
                    uiState.shareIntent?.let { intent ->
                        startActivity(
                            Intent.createChooser(intent, "choose an app")
                        )
                    }
                    uiState.imageToShare?.let { file ->
                        viewModel.sharePoemImageUri(getUriOf(file), poemViewPager.currentItem)
                    }
                    uiState.copiedPoem?.let {
                        Toast.makeText(this, "poem copied to clipboard.", Toast.LENGTH_SHORT).show()
                    }
                    if(::poemViewPager.isInitialized && poemViewPager.currentItem != uiState.currentItemIndex)
                        poemViewPager.currentItem = uiState.currentItemIndex
                */
                }
                PoemListViewModel.UiState.Loading -> {}
            }
        }.launchIn(lifecycleScope)
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
