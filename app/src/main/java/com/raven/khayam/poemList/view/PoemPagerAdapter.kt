package com.raven.khayam.poemList.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.raven.khayam.model.PoemItem


class PoemPagerAdapter(
    fragmentActivity: FragmentActivity,
    val poemList: List<PoemItem>
) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return FragPoem.create(poemList[position])
    }

    override fun getItemCount(): Int {
        return poemList.size
    }
}