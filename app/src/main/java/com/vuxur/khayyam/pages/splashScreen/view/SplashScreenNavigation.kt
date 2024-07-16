package com.vuxur.khayyam.pages.splashScreen.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vuxur.khayyam.pages.poemList.view.poem_list.navigateToPoemListSingleTop
import com.vuxur.khayyam.pages.setting.view.navigateToSettingPopBackStack

const val SPLASH_SCREEN_ROUTE = "splash_screen_route"

fun NavController.navigateToSplashScreen() {
    this.navigate(SPLASH_SCREEN_ROUTE)
}

fun NavGraphBuilder.splashScreen(
    navController: NavController
) {
    composable(SPLASH_SCREEN_ROUTE) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SplashScreenRoute(
                viewModel = hiltViewModel(),
                navigateToSetting = navController::navigateToSettingPopBackStack,
                navigateToPoemList = navController::navigateToPoemListSingleTop,
                navController::popBackStack
            )
        }
    }
}