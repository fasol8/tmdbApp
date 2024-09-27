package com.sol.tmdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sol.tmdb.navigation.MainMenu
import com.sol.tmdb.ui.theme.TmdbTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
//    private val languageChangeHelper = LanguageChangeHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        val sharedPref = getSharedPreferences("AppSettings", MODE_PRIVATE)
//        val selectedLanguage = sharedPref.getString("selectedLanguage", "en") ?: "en"
//
//        // Verificar si el idioma actual es diferente del seleccionado
//        if (languageChangeHelper.getLanguageCode(this) != selectedLanguage) {
//            languageChangeHelper.changeLanguage(this, selectedLanguage)
//        }
        setContent {
            TmdbTheme {
                MainMenu()
            }
        }
    }


}