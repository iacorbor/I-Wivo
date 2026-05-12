package com.icb.iwivo.data.repository

import android.content.Context

class SettingsRepository(context: Context) {

    companion object {
        const val LANGUAGE_SPANISH = "es"
        const val LANGUAGE_ENGLISH = "en"

        private const val PREFS_NAME = "iwivo_settings"
        private const val KEY_SOUND_ENABLED = "sound_enabled"
        private const val KEY_HAPTICS_ENABLED = "haptics_enabled"
        private const val KEY_SELECTED_LANGUAGE = "selected_language"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isSoundEnabled(): Boolean {
        return prefs.getBoolean(KEY_SOUND_ENABLED, true)
    }

    fun setSoundEnabled(enabled: Boolean) {
        prefs.edit()
            .putBoolean(KEY_SOUND_ENABLED, enabled)
            .apply()
    }

    fun isHapticsEnabled(): Boolean {
        return prefs.getBoolean(KEY_HAPTICS_ENABLED, true)
    }

    fun setHapticsEnabled(enabled: Boolean) {
        prefs.edit()
            .putBoolean(KEY_HAPTICS_ENABLED, enabled)
            .apply()
    }

    fun getSelectedLanguage(): String {
        return prefs.getString(KEY_SELECTED_LANGUAGE, LANGUAGE_SPANISH)
            ?: LANGUAGE_SPANISH
    }

    fun setSelectedLanguage(language: String) {
        val safeLanguage = when (language) {
            LANGUAGE_SPANISH -> LANGUAGE_SPANISH
            LANGUAGE_ENGLISH -> LANGUAGE_ENGLISH
            else -> LANGUAGE_SPANISH
        }

        prefs.edit()
            .putString(KEY_SELECTED_LANGUAGE, safeLanguage)
            .apply()
    }
}