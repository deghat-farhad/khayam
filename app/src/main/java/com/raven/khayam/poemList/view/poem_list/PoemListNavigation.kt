package com.raven.khayam.poemList.view.poem_list

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel

const val ROUTE_POEM_LIST = "poem_list_route"
fun NavGraphBuilder.poemList(
    navController: NavController
) {
    composable(ROUTE_POEM_LIST) {
        PoemListRoute(
            viewModel = hiltViewModel(),
        )
    }
}