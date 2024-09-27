package com.sol.tmdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sol.tmdb.navigation.MainMenu
import com.sol.tmdb.ui.theme.TmdbTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val selectedLanguage = sharedPref.getString("selectedLanguage", "en-US") ?: "en-US"
        setLocale(selectedLanguage)

        enableEdgeToEdge()
        setContent {
            TmdbTheme {
                MainMenu()
            }
        }
    }

    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}