package com.sol.tmdb.presentation.tv

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.sol.tmdb.domain.model.tv.TvDetail

@Composable
fun TvDetail(tvId: Int, viewModel: TvViewModel = hiltViewModel()) {
    val tv by viewModel.tvById.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()

    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(tvId) {
        viewModel.searchTvById(tvId)
    }

    LaunchedEffect(tv) {
        if (tv != null)
            isLoading.value = true
    }

    when {
        tv != null -> {
            TvCard(tv!!)
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
fun TvCard(tv: TvDetail) {
    val imageBackground = "https://image.tmdb.org/t/p/w500" + tv.backdropPath
    val imagePoster = "https://image.tmdb.org/t/p/w500" + tv.posterPath

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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 96.dp)
                    .offset(y = 18.dp)
                    .zIndex(1f)
            ) {
                AsyncImage(
                    model = imagePoster,
                    contentDescription = "Poster tv",
                    modifier = Modifier
                        .height(300.dp)
                        .aspectRatio(0.66f)
                        .clip(RoundedCornerShape(8.dp))
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
                        text = tv.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 24.sp
                    )
                    Text(text = "Genres: ${tv.genres.size}")
                    Text(text = tv.tagline, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = tv.overview, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
