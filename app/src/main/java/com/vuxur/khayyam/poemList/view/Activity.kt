package com.vuxur.khayyam.poemList.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vuxur.khayyam.FORCE_TO_PERSIAN
import com.vuxur.khayyam.poemList.view.poem_list.ROUTE_POEM_LIST
import com.vuxur.khayyam.poemList.view.poem_list.poemList
import com.vuxur.khayyam.poemList.view.theme.KhayyamTheme
import com.vuxur.khayyam.utils.LocaleUtil
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
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        if (FORCE_TO_PERSIAN) {
            val constrainedBaseCtx = LocaleUtil.constrainConfigurationLocale(newBase)
            super.attachBaseContext(constrainedBaseCtx)
        } else
            super.attachBaseContext(newBase)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (FORCE_TO_PERSIAN) {
            val constrainedConfiguration = LocaleUtil.constrainConfigurationLocale(newConfig)
            super.onConfigurationChanged(constrainedConfiguration)
        } else
            super.onConfigurationChanged(newConfig)
    }

    override fun createConfigurationContext(overrideConfiguration: Configuration): Context {
        if (FORCE_TO_PERSIAN) {
            val constrainedConf = LocaleUtil.constrainConfigurationLocale(overrideConfiguration)
            return super.createConfigurationContext(constrainedConf)
        }
        return super.createConfigurationContext(overrideConfiguration)
    }
}