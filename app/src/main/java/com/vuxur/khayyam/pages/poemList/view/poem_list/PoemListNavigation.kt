package com.vuxur.khayyam.pages.poemList.view.poem_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vuxur.khayyam.pages.setting.view.navigateToSetting

const val ROUTE_POEM_LIST = "poem_list_route"
fun NavGraphBuilder.poemList(
    navController: NavController
) {
    composable(ROUTE_POEM_LIST) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            PoemListRoute(
                viewModel = hiltViewModel(),
                navController::navigateToSetting,
            )
        }
    }
}