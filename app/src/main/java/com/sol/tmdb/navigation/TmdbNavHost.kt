package com.sol.tmdb.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sol.tmdb.presentation.movie.MovieDetail
import com.sol.tmdb.presentation.movie.MoviesScreen
import com.sol.tmdb.presentation.person.PersonDetail
import com.sol.tmdb.presentation.person.PersonScreen
import com.sol.tmdb.presentation.tv.TvDetail
import com.sol.tmdb.presentation.tv.TvScreen

@Composable
fun TmdbNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = TmdbScreen.Movie.route) {
        composable(TmdbScreen.Movie.route) { MoviesScreen(navController) }
        composable(
            route = TmdbScreen.MovieDetail.route + "/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { navBackStackEntry ->
            val movieId = navBackStackEntry.arguments?.getInt("movieId") ?: return@composable
            MovieDetail(movieId = movieId, navController = navController)
        }
        composable(TmdbScreen.Tv.route) { TvScreen(navController) }
        composable(
            route = TmdbScreen.TvDetail.route + "/{tvId}",
            arguments = listOf(navArgument("tvId") { type = NavType.IntType })
        ) { navBackStackEntry ->
            val tvId = navBackStackEntry.arguments?.getInt("tvId") ?: return@composable
            TvDetail(tvId = tvId)
        }
        composable(TmdbScreen.Person.route) { PersonScreen(navController) }
        composable(
            TmdbScreen.PersonDetail.route + "/{personId}",
            arguments = listOf(navArgument("personId") { type = NavType.IntType })
        ) { navBackStackEntry ->
            val personId = navBackStackEntry.arguments?.getInt("personId") ?: return@composable
            PersonDetail(personId = personId)
        }
    }
}