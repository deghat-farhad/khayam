package com.raven.khayam.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.Locale

/**
 * Helps to change locales configuration for [Context] objects.
 * Remember that there are 3 types of [Context.getResources] layers:
 *
 * 1. Top-level resources (ex: manifest activity name)
 *
 * 2. Application resources
 *
 * 3. Activity resources
 *
 * So, changing the Application layer won't affect the activity layer.
 */
object LocaleUtil {

    const val DEFAULT_LANGUAGE = "fa"
    const val DEFAULT_COUNTRY = "ir"

    /**
     * Constraint This [context] Locale.
     * @param constrainedCountry - the country to match the activity for
     * @param constrainedLanguage - the language inside that country to match the activity for
     * @return new / same instance configured [Context]. (depends on Android OS version)
     */
    fun constrainConfigurationLocale(
        context: Context,
        constrainedCountry: String = DEFAULT_COUNTRY,
        constrainedLanguage: String = DEFAULT_LANGUAGE
    ): Context {
        val newConf = constrainConfigurationLocale(
            context.resources.configuration,
            constrainedCountry,
            constrainedLanguage
        )
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
            context.createConfigurationContext(newConf)
        else
            context
    }

    /**
     * Constraint This [configuration] Locale.
     * @param constrainedCountry - the country to match the activity for
     * @param constrainedLanguage - the language inside that country to match the activity for
     * @return new / same instance of [Configuration]. (depends on Android OS version)
     */
    fun constrainConfigurationLocale(
        currentConfiguration: Configuration,
        constrainedCountry: String = DEFAULT_COUNTRY,
        constrainedLanguage: String = DEFAULT_LANGUAGE
    ): Configuration {

        val configuration = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
            Configuration(currentConfiguration)
        else
            currentConfiguration
        val synthesizedLocale = Locale(constrainedLanguage, constrainedCountry)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val oldLocales = configuration.locales
            @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
            if (!oldLocales.isEmpty) {
                if (!oldLocales[0].language!!.contentEquals(synthesizedLocale.language)) {
                    var newLocales = arrayOfNulls<Locale>(oldLocales.size() + 1)
                    newLocales[0] = synthesizedLocale // first locale determines layout direction
                    var deductionCount = 0
                    for (i in 0 until oldLocales.size()) {
                        if (newLocales[0]?.language?.contentEquals(oldLocales[i].language) != true) // add only different locale if not null
                            newLocales[i + 1 - deductionCount] = oldLocales[i]
                        else {
                            val temp = arrayOfNulls<Locale>(newLocales.size - 1)
                            for (j in 0..i) {
                                temp[j] = newLocales[j]
                            }
                            newLocales = temp
                            deductionCount++
                        }
                    }
                    configuration.setLocales(LocaleList(*newLocales))
                }
            } else {
                configuration.setLocales(LocaleList(synthesizedLocale))
            }
        } else {
            @Suppress("DEPRECATION", "UNNECESSARY_NOT_NULL_ASSERTION")
            if (configuration.locale == null || !synthesizedLocale.language!!.contentEquals(
                    configuration.locale.language!!
                )
            )
                configuration.setLocale(synthesizedLocale)
        }
        return configuration
    }
}