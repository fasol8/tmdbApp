package com.sol.tmdb.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openYoutubeVideo(context: Context, videoKey: String) {
    val videoUrl = "https://www.youtube.com/watch?v=$videoKey"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
    intent.setPackage("com.google.android.youtube")

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
        context.startActivity(webIntent)
    }
}