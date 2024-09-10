package com.sol.tmdb.presentation.tv

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.sol.tmdb.domain.model.movie.MovieGenre
import com.sol.tmdb.domain.model.tv.CountryResult
import com.sol.tmdb.domain.model.tv.CreatedBy
import com.sol.tmdb.domain.model.tv.CreditsResponse
import com.sol.tmdb.domain.model.tv.LastEpisodeToAir
import com.sol.tmdb.domain.model.tv.SimilarResult
import com.sol.tmdb.domain.model.tv.TvCast
import com.sol.tmdb.domain.model.tv.TvCertification
import com.sol.tmdb.domain.model.tv.TvCrew
import com.sol.tmdb.domain.model.tv.TvDetail
import com.sol.tmdb.domain.model.tv.TvRecommendationsResult
import com.sol.tmdb.navigation.TmdbScreen

@Composable
fun TvDetail(tvId: Int, navController: NavController, viewModel: TvViewModel = hiltViewModel()) {
    val tv by viewModel.tvById.observeAsState()
    val tvRatings by viewModel.tvRatings.observeAsState()
    val credits by viewModel.tvCredits.observeAsState()
    val tvProviders by viewModel.tvProviders.observeAsState()
    val tvSimilar by viewModel.tvSimilar.observeAsState(emptyList())
    val tvRecommendations by viewModel.tvRecommendations.observeAsState(emptyList())

    val errorMessage by viewModel.errorMessage.observeAsState()

    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(tvId) {
        viewModel.searchTvById(tvId)
        viewModel.searchTvRatings(tvId)
        viewModel.searchTvCredits(tvId)
        viewModel.searchTvSimilar(tvId)
        viewModel.searchTvRecommendation(tvId)
        viewModel.searchTvProvidersForMXAndUs(tvId)
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
                        TvCard(
                            tv!!,
                            tvRatings,
                            credits,
                            tvProviders,
                            tvSimilar,
                            tvRecommendations,
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
fun TvCard(
    tv: TvDetail,
    tvRatings: Map<String, TvCertification?>?,
    credits: CreditsResponse?,
    tvProviders: Map<String, CountryResult?>?,
    tvSimilar: List<SimilarResult?>,
    tvRecommendations: List<TvRecommendationsResult?>,
    navController: NavController
) {
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
                    Row {
                        tvRatings?.let { rati ->
                            rati.forEach { (country, rating) ->
                                if (country == "MX" && rating?.certification != null) {
                                    Text(
                                        text = rating.certification,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .border(2.dp, Color.Black)
                                            .padding(2.dp)
                                    )
                                }
                            }
                        }
                        val genreNames =
                            tv.genres.mapNotNull { MovieGenre.fromId(it.id)?.genreName }
                        Text(
                            text = genreNames.joinToString(", "),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier.padding(2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = tv.overview, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    if (tv.createdBy.isNotEmpty()) {
                        Text(
                            text = "Created by:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Row {
                            tv.createdBy.forEach { creator ->
                                Text(
                                    text = creator.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Blue,  // Puedes agregarle color para que parezca un enlace
                                    modifier = Modifier
                                        .clickable {
                                            navController.navigate(TmdbScreen.PersonDetail.route + "/${creator.id}")
                                        }
                                        .weight(1f)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Facts",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Row {
                        Text(
                            text = "Status \n" + tv.status,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .padding(2.dp)
                                .weight(.8f)
                        )
                        Column(Modifier.weight(1.2f)) {
                            Text(
                                text = "Total season: " + tv.numberOfSeasons,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .padding(2.dp)
                            )
                            Text(
                                text = "Total episodes: " + tv.numberOfEpisodes,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .padding(2.dp)
                            )
                            Text(
                                text = "Origin country: " + tv.originCountry,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .padding(2.dp)
                            )
                        }
                        Column(Modifier.weight(1f)) {
                            Text(text = "Networks", style = MaterialTheme.typography.bodySmall)
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier.height(60.dp)
                            ) {
                                items(tv.networks.size) { index ->
                                    val network = tv.networks[index]
                                    network.let {
                                        val image =
                                            ("https://image.tmdb.org/t/p/w500" + it.logoPath)
                                                ?: ""
                                        AsyncImage(
                                            model = image,
                                            contentDescription = "logo provider",
                                            modifier = Modifier
                                                .width(50.dp)
                                                .height(50.dp)
                                                .padding(4.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
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
                    Spacer(modifier = Modifier.height(4.dp))
                    when {
                        tvProviders != null -> {
                            val mxProviders = tvProviders["MX"]
                            val usProviders = tvProviders["US"]

                            Row {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                ) {
                                    if (mxProviders != null) {
                                        Text(text = "Providers in MX:")
                                        LazyVerticalGrid(
                                            columns = GridCells.Fixed(3),
                                            modifier = Modifier.height(90.dp)
                                        ) {
                                            items(mxProviders.flatrate.size) { index ->
                                                val provide = mxProviders.flatrate[index]
                                                provide.let {
                                                    val image =
                                                        ("https://image.tmdb.org/t/p/w500" + it.logoPath)
                                                            ?: ""
                                                    AsyncImage(
                                                        model = image,
                                                        contentDescription = "logo provider",
                                                        modifier = Modifier
                                                            .width(40.dp)
                                                            .height(40.dp)
                                                            .padding(4.dp),
                                                    )
                                                }
                                            }
                                        }
                                    } else Text(text = "No providers available for MX")
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    if (usProviders != null) {
                                        Text(text = "Providers in US:")
                                        LazyVerticalGrid(
                                            columns = GridCells.Fixed(3),
                                            modifier = Modifier.height(90.dp)
                                        ) {
                                            items(usProviders.flatrate.size) { index ->
                                                val provide = usProviders.flatrate.get(index)
                                                provide.let {
                                                    val image =
                                                        ("https://image.tmdb.org/t/p/w500" + it.logoPath)
                                                            ?: ""
                                                    AsyncImage(
                                                        model = image,
                                                        contentDescription = "logo provider",
                                                        modifier = Modifier
                                                            .width(38.dp)
                                                            .height(38.dp)
                                                            .padding(4.dp),
                                                    )
                                                }
                                            }
                                        }
                                    } else Text(text = "No providers available for US")
                                }
                            }
                        }

                        else -> {
                            CircularProgressIndicator()
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LastEpisodeToAir(tv.lastEpisodeToAir)
                    Text(text = "View All Seasons",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Blue,
                        modifier = Modifier
                            .clickable {
//                                navController.navigate(TmdbScreen.PersonDetail.route + "/${creator.id}")
                            })
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Similar:")
                    LazyRow {
                        items(tvSimilar.size) { index ->
                            val oneSimilar = tvSimilar[index]
                            ItemTvSimilar(oneSimilar) {
                                navController.navigate(TmdbScreen.TvDetail.route + "/${oneSimilar?.id}")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Recommendation:")
                    LazyRow {
                        items(tvRecommendations.size) { index ->
                            val tvRecommendation = tvRecommendations[index]
                            ItemTvRecommendation(tvRecommendation) {
                                navController.navigate(TmdbScreen.TvDetail.route + "/${tvRecommendation?.id}")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                }
            }
        }

        Box(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-28).dp, y = (370).dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(color = Color(0xFFEEEEEE))

                drawArc(
                    color = when {
                        ((tv.voteAverage * 10).toInt()) < 30 -> Color(0xFFEF5350)
                        ((tv.voteAverage * 10).toInt()) < 60 -> Color(0xFFFFCA28)
                        else -> Color(0xFF0F9D58)
                    },
                    startAngle = -90f,
                    sweepAngle = (tv.voteAverage * 36).toFloat(),
                    useCenter = false,
                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            Text(
                text = "${(tv.voteAverage * 10).toInt()}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(
                    Alignment.Center
                )
            )
        }
    }
}

@Composable
fun LastEpisodeToAir(lastEpisodeToAir: LastEpisodeToAir) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .height(180.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.align(Alignment.TopStart)) {
                val image =
                    lastEpisodeToAir.stillPath.let { "https://image.tmdb.org/t/p/w500$it" } ?: ""
                AsyncImage(
                    model = image,
                    contentDescription = "poster Episode",
                    modifier = Modifier.width(120.dp),
                    contentScale = ContentScale.Crop
                )
                Column {
                    Text(
                        text = "Season ${lastEpisodeToAir.seasonNumber}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row {
                        Text(
                            text = "${(lastEpisodeToAir.voteAverage * 10).toInt()}%",
                            modifier = Modifier
                                .padding(4.dp)
                                .border(2.dp, Color.Black)
                        )
                        Text(text = lastEpisodeToAir.airDate)
                    }
                    Text(
                        text = lastEpisodeToAir.overview,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Row {
                        Text(text = lastEpisodeToAir.name)
                        Text(text = "(${lastEpisodeToAir.seasonNumber}x${lastEpisodeToAir.episodeNumber})")
                        Text(
                            text = lastEpisodeToAir.episodeType,
                            modifier = Modifier
                                .padding(4.dp)
                                .border(2.dp, Color.Black)
                        )
                    }
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
fun ItemCrew(crew: TvCrew, onClick: () -> Unit) {
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
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp),
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
fun ItemTvSimilar(similar: SimilarResult?, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .width(120.dp),
        onClick = { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.align(Alignment.TopStart)) {
                val image = ("https://image.tmdb.org/t/p/w500" + similar?.posterPath) ?: ""
                AsyncImage(
                    model = image, contentDescription = "poster movie",
                    modifier = Modifier
                        .width(120.dp)
                        .height(170.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun ItemTvRecommendation(recommendation: TvRecommendationsResult?, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .width(120.dp),
        onClick = { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.align(Alignment.TopStart)) {
                val image = ("https://image.tmdb.org/t/p/w500" + recommendation?.posterPath) ?: ""
                AsyncImage(
                    model = image, contentDescription = "poster movie",
                    modifier = Modifier
                        .width(120.dp)
                        .height(170.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
