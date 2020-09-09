package com.raven.khayam.poemList.view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raven.khayam.model.PoemItem

class PoemAdapter(
    private val poemList: List<PoemItem>
):  RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PoemHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return poemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PoemHolder) holder.bind(poemList[position])
    }

}