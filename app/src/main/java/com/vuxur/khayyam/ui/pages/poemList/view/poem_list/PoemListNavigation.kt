package com.vuxur.khayyam.ui.pages.poemList.view.poem_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.vuxur.khayyam.ui.pages.setting.view.navigateToSetting

const val ROUTE_POEM_LIST = "poem_list_route"
fun NavGraphBuilder.poemList(
    navController: NavController
) {
    composable(
        route = ROUTE_POEM_LIST,
        arguments = listOf(
            navArgument("initial_poem_id") {
                type = NavType.IntType
                defaultValue = -1
                nullable = false
            }
        ),
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "khayyam://poem_list?poem_id={initial_poem_id}"
            }
        )
    ) {
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