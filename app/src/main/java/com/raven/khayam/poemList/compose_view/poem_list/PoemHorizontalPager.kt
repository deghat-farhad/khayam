package com.raven.khayam.poemList.compose_view.poem_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raven.khayam.model.PoemItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PoemHorizontalPager(
    modifier: Modifier = Modifier,
    poemList: List<PoemItem>,
    currentPoemIndex: Int,
    setCurrentPoemIndex: (Int) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = {
        poemList.size
    })
    LaunchedEffect(pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress && pagerState.currentPage != currentPoemIndex)
            setCurrentPoemIndex(pagerState.currentPage)
    }
    LaunchedEffect(key1 = currentPoemIndex) {
        if (currentPoemIndex != pagerState.currentPage)
            pagerState.animateScrollToPage(currentPoemIndex)
    }
    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) { page ->
        Card(
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 8.dp)
                .fillMaxSize()
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