package com.raven.khayam.poemList.compose_view.poem_list

import androidx.compose.runtime.Composable
import com.raven.khayam.model.PoemItem

@Composable
fun PoemListScreen(
    poemList: List<PoemItem>
) {
    PoemHorizontalPager(poemList)
}