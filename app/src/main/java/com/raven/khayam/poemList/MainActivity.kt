package com.raven.khayam.poemList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.raven.khayam.R
import com.raven.khayam.di.DaggerViewModelComponent
import com.raven.khayam.di.ViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ViewModelPoemList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        injectThisToDagger()
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ViewModelPoemList::class.java)
    }

    private fun injectThisToDagger(){
        DaggerViewModelComponent.builder().build().injectActivity(this)
    }
}
