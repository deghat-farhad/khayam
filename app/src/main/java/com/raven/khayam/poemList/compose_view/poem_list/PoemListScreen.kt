package com.raven.khayam.poemList.compose_view.poem_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raven.khayam.model.PoemItem

@Composable
fun PoemListScreen(
    poemList: List<PoemItem>,
    currentPoemIndex: Int,
    onRandomPoem: () -> Unit,
    onSearch: (String) -> Unit
) {
    Column(
        Modifier
            .systemBarsPadding()
            .imePadding()
    ) {
        Box(
            modifier = Modifier.weight(1.0f),
            contentAlignment = Alignment.BottomEnd
        ) {
            PoemHorizontalPager(
                modifier = Modifier.fillMaxSize(),
                poemList = poemList,
                currentPoemIndex = currentPoemIndex
            )
            FloatingActionButton(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                onClick = { onRandomPoem() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Shuffle,
                    contentDescription = null
                )
            }
        }
        KSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 20.dp, end = 20.dp),
        ) {
            onSearch(it)
        }
    }
}