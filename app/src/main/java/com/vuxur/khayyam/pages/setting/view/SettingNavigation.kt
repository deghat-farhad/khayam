package com.vuxur.khayyam.pages.setting.view

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

const val ROUTE_SETTING = "setting_route"

fun NavController.navigateToSettingPopBackStack() {
    this.navigate(ROUTE_SETTING) {
        popBackStack()
    }
}

fun NavController.navigateToSetting() {
    this.navigate(ROUTE_SETTING)
}

fun NavGraphBuilder.setting(
    navController: NavController
) {
    composable(ROUTE_SETTING) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SettingRoute(
                viewModel = hiltViewModel(),
                navigateToPoemListSingleTop = navController::navigateToPoemListSingleTop,
            )
        }
    }
}