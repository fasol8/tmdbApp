package com.sol.tmdb.presentation.movie

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.sol.tmdb.domain.model.movie.MovieDetail
import com.sol.tmdb.domain.model.movie.MovieGenre

@Composable
fun MovieDetail(movieId: Int, viewModel: MovieViewModel = hiltViewModel()) {
    val movie by viewModel.movieById.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()

    val isLoading = remember { mutableStateOf(true) }


    LaunchedEffect(movieId) {
        viewModel.searchMovieById(movieId)
    }

    LaunchedEffect(movie) {
        if (movie != null)
            isLoading.value = true
    }

    when {
        movie != null -> {
            MovieCard(movie!!)
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
fun MovieCard(movie: MovieDetail?) {
    val imageBackground = "https://image.tmdb.org/t/p/w500" + movie!!.backdropPath
    val imagePoster = "https://image.tmdb.org/t/p/w500" + movie.posterPath

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
                    .padding(top = 96.dp)
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
                    Text(text = movie.tagline, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = movie.overview, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
