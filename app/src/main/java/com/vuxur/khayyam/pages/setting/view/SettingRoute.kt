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
        (uiState as? SettingViewModel.UiState.Loaded)?.let { uiStateSnapshot ->
            uiStateSnapshot.events.forEach { event ->
                when (event) {
                    SettingViewModel.Event.PopBack -> popBack()
                }
                viewModel.onEventConsumed(event)
            }
        }
        onDispose { }
    }
    (uiState as? SettingViewModel.UiState.Loaded)?.let { uiStateSnapshot ->
        SettingScreen(
            availableTranslations = uiStateSnapshot.availableTranslations,
            selectedTranslationOptionItem = uiStateSnapshot.selectedTranslationOption,
            onPopBack = viewModel::popBack,
            onSpesificTranslationSelection = { specificTranslationItem ->
                viewModel.setSpecificTranslation(specificTranslationItem)
            },
            onUseUntranslated = viewModel::setToUseUntranslated,
            onUseMatchSystemLanguageTranslation = viewModel::setToUseMatchSystemLanguageTranslation,
        )
    }
}