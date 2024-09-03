package com.sol.tmdb.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sol.tmdb.presebtation.movie.MoviesScreen
import com.sol.tmdb.presebtation.tv.TvScreen

@Composable
fun TmdbNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = TmdbScreen.Movie.route) {
        composable(TmdbScreen.Movie.route){ MoviesScreen()}
        composable(TmdbScreen.Tv.route){ TvScreen()}
    }
}