package com.raven.khayam.poemList.compose_view.poem_list

import androidx.compose.runtime.Composable
import com.raven.khayam.poemList.PoemListViewModel

@Composable
fun PoemListRoute(
    viewModel: PoemListViewModel,
) {
    //val uiState by viewModel.state.collectAsState()
    PoemListScreen()
}