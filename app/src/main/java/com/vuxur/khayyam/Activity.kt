package com.vuxur.khayyam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vuxur.khayyam.pages.poemList.view.poem_list.ROUTE_POEM_LIST
import com.vuxur.khayyam.pages.poemList.view.poem_list.poemList
import com.vuxur.khayyam.pages.setting.view.setting
import com.vuxur.khayyam.theme.KhayyamTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            KhayyamTheme {
                NavHost(
                    navController = navController,
                    startDestination = ROUTE_POEM_LIST,
                ) {
                    poemList(navController)
                    setting(navController)
                }
            }
        }
    }
}