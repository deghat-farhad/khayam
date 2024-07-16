package com.vuxur.khayyam.pages.splashScreen.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.vuxur.khayyam.pages.splashScreen.SplashScreenViewModel

@Composable
fun SplashScreenRoute(
    viewModel: SplashScreenViewModel,
    navigateToSetting: () -> Unit,
    navigateToPoemList: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = true) {
        viewModel.viewIsReady()
    }

    DisposableEffect(uiState) {
        uiState.let { uiStateSnapshot ->
            (uiStateSnapshot as? SplashScreenViewModel.UiState.Initialized)?.let {
                uiStateSnapshot.events.forEach { event ->
                    when (event) {
                        SplashScreenViewModel.Event.NavigateToLanguageSetting -> navigateToSetting()
                        SplashScreenViewModel.Event.NavigateToPoemList -> navigateToPoemList()
                    }
                    viewModel.onEventConsumed(event)
                }
            }
        }
        onDispose { }
    }
    SplashScreenScreen()
}