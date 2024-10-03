package com.sol.tmdb.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

fun openProvider(providerName: String, context: Context) {
    when {
        providerName.lowercase().contains("amazon") -> {
            openAppOrWebsiteProvider(
                "https://www.primevideo.com",
                "com.amazon.avod.thirdpartyclient",
                context
            )
        }

        providerName.lowercase().contains("apple") -> {
            openAppOrWebsiteProvider("https://tv.apple.com", "com.apple.atve.androidtv", context)
        }

        providerName.lowercase().contains("netflix") -> {
            openAppOrWebsiteProvider("https://www.netflix.com", "com.netflix.mediaclient", context)
        }

        providerName.equals("Disney Plus", ignoreCase = true) -> {
            openAppOrWebsiteProvider("https://www.disneyplus.com", "com.disney.disneyplus", context)
        }

        providerName.equals("Max", ignoreCase = true) -> {
            openAppOrWebsiteProvider("https://play.max.com", "com.hbo.hbonow", context)
        }

        providerName.equals("Paramount Plus", ignoreCase = true) -> {
            openAppOrWebsiteProvider("https://www.paramountplus.com/mx/", "com.cbs.ott", context)
        }

        providerName.equals("Hulu", ignoreCase = true) -> {
            openAppOrWebsiteProvider("https://www.hulu.com", "com.hulu.plus", context)
        }

        providerName.equals("YouTube TV", ignoreCase = true) -> {
            openAppOrWebsiteProvider(
                "https://tv.youtube.com",
                "com.google.android.youtube.tv",
                context
            )
        }

        providerName.equals("Peacock TV", ignoreCase = true) -> {
            openAppOrWebsiteProvider(
                "https://www.peacocktv.com",
                "com.peacocktv.peacockandroid",
                context
            )
        }

        providerName.equals("Star Plus", ignoreCase = true) -> {
            openAppOrWebsiteProvider("https://www.starplus.com", "com.disney.starplus", context)
        }

        providerName.equals("Crunchyroll", ignoreCase = true) -> {
            openAppOrWebsiteProvider(
                "https://www.crunchyroll.com",
                "com.crunchyroll.crunchyroid",
                context
            )
        }

        providerName.equals("Tubi", ignoreCase = true) -> {
            openAppOrWebsiteProvider("https://tubitv.com", "com.tubitv", context)
        }

        providerName.equals("Pluto TV", ignoreCase = true) -> {
            openAppOrWebsiteProvider("https://pluto.tv", "tv.pluto.android", context)
        }

        providerName.equals("VIX", ignoreCase = true) -> {
            openAppOrWebsiteProvider("https://www.vix.com", "com.blazeblue.vix", context)
        }

        providerName.equals("Claro Video", ignoreCase = true) -> {
            openAppOrWebsiteProvider("https://www.clarovideo.com", "com.clarovideo", context)
        }

        providerName.equals("Movistar Play", ignoreCase = true) -> {
            openAppOrWebsiteProvider(
                "https://play.movistar.com.mx",
                "com.movistar.android.play",
                context
            )
        }

        providerName.equals("DAZN", ignoreCase = true) -> {
            openAppOrWebsiteProvider("https://www.dazn.com", "com.dazn", context)
        }

        providerName.equals("FuboTV", ignoreCase = true) -> {
            openAppOrWebsiteProvider("https://www.fubo.tv", "tv.fubo.mobile", context)
        }

        providerName.equals("Sling TV", ignoreCase = true) -> {
            openAppOrWebsiteProvider("https://www.sling.com", "com.sling", context)
        }

        providerName.equals("Google Play Movies & TV", ignoreCase = true) -> {
            openAppOrWebsiteProvider(
                "https://play.google.com/store/movies",
                "com.google.android.videos",
                context
            )
        }

        providerName.equals("Rakuten TV", ignoreCase = true) -> {
            openAppOrWebsiteProvider("https://rakuten.tv", "com.rakuten.tv", context)
        }

        providerName.equals("MUBI", ignoreCase = true) -> {
            openAppOrWebsiteProvider("https://mubi.com", "com.mubi", context)
        }

        else -> Log.i("Provider", providerName)
    }
}

fun openAppOrWebsiteProvider(
    uriString: String,
    packageString: String,
    context: Context
) {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(uriString)
        intent.setPackage(packageString)
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        val webIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(uriString))
        context.startActivity(webIntent)
    }
}