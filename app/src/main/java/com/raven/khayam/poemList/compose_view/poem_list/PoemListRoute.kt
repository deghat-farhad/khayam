package com.raven.khayam.poemList.compose_view.poem_list

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.raven.khayam.poemList.PoemListViewModel
import java.io.File

@Composable
fun PoemListRoute(
    viewModel: PoemListViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.viewIsReady()
    }

    DisposableEffect(uiState) {
        val localUiState = uiState
        if (localUiState is PoemListViewModel.UiState.Loaded) {
            localUiState.events.forEach { event ->
                when (event) {
                    is PoemListViewModel.Event.CopyPoemText -> {
                        Toast.makeText(context, "poem copied to clipboard.", Toast.LENGTH_SHORT)
                            .show()
                    }

                    is PoemListViewModel.Event.SharePoemImage -> {
                        viewModel.sharePoemImageUri(getUriOf(context, event.imageToShare))
                    }

                    is PoemListViewModel.Event.SharePoemText -> {
                        startActivity(
                            context,
                            Intent.createChooser(event.shareIntent, "choose an app"),
                            null
                        )
                    }
                }
                viewModel.onEventConsumed(event)
            }
        }
        onDispose { }
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
                onCopyPoemText = {
                    viewModel.copyPoem(clipboardManager)
                },
                onSharePoemText = viewModel::sharePoemText,
                onSharePoemImage = {}
            )
        }

        PoemListViewModel.UiState.Loading -> {}
    }
}

private fun getUriOf(context: Context, file: File) =
    FileProvider.getUriForFile(context, "com.raven.khayam.fileprovider", file)