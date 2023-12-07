package com.raven.khayam.poemList.compose_view.poem_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raven.khayam.model.PoemItem
import com.raven.khayam.poemList.compose_view.PoemView

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PoemHorizontalPager(poemList: List<PoemItem>) {
    val pagerState = rememberPagerState(pageCount = {
        poemList.size
    })
    HorizontalPager(state = pagerState) { page ->
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                PoemView(
                    modifier = Modifier.padding(horizontal = 56.dp),
                    poemItem = poemList[page]
                )
            }
        }
    }
}