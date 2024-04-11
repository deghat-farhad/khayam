package com.vuxur.khayyam.pages.setting.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.vuxur.khayyam.pages.setting.SettingViewModel

@Composable
fun SettingRoute(
    viewModel: SettingViewModel,
    popBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = true) {
        viewModel.viewIsReady()
    }

    DisposableEffect(uiState) {
        uiState.let { uiStateSnapshot ->
            when (uiStateSnapshot) {
                is SettingViewModel.UiState.Loaded -> {
                    uiStateSnapshot.events.forEach { event ->
                        when (event) {
                            SettingViewModel.Event.popBack -> popBack()
                        }
                        viewModel.onEventConsumed(event)
                    }
                }

                SettingViewModel.UiState.Loading -> {}
            }
        }
        onDispose { }
    }
    when (val uiStateSnapshot = uiState) {
        is SettingViewModel.UiState.Loaded ->
            SettingScreen(
                supportedLocales = uiStateSnapshot.supportedLocales,
                currentLocale = uiStateSnapshot.selectedPoemLocale,
            ) { selectedLocaleItem ->
                viewModel.setSelectedPoemLocale(selectedLocaleItem)
            }

        SettingViewModel.UiState.Loading -> {}
    }
}