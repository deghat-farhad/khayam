package com.vuxur.khayyam.ui.pages.poemList.view.poem_list

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.core.content.FileProvider
import com.vuxur.khayyam.ui.pages.poemList.view.viewModel.PoemListViewModel
import com.vuxur.khayyam.utils.getCurrentLocale
import java.io.File
import kotlinx.coroutines.launch

@Composable
fun PoemListRoute(
    viewModel: PoemListViewModel,
    navigateToSetting: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val currentLocale = getCurrentLocale(LocalResources.current)

    LaunchedEffect(currentLocale) {
        viewModel.viewIsReady(currentLocale)
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
                        context.startActivity(
                            Intent.createChooser(event.shareIntent, "choose an app")
                        )
                    }

                    PoemListViewModel.Event.NavigateToLanguageSetting -> navigateToSetting()
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
                viewModel::navigateToNearestResult,
                viewModel::navigateToNextResult,
                viewModel::navigateToPreviousResult,
                viewModel::setCurrentPoemIndex,
                state.searchState.hasResult,
                state.searchState.hasNext,
                state.searchState.hasPrevious,
                onCopyPoemText = {
                    viewModel.copyPoem()?.let { poemText ->
                        coroutineScope.launch {
                            clipboard.setClipEntry(
                                ClipEntry(
                                    ClipData.newPlainText("Khayyam poem", poemText)
                                )
                            )
                        }
                    }
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
