@file:OptIn(ExperimentalMaterial3Api::class)

package com.sol.tmdb.presentation.movie

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sol.tmdb.R
import com.sol.tmdb.utils.SharedViewModel
import com.sol.tmdb.domain.model.movie.MovieResult
import com.sol.tmdb.navigation.TmdbScreen

@Composable
fun MoviesScreen(
    category: String,
    navController: NavController,
    sharedViewModel: SharedViewModel,
    viewModel: MovieViewModel = hiltViewModel(),
) {
    val isSearchIsVisible by sharedViewModel.isSearchBarVisible.observeAsState(false)
    val movies by if (isSearchIsVisible) {
        viewModel.movies.observeAsState(emptyList())
    } else {
        when (category) {
            "now_playing" -> viewModel.nowPlaying.observeAsState(emptyList())
            "popular" -> viewModel.popularMovies.observeAsState(emptyList())
            "top_rated" -> viewModel.topRatedMovies.observeAsState(emptyList())
            "upcoming" -> viewModel.upcomingMovies.observeAsState(emptyList())
            else -> viewModel.movies.observeAsState(emptyList())
        }
    }
    var query by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(true) {
        viewModel.observeLanguage(sharedViewModel)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp, top = 58.dp)
    ) {
        Column {
            if (isSearchIsVisible) {
                MovieSearchBar(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = { viewModel.searchMovie(query) }
                )
            } else {
                Spacer(modifier = Modifier.height(38.dp))
            }
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(movies.size) { index ->
                    val movie = movies[index]
                    ItemMovie(movie) {
                        navController.navigate(TmdbScreen.MovieDetail.route + "/${movie.id}")
                    }

                    if (index == movies.size - 1) {
                        LaunchedEffect(true) {
                            if (isSearchIsVisible) {
                                viewModel.searchMovie(query)
                            } else {
                                whenCategoryMovie(category, viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieSearchBar(query: String, onQueryChange: (String) -> Unit, onSearch: (String) -> Unit) {
    var activate by remember { mutableStateOf(false) }

    SearchBar(
        query = query,
        onQueryChange = {
            val capitalizedQuery = it.replaceFirstChar { char ->
                if (char.isLowerCase()) char.titlecase() else char.toString()
            }
            onQueryChange(capitalizedQuery)
        },
        onSearch = {
            onSearch(it)
            activate = false
        },
        active = activate,
        onActiveChange = { activate = true },
        placeholder = { Text(text = stringResource(R.string.search_movie_placeholder)) },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Movie")
        },
        trailingIcon = {
            Row {
                IconButton(onClick = {
                    activate = false
                    onQueryChange("")
                    onSearch("")
                }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close SearchBar")
                }
            }
        }
    ) {}
}

@Composable
fun ItemMovie(movie: MovieResult, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                val image = if (movie.posterPath.isNullOrEmpty()) {
                    R.drawable.no_image
                } else {
                    "https://image.tmdb.org/t/p/w500${movie.posterPath}"
                }
                AsyncImage(
                    model = image, contentDescription = stringResource(R.string.poster_movie_description),
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.no_image),
                    error = painterResource(id = R.drawable.no_image)
                )
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 8.dp, start = 8.dp)
                )
                Text(
                    text = movie.releaseDate,
                    modifier = Modifier.padding(top = 4.dp, start = 16.dp)
                )
            }
            if (movie.voteAverage.toInt() != 0) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.BottomEnd) // Posiciona el círculo en la esquina inferior derecha
                        .offset(x = (-8).dp, y = (-48).dp),
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Dibuja el fondo del círculo
                        drawCircle(color = Color(0xFFEEEEEE))

                        // Dibuja el arco que representa el porcentaje
                        drawArc(
                            color = when {
                                ((movie.voteAverage * 10).toInt()) < 30 -> Color(0xFFEF5350)
                                ((movie.voteAverage * 10).toInt()) < 60 -> Color(0xFFFFCA28)
                                else -> Color(0xFF0F9D58)
                            },
                            startAngle = -90f,
                            sweepAngle = (movie.voteAverage * 36).toFloat(), // 10 * 36 = 360 grados (cierre del círculo)
                            useCenter = false,
                            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    Text(
                        text = "${(movie.voteAverage * 10).toInt()}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

fun whenCategoryMovie(category: String, viewModel: MovieViewModel) = when (category) {
    "now_playing" -> viewModel.loadNowPlaying()
    "popular" -> viewModel.loadPopularMovies()
    "top_rated" -> viewModel.loadTopRatedMovies()
    "upcoming" -> viewModel.loadUpcomingMovies()
    else -> viewModel.loadMovies()
}