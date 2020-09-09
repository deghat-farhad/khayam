package com.raven.khayam.poemList.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raven.khayam.R
import com.raven.khayam.model.PoemItem

class PoemHolder(poemHolderView: View): RecyclerView.ViewHolder(poemHolderView) {
    private val txtViwPoem = poemHolderView.findViewById<TextView>(R.id.txtViwPoem)

    fun bind(poemItem: PoemItem){
        txtViwPoem.text = poemItem.text
    }

    companion object{
        fun create(parent: ViewGroup): PoemHolder{
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.poem_holder, parent, false)
            return PoemHolder(view)
        }
    }
}