package com.sol.tmdb

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

class LanguageChangeHelper {
    fun getLanguageCode(context: Context): String {
        val sharedPref = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        return sharedPref.getString("selectedLanguage", "en-US") ?: "en-US"
    }

    fun changeLanguage(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        // Guardar el idioma en SharedPreferences
        val sharedPref = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("selectedLanguage", languageCode)
            apply()
        }
    }
}