package com.sol.tmdb

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

class LanguageChangeHelper {
    fun changeLanguage(context: Context, languageCode: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) //version >=12
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(languageCode)
        else { //version<12
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
        }
    }

    fun getLanguageCode(context: Context): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales[0].toLanguageTag()
                ?.split("-")?.first() ?: "en"
        } else {
            AppCompatDelegate.getApplicationLocales()[0]?.toLanguageTag()?.split("-")?.first()
                ?: "en"
        }
    }
}