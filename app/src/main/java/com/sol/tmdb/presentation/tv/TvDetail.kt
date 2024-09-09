package com.sol.tmdb.presentation.tv

import android.util.Log
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
import com.sol.tmdb.domain.model.tv.CreditsResponse
import com.sol.tmdb.domain.model.tv.TvCast
import com.sol.tmdb.domain.model.tv.TvCrew
import com.sol.tmdb.domain.model.tv.TvDetail
import com.sol.tmdb.domain.model.tv.TvGenre
import com.sol.tmdb.navigation.TmdbScreen

@Composable
fun TvDetail(tvId: Int, navController: NavController, viewModel: TvViewModel = hiltViewModel()) {
    val tv by viewModel.tvById.observeAsState()
    val credits by viewModel.tvCredits.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()

    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(tvId) {
        viewModel.searchTvById(tvId)
        viewModel.searchTvCredits(tvId)
    }

    LaunchedEffect(tv) {
        if (tv != null)
            isLoading.value = true
    }

    when {
        tv != null -> {
            if (credits != null) {
                LazyColumn {
                    item {
                        TvCard(tv!!, credits, navController)
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
fun TvCard(tv: TvDetail, credits: CreditsResponse?, navController: NavController) {
    val imageBackground = "https://image.tmdb.org/t/p/w500" + tv.backdropPath
    val imagePoster = "https://image.tmdb.org/t/p/w500" + tv.posterPath
    val cast = credits?.cast ?: emptyList()
    val crew = credits?.crew ?: emptyList()

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
                    val genreNames = tv.genres.mapNotNull { TvGenre.fromId(it.id)?.genreName }
                    Text(
                        text = genreNames.joinToString(", "),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.padding(2.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = tv.overview, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Cast:")
                    LazyRow {
                        items(cast.size) { index ->
                            val oneCast = cast[index]
                            ItemCast(oneCast) {
                                navController.navigate(TmdbScreen.PersonDetail.route + "/${oneCast.id}")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Crew:")
                    LazyRow {
                        items(crew.size) { index ->
                            val oneCrew = crew[index]
                            ItemCrew(oneCrew) {
                                navController.navigate(TmdbScreen.PersonDetail.route + "/${oneCrew.id}")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun ItemCast(cast: TvCast, onClick: () -> Unit) {
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
                    modifier = Modifier
                        .width(120.dp)
                        .height(180.dp),
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
fun ItemCrew( crew: TvCrew, onClick: () -> Unit) {
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
                    modifier = Modifier.width(100.dp).height(100.dp),
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
