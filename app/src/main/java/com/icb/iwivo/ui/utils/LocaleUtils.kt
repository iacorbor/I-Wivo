package com.icb.iwivo.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import java.util.Locale

object LocaleUtils {

    fun applyLocale(context: Context, languageCode: String): ContextWrapper {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        val localizedContext = context.createConfigurationContext(configuration)

        return ContextWrapper(localizedContext)
    }
}