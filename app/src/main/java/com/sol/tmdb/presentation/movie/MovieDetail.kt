package com.sol.tmdb.presentation.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.sol.tmdb.domain.model.movie.Cast
import com.sol.tmdb.domain.model.movie.Crew
import com.sol.tmdb.domain.model.movie.MovieCredits
import com.sol.tmdb.domain.model.movie.MovieDetail
import com.sol.tmdb.domain.model.movie.MovieGenre
import com.sol.tmdb.domain.model.movie.MovieRecommendationResult
import com.sol.tmdb.domain.model.movie.MovieSimilarResult
import com.sol.tmdb.navigation.TmdbScreen


@Composable
fun MovieDetail(
    movieId: Int,
    navController: NavController,
    viewModel: MovieViewModel = hiltViewModel()
) {
    val movie by viewModel.movieById.observeAsState()
    val movieCredits by viewModel.movieCredits.observeAsState()
    val movieSimilar by viewModel.movieSimilar.observeAsState(emptyList())
    val movieRecommendation by viewModel.movieRecommendation.observeAsState(emptyList())

    val errorMessage by viewModel.errorMessage.observeAsState()

    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(movieId) {
        viewModel.searchMovieById(movieId)
        viewModel.searchMovieCredits(movieId)
        viewModel.searchMovieSimilar(movieId)
        viewModel.searchMovieRecommendation(movieId)
    }

    LaunchedEffect(movie) {
        if (movie != null)
            isLoading.value = true
    }

    when {
        movie != null -> {
            if (movieCredits != null) {
                LazyColumn {
                    item {
                        MovieCard(
                            movie,
                            movieCredits!!,
                            movieSimilar,
                            movieRecommendation,
                            navController
                        )
                    }
                }
            }
        }

        isLoading.value -> {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        else -> {
            Text(
                text = errorMessage ?: "Unknown error",
                modifier = Modifier.padding(16.dp, top = 96.dp)
            )
        }
    }
}

@Composable
fun MovieCard(
    movie: MovieDetail?,
    movieCredits: MovieCredits?,
    movieSimilar: List<MovieSimilarResult?>,
    movieRecommendation: List<MovieRecommendationResult?>,
    navController: NavController
) {
    val imageBackground = "https://image.tmdb.org/t/p/w500" + movie!!.backdropPath
    val imagePoster = "https://image.tmdb.org/t/p/w500" + movie.posterPath
    val movieCast = movieCredits!!.cast
    val movieCrew = movieCredits.crew

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.5f),
                        Color.Transparent
                    )
                )
            )
            .paint(
                painter = rememberAsyncImagePainter(model = imageBackground),
                contentScale = ContentScale.FillHeight,
                alpha = 0.3f
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // Centra el contenido horizontalmente
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 96.dp, bottom = 16.dp)
                    .offset(y = 18.dp) // Desplaza la imagen hacia abajo, superponiéndola sobre la tarjeta
                    .zIndex(1f) // Asegura que la imagen esté sobre la tarjeta
            ) {
                AsyncImage(
                    model = imagePoster,
                    contentDescription = "Poster movie",
                    modifier = Modifier
                        .height(300.dp)
                        .aspectRatio(0.66f) // Mantiene la relación de aspecto típica de un póster
                        .clip(RoundedCornerShape(8.dp)) // Esquina redondeada opcional
                )
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 16.dp)
            ) {
                Column(Modifier.padding(horizontal = 8.dp)) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 24.sp
                    )
                    val genreNames = movie.genres.mapNotNull { MovieGenre.fromId(it.id)?.genreName }
                    Text(
                        text = genreNames.joinToString(", "),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.padding(2.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = movie.overview, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Cast:")
                    LazyRow {
                        items(movieCast.size) { index ->
                            val cast = movieCast[index]
                            ItemCast(cast) {
                                navController.navigate(TmdbScreen.PersonDetail.route + "/${cast.id}")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Crew:")
                    LazyRow {
                        items(movieCrew.size) { index ->
                            val crew = movieCrew[index]
                            ItemCrew(crew) {
                                navController.navigate(TmdbScreen.PersonDetail.route + "/${crew.id}")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Movie Similar:")
                    LazyRow {
                        items(movieSimilar.size) { index ->
                            val movieSim = movieSimilar[index]
                            ItemMovieSimilar(movieSim) {
                                navController.navigate(TmdbScreen.MovieDetail.route + "/${movieSim?.id}")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Movie Recommendation:")
                    LazyRow {
                        items(movieRecommendation.size) { index ->
                            val movieRec = movieRecommendation[index]
                            ItemMovieRecommendation(movieRec) {
                                navController.navigate(TmdbScreen.MovieDetail.route + "/${movieRec?.id}")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(64.dp))
                }
            }
        }
    }
}

@Composable
fun ItemCast(cast: Cast, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .width(120.dp),
        onClick = { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.align(Alignment.TopStart)) {
                val image = ("https://image.tmdb.org/t/p/w500" + cast.profilePath) ?: ""
                AsyncImage(
                    model = image, contentDescription = "poster movie",
                    modifier = Modifier.width(120.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = cast.name,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 8.dp, start = 2.dp)
                )
                Text(
                    text = cast.character,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 2.dp, start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ItemCrew(crew: Crew, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .width(100.dp),
        onClick = { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.align(Alignment.TopStart)) {
                val image = ("https://image.tmdb.org/t/p/w500" + crew.profilePath) ?: ""
                AsyncImage(
                    model = image, contentDescription = "poster movie",
                    modifier = Modifier.width(100.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = crew.name,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 8.dp, start = 2.dp)
                )
                Text(
                    text = crew.department,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 2.dp, start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ItemMovieSimilar(movieSim: MovieSimilarResult?, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .width(200.dp)
            .height(150.dp),
        onClick = { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.align(Alignment.TopStart)) {
                val image = ("https://image.tmdb.org/t/p/w500" + movieSim!!.posterPath) ?: ""
                AsyncImage(
                    model = image, contentDescription = "poster movie",
                    modifier = Modifier
                        .width(200.dp)
                        .height(130.dp),
                    contentScale = ContentScale.Crop
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = movieSim.title,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    )
                    Text(
                        text = "${(movieSim.voteAverage * 10).toInt()}%",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}

@Composable
fun ItemMovieRecommendation(movieRec: MovieRecommendationResult?, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .width(200.dp)
            .height(150.dp),
        onClick = { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.align(Alignment.TopStart)) {
                val image = ("https://image.tmdb.org/t/p/w500" + movieRec!!.posterPath) ?: ""
                AsyncImage(
                    model = image, contentDescription = "poster movie",
                    modifier = Modifier
                        .width(200.dp)
                        .height(130.dp),
                    contentScale = ContentScale.Crop
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = movieRec.title,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    )
                    Text(
                        text = "${(movieRec.voteAverage * 10).toInt()}%",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}
