package com.raven.khayam.poemList.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.raven.khayam.poemList.view.poem_list.ROUTE_POEM_LIST
import com.raven.khayam.poemList.view.poem_list.poemList
import com.raven.khayam.poemList.view.theme.KhayamTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            KhayamTheme {
                NavHost(
                    navController = navController,
                    startDestination = ROUTE_POEM_LIST,
                ) {
                    poemList(navController)
                }
            }
        }
    }
}