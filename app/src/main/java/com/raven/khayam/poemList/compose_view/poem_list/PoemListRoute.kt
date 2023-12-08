package com.raven.khayam.poemList.compose_view.poem_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.raven.khayam.poemList.PoemListViewModel

@Composable
fun PoemListRoute(
    viewModel: PoemListViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.viewIsReady()
    }

    when (val state = uiState) {
        is PoemListViewModel.UiState.Loaded -> {
            PoemListScreen(
                state.poems,
                state.currentItemIndex,
                viewModel::randomPoem,
                viewModel::findNearestPoem,
                viewModel::onNextResult,
                viewModel::onPreviousResult,
                viewModel::setCurrentPoemIndex,
                state.isThereAnyResult,
                state.isThereNextResult,
                state.isTherePreviousResult,
            )
        }

        PoemListViewModel.UiState.Loading -> {}
    }
}