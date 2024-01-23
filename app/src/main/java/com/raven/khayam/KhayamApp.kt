package com.raven.khayam

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.raven.khayam.utils.LocaleUtil
import dagger.hilt.android.HiltAndroidApp

const val FORCE_TO_PERSIAN = true
@HiltAndroidApp
class KhayamApp: Application() {
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