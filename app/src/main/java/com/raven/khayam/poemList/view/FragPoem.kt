package com.raven.khayam.poemList.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.raven.khayam.R
import com.raven.khayam.model.PoemItem

class FragPoem private constructor(
    private val poemItem: PoemItem
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.poem_holder, container, false)
        initiate(fragment)
        return fragment
    }

    private fun initiate(fragment: View) {
        fragment.findViewById<TextView>(R.id.txtViwHemistich1).text =
            String.format("  %s", poemItem.hemistich1)
        fragment.findViewById<TextView>(R.id.txtViwHemistich2).text =
            String.format("  %s", poemItem.hemistich2)
        fragment.findViewById<TextView>(R.id.txtViwHemistich3).text =
            String.format("  %s", poemItem.hemistich3)
        fragment.findViewById<TextView>(R.id.txtViwHemistich4).text =
            String.format("  %s", poemItem.hemistich4)

        fragment.findViewById<TextView>(R.id.txtViwId).text =
        if (poemItem.isSuspicious)
            String.format("%s !", poemItem.id)
            else
            poemItem.id.toString()
    }

    companion object {
        fun create(poemItem: PoemItem): FragPoem {
            return FragPoem(poemItem)
        }
    }
}