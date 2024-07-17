package com.vuxur.khayyam.pages.poemList.view.poem_list

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Translate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.vuxur.khayyam.R
import com.vuxur.khayyam.pages.poemList.view.viewModel.PoemListViewModel
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
        viewModel.viewIsReady()
    }

    DisposableEffect(uiState) {
        uiState.let { uiStateSnapshot ->
            when (uiStateSnapshot) {
                is PoemListViewModel.UiState.Loaded -> {
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

                is PoemListViewModel.UiState.Loading -> {
                    uiStateSnapshot.events.forEach { event ->
                        when (event) {
                            is PoemListViewModel.Event.CopyPoemText -> {
                                throw (IllegalStateException("there is no poem to copy in \"PreLoad\" state."))
                            }

                            is PoemListViewModel.Event.SharePoemImage -> {
                                throw (IllegalStateException("there is no poem to share in \"PreLoad\" state."))
                            }

                            is PoemListViewModel.Event.SharePoemText -> {
                                throw (IllegalStateException("there is no poem to share in \"PreLoad\" state."))
                            }

                            PoemListViewModel.Event.NavigateToLanguageSetting -> navigateToSetting()
                        }
                        viewModel.onEventConsumed(event)
                    }
                }
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
                localeItem = state.selectedLocaleItem,
            )
        }

        is PoemListViewModel.UiState.Loading -> {
            if (state.showLanguageSettingDialog) {
                KAlertDialog(
                    onDismissRequest = viewModel::useSystemLanguage,
                    onConfirmation = viewModel::navigateToSetting,
                    dialogTitle = stringResource(R.string.choose_poem_language),
                    dialogText = stringResource(R.string.please_select_your_preferred_language_for_reading_the_poems_in_the_app),
                    icon = Icons.Default.Translate,
                    dismissButtonText = stringResource(R.string.use_system_language),
                    confirmButtonText = stringResource(R.string.set_language),
                )
            }
        }
    }
}

private fun getUriOf(context: Context, file: File) =
    FileProvider.getUriForFile(context, "com.vuxur.khayyam.fileprovider", file)