package com.sol.tmdb.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sol.tmdb.presebtation.movie.MovieDetail
import com.sol.tmdb.presebtation.movie.MoviesScreen
import com.sol.tmdb.presebtation.tv.TvScreen

@Composable
fun TmdbNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = TmdbScreen.Movie.route) {
        composable(TmdbScreen.Movie.route) { MoviesScreen(navController) }
        composable(
            route = TmdbScreen.MovieDetail.route + "/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { navBackStackEntry ->
            val movieId = navBackStackEntry.arguments?.getInt("movieId") ?: return@composable
            MovieDetail(movieId = movieId)
        }
        composable(TmdbScreen.Tv.route) { TvScreen() }
    }
}