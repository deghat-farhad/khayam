package com.raven.khayam.poemList.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.raven.khayam.R
import com.raven.khayam.model.PoemItem

class FragPoem private constructor(private val poemItem: PoemItem) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.poem_holder, container, false)
        initiate(fragment)
        return fragment
    }

    private fun initiate(fragment: View){
        fragment.findViewById<TextView>(R.id.txtViwPoem).text = poemItem.text
        fragment.findViewById<CardView>(R.id.cardViwPoem).setOnClickListener { fullScreen() }
    }

    private fun fullScreen() {
        activity?.let {
            it.window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                    )
        }
    }

    companion object {
        fun create(poemItem: PoemItem): FragPoem {
            return FragPoem(poemItem)
        }
    }
}