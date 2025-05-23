package com.vuxur.khayyam.ui.pages.poemList.view.poem_list

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
import com.vuxur.khayyam.ui.pages.poemList.view.viewModel.PoemListViewModel
import com.vuxur.khayyam.utils.getCurrentLocale
import java.io.File

@Composable
fun PoemListRoute(
    viewModel: PoemListViewModel,
    navigateToSetting: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.viewIsReady(getCurrentLocale(context.resources))
    }

    DisposableEffect(uiState) {
        (uiState as? PoemListViewModel.UiState.Loaded)?.let { uiStateSnapshot ->
            uiStateSnapshot.events.forEach { event ->
                when (event) {
                    is PoemListViewModel.Event.CopyPoemText -> {
                        Toast.makeText(
                            context,
                            "poem copied to clipboard.",
                            Toast.LENGTH_SHORT
                        )
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

                    PoemListViewModel.Event.NavigateToLanguageSetting -> navigateToSetting()
                }
                viewModel.onEventConsumed(event)
            }
        }
        val uiStateSnapshot = uiState
        if (uiStateSnapshot is PoemListViewModel.UiState.Loaded) {

        }
        onDispose { }
    }

    when (val state = uiState) {
        is PoemListViewModel.UiState.Loaded -> {
            PoemListScreen(
                state.poems,
                state.currentItemIndex,
                viewModel::randomPoem,
                viewModel::navigateToNearestResult,
                viewModel::navigateToNextResult,
                viewModel::navigateToPreviousResult,
                viewModel::setCurrentPoemIndex,
                state.searchState.hasResult,
                state.searchState.hasNext,
                state.searchState.hasPrevious,
                onCopyPoemText = {
                    viewModel.copyPoem(clipboardManager)
                },
                onSharePoemText = viewModel::sharePoemText,
                onSharePoemImage = { bitmap ->
                    viewModel.sharePoemImage(bitmap)
                },
                onNavigateToSetting = navigateToSetting,
                translationItem = state.translation,
                showTranslationSnackbar = state.showTranslationDecision,
                onSetToUseUntranslated = viewModel::setToUseUntranslated,
                onChooseNotToUseUntranslated = viewModel::translationDecisionMade,
                highlightPhrase = state.searchState.searchPhrase
            )
        }

        is PoemListViewModel.UiState.Loading -> {
        }
    }
}

private fun getUriOf(context: Context, file: File) =
    FileProvider.getUriForFile(context, "com.vuxur.khayyam.fileprovider", file)