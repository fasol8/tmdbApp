@file:OptIn(ExperimentalPagerApi::class)

package com.sol.tmdb.presentation.tv

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.sol.tmdb.R
import com.sol.tmdb.domain.model.movie.MovieGenre
import com.sol.tmdb.domain.model.tv.CountryFlag
import com.sol.tmdb.domain.model.tv.CountryResult
import com.sol.tmdb.domain.model.tv.CreditsResponse
import com.sol.tmdb.domain.model.tv.LastEpisodeToAir
import com.sol.tmdb.domain.model.tv.Season
import com.sol.tmdb.domain.model.tv.SimilarResult
import com.sol.tmdb.domain.model.tv.TvCast
import com.sol.tmdb.domain.model.tv.TvCertification
import com.sol.tmdb.domain.model.tv.TvCrew
import com.sol.tmdb.domain.model.tv.TvDetail
import com.sol.tmdb.domain.model.tv.TvImagesBackdrop
import com.sol.tmdb.domain.model.tv.TvImagesPoster
import com.sol.tmdb.domain.model.tv.TvImagesResponse
import com.sol.tmdb.domain.model.tv.TvRecommendationsResult
import com.sol.tmdb.domain.model.tv.TvVideosResult
import com.sol.tmdb.navigation.TmdbScreen
import com.sol.tmdb.presentation.movie.openYoutubeVideo
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TvDetail(tvId: Int, navController: NavController, viewModel: TvViewModel = hiltViewModel()) {
    val tv by viewModel.tvById.observeAsState()
    val tvRatings by viewModel.tvRatings.observeAsState(emptyMap())
    val tvCredits by viewModel.tvCredits.observeAsState()
    val tvProviders by viewModel.tvProviders.observeAsState(emptyMap())
    val tvImages by viewModel.tvImages.observeAsState()
    val tvVideos by viewModel.tvVideos.observeAsState(emptyList())
    val tvSimilar by viewModel.tvSimilar.observeAsState(emptyList())
    val tvRecommendations by viewModel.tvRecommendations.observeAsState(emptyList())
    val errorMessage by viewModel.errorMessage.observeAsState()

    LaunchedEffect(tvId) { viewModel.searchAll(tvId) }

    when {
        tv != null -> {
            if (tvCredits != null && tvImages != null && tvRatings != null) {
                LazyColumn {
                    item {
                        TvCard(
                            tv,
                            tvRatings,
                            tvCredits,
                            tvProviders,
                            tvImages,
                            tvVideos,
                            tvSimilar,
                            tvRecommendations,
                            navController
                        )
                    }
                }
            } else {
                CircularProgressIndicator(modifier = Modifier.padding(164.dp))
            }
        }

        else -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(100.dp)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Error message: $errorMessage",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TvCard(
    tv: TvDetail?,
    tvRatings: Map<String, TvCertification?>?,
    credits: CreditsResponse?,
    tvProviders: Map<String, CountryResult?>?,
    tvImages: TvImagesResponse?,
    tvVideos: List<TvVideosResult>?,
    tvSimilar: List<SimilarResult?>,
    tvRecommendations: List<TvRecommendationsResult?>,
    navController: NavController
) {
    val imageBackground = ("https://image.tmdb.org/t/p/w500" + tv?.backdropPath)
        ?: painterResource(id = R.drawable.no_image)
    val imagePoster = ("https://image.tmdb.org/t/p/w500" + tv?.posterPath)
        ?: painterResource(id = R.drawable.no_image)
    val cast = credits?.cast ?: emptyList()
    val crew = credits?.crew ?: emptyList()

    if (tv != null) {
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
                modifier = Modifier.padding(horizontal = 8.dp)
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
                        .padding(start = 2.dp, end = 2.dp, bottom = 24.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 16.dp)
                ) {
                    Column(Modifier.padding(horizontal = 8.dp)) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = tv.name ?: "No title available",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 24.sp
                        )
                        TvRatingAndGenre(tv, tvRatings)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tv.overview ?: "No Overview Available",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        TvCreatedByPerson(tv, navController)
                        Spacer(modifier = Modifier.height(6.dp))
                        TvInfoTabs(tv, tvProviders)
                        Spacer(modifier = Modifier.height(4.dp))
                        TvCastAndCrewTabs(cast, crew, navController)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Last Season")
                        LastSeason(tv.seasons.last(), tv.lastEpisodeToAir)
                        Text(text = "View All Seasons",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Blue,
                            modifier = Modifier
                                .clickable {
                                    navController.navigate(TmdbScreen.TvSeason.route + "/${tv.id}/${tv.numberOfSeasons}")
                                })
                        Spacer(modifier = Modifier.height(4.dp))
                        TvMediaTabs(tvImages, tvVideos)
                        Spacer(modifier = Modifier.height(4.dp))
                        TvRecommendationAndSimilarTabs(tvRecommendations, tvSimilar, navController)
                    }
                }
            }

            if (tv.voteAverage.toInt() != 0) {
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
    }
}

@Composable
fun TvRatingAndGenre(tv: TvDetail, tvRatings: Map<String, TvCertification?>?) {
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
                ?: listOf("No Genres Available")
        Text(
            text = genreNames.joinToString(", "),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(2.dp)
        )
    }
}

@Composable
fun TvCreatedByPerson(tv: TvDetail, navController: NavController) {
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
                    text = creator.name ?: "Unknown",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Blue,
                    modifier = Modifier
                        .clickable {
                            navController.navigate(TmdbScreen.PersonDetail.route + "/${creator.id}")
                        }
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
fun TvInfoTabs(tv: TvDetail, tvProviders: Map<String, CountryResult?>?) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    val tabs = listOf("Facts", "Providers")

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> TvFactsTab(tv = tv)

            1 -> TvProviderTab(tvProviders = tvProviders ?: emptyMap())
        }
    }
}

@Composable
fun TvProviderTab(tvProviders: Map<String, CountryResult?>) {
    val mxProviders = tvProviders["MX"]
    val usProviders = tvProviders["US"]

    Column {
        if (mxProviders != null) {
            LazyRow {
                items(mxProviders.flatrate.size) { index ->
                    val provide = mxProviders.flatrate[index]
                    provide.let {
                        val image = if (it.logoPath.isNullOrEmpty()) {
                            R.drawable.no_image
                        } else {
                            "https://image.tmdb.org/t/p/w500${it.logoPath}"
                        }
                        AsyncImage(
                            model = image,
                            contentDescription = "logo provider",
                            modifier = Modifier
                                .width(60.dp)
                                .height(60.dp)
                                .padding(4.dp),
                            placeholder = painterResource(id = R.drawable.no_image),
                            error = painterResource(id = R.drawable.no_image)
                        )
                    }
                }
            }
        } else Text(text = "No providers available for MX")
    }
}

@Composable
fun TvFactsTab(tv: TvDetail) {
    Row {
        Text(
            text = "Status \n" + (tv.status ?: "Unknown"),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(2.dp)
                .weight(.8f)
        )
        Column(Modifier.weight(1.2f)) {
            Text(
                text = "Total season: " + (tv.numberOfSeasons ?: 0),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(2.dp)
            )
            Text(
                text = "Total episodes: " + (tv.numberOfEpisodes ?: 0),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(2.dp)
            )
            Text(
                text = "Origin country: " + (tv.originCountry.joinToString {
                    CountryFlag.getFlagByCode(it)
                } ?: "Unknown"),
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
                        val image = if (it.logoPath.isNullOrEmpty()) {
                            R.drawable.no_image
                        } else {
                            "https://image.tmdb.org/t/p/w500${it.logoPath}"
                        }
                        AsyncImage(
                            model = image,
                            contentDescription = "logo provider",
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp)
                                .padding(4.dp),
//                            TODO
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TvCastAndCrewTabs(
    tvCast: List<TvCast>,
    tvCrew: List<TvCrew>,
    navController: NavController
) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    val tabs = listOf("Cast", "Crew")

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> if (tvCast.isNullOrEmpty()) {
                Text(text = "No cast available")
            } else {
                CastTvTab(cast = tvCast, navController = navController)
            }

            1 -> if (tvCrew.isNullOrEmpty()) {
                Text(text = "No crew available")
            } else {
                CrewTvTab(crew = tvCrew, navController = navController)
            }
        }
    }
}

@Composable
fun CrewTvTab(crew: List<TvCrew>, navController: NavController) {
    LazyRow {
        items(crew.size) { index ->
            val oneCrew = crew[index]
            ItemTvCrew(oneCrew) {
                navController.navigate(TmdbScreen.PersonDetail.route + "/${oneCrew.id}")
            }
        }
    }
}

@Composable
fun CastTvTab(cast: List<TvCast>, navController: NavController) {
    LazyRow {
        items(cast.size) { index ->
            val oneCast = cast[index]
            ItemTvCast(oneCast) {
                navController.navigate(TmdbScreen.PersonDetail.route + "/${oneCast.id}")
            }
        }
    }
}

@Composable
fun ItemTvCast(cast: TvCast, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .width(120.dp),
        onClick = { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.align(Alignment.TopStart)) {
                val image = if (cast.profilePath.isNullOrEmpty()) {
                    R.drawable.profile_no_image
                } else {
                    "https://image.tmdb.org/t/p/w500${cast.profilePath}"
                }
                AsyncImage(
                    model = image, contentDescription = "poster movie",
                    modifier = Modifier
                        .width(120.dp)
                        .height(180.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.profile_no_image),
                    error = painterResource(id = R.drawable.profile_no_image)
                )
                Text(
                    text = cast.name ?: "Unknown",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 8.dp, start = 2.dp)
                )
                Text(
                    text = cast.character ?: "Unknown",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 2.dp, start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ItemTvCrew(crew: TvCrew, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .width(100.dp),
        onClick = { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.align(Alignment.TopStart)) {
                val image = if (crew.profilePath.isNullOrEmpty()) {
                    R.drawable.profile_no_image
                } else {
                    "https://image.tmdb.org/t/p/w500${crew.profilePath}"
                }
                AsyncImage(
                    model = image, contentDescription = "poster movie",
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.profile_no_image),
                    error = painterResource(id = R.drawable.profile_no_image)
                )
                Text(
                    text = crew.name ?: "Unknown",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 8.dp, start = 2.dp)
                )
                Text(
                    text = crew.department ?: "Unknown",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 2.dp, start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun LastSeason(last: Season?, lastEpisodeToAir: LastEpisodeToAir?) {
    if (last != null && lastEpisodeToAir != null) {
        Card(
            modifier = Modifier
                .padding(4.dp)
                .height(150.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(Modifier.align(Alignment.TopStart)) {
                    val image = if (last.posterPath.isNullOrEmpty()) {
                        R.drawable.no_image
                    } else {
                        "https://image.tmdb.org/t/p/w500${last.posterPath ?: ""}"
                    }
                    AsyncImage(
                        model = image,
                        contentDescription = "poster Episode",
                        modifier = Modifier
                            .width(100.dp)
                            .fillMaxHeight(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.no_image),
                        error = painterResource(id = R.drawable.no_image)
                    )
                    Column(Modifier.padding(start = 16.dp, top = 16.dp)) {
                        Text(
                            text = last.name ?: "Unknown",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (last.voteAverage.toInt() != 0) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_star),
                                    contentDescription = "Star Icon",
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFFFFD700) // Color dorado
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${(last.voteAverage * 10).toInt()}%",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            val year = last.airDate?.let {
                                try {
                                    LocalDate.parse(it, DateTimeFormatter.ISO_DATE).year.toString()
                                } catch (e: Exception) {
                                    "N/A"
                                }
                            } ?: "N/A"
                            Text(
                                text = year,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Text(
                                text = " • ${last.episodeCount ?: "0"} Episodes",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = last.overview ?: "No overview available",
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = lastEpisodeToAir.name ?: "No episode name available",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (lastEpisodeToAir.seasonNumber != null && lastEpisodeToAir.episodeNumber != null) {
                                Text(
                                    text = "(${lastEpisodeToAir.seasonNumber}x${lastEpisodeToAir.episodeNumber})",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            if (lastEpisodeToAir.episodeType == "finale") {
                                Text(
                                    text = "Season Finale",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .border(1.dp, MaterialTheme.colorScheme.primary)
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TvMediaTabs(tvImages: TvImagesResponse?, tvVideos: List<TvVideosResult>?) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    val tabs = listOf("Poster", "Back Drop", "Videos")

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> TvPostersTab(posters = tvImages?.posters ?: emptyList())
            1 -> TvBackdropTab(backdrops = tvImages?.backdrops ?: emptyList())
            2 -> tvVideos?.let { TvVideosTab(videos = it) }
        }
    }
}

@Composable
fun TvPostersTab(posters: List<TvImagesPoster>?) {
    if (posters != null) {
        val pagerState = rememberPagerState(initialPage = 0)

        if (posters.isEmpty()) {
            Text(text = "No posters available")
            return
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(50.dp),
                count = posters.size,
                modifier = Modifier
                    .fillMaxSize()
            ) { page ->
                val poster = posters[page]
                TvPosterItem(poster = poster, isHero = page == pagerState.currentPage)
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                activeColor = Color.Black,
                inactiveColor = Color.Gray
            )
        }
    }
}

@Composable
fun TvPosterItem(poster: TvImagesPoster, isHero: Boolean) {
    val scale = animateFloatAsState(if (isHero) 1.2f else 0.85f).value

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(8.dp)
            .scale(scale),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        val image = poster.filePath.let { "https://image.tmdb.org/t/p/w500$it" } ?: ""
        AsyncImage(
            model = image,
            contentDescription = "profile image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun TvBackdropTab(backdrops: List<TvImagesBackdrop>?) {
    if (backdrops != null) {
        val pagerState = rememberPagerState(initialPage = 0)
        if (backdrops.isEmpty()) {
            Text(text = "No backdrops available")
            return
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(50.dp),
                count = backdrops!!.size,
                modifier = Modifier
                    .fillMaxSize()
            ) { page ->
                val backdrop = backdrops[page]
                TvBackDropItem(backdrop = backdrop, isHero = page == pagerState.currentPage)
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                activeColor = Color.Black,
                inactiveColor = Color.Gray
            )
        }
    }
}

@Composable
fun TvBackDropItem(backdrop: TvImagesBackdrop, isHero: Boolean) {
    val scale = animateFloatAsState(if (isHero) 1.2f else 0.85f).value

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(8.dp)
            .scale(scale),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        val image = backdrop.filePath.let { "https://image.tmdb.org/t/p/w500$it" } ?: ""
        AsyncImage(
            model = image,
            contentDescription = "profile image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun TvVideosTab(videos: List<TvVideosResult?>) {
    val context = LocalContext.current
    val videosYoutube = getYoutubeVideosTv(videos)

    if (videosYoutube.isEmpty()) {
        Text(text = "No YouTube videos available")
    } else {
        LazyRow {
            items(videosYoutube.size) { index ->
                val video = videosYoutube[index]
                if (video != null) {
                    CardTvVideosYoutube(video) { openYoutubeVideo(context, video.key) }
                }
            }
        }
    }
}

@Composable
fun CardTvVideosYoutube(video: TvVideosResult, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(120.dp)
            .padding(4.dp),
        onClick = { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Text(
                text = video.name ?: "Unknown",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Blue
            )
        }
    }
}

fun getYoutubeVideosTv(tvVideos: List<TvVideosResult?>): List<TvVideosResult?> {
    return tvVideos.filter { it?.site == "YouTube" }
}

@Composable
fun TvRecommendationAndSimilarTabs(
    tvRecommendations: List<TvRecommendationsResult?>,
    tvSimilar: List<SimilarResult?>,
    navController: NavController
) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    val tabs = listOf("Recommendation", "Similar")

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            maxLines = 1,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> if (tvRecommendations.isNullOrEmpty()) {
                Text(text = "")
            } else {
                TvRecommendationTab(
                    tvRecommendation = tvRecommendations,
                    navController = navController
                )
            }

            1 -> if (tvSimilar.isNullOrEmpty()) {
                Text(text = "")
            } else {
                TvSimilarTab(tvSimilar = tvSimilar, navController = navController)
            }
        }
    }
}

@Composable
fun TvSimilarTab(tvSimilar: List<SimilarResult?>, navController: NavController) {
    LazyRow {
        items(tvSimilar.size) { index ->
            val tvSim = tvSimilar[index]
            if (tvSim != null) {
                ItemTvSimilar(tvSim) {
                    navController.navigate(TmdbScreen.TvDetail.route + "/${tvSim.id}")
                }
            } else {
                Text(text = "Invalid movie data")
            }
        }
    }
}

@Composable
fun TvRecommendationTab(
    tvRecommendation: List<TvRecommendationsResult?>,
    navController: NavController
) {
    LazyRow {
        items(tvRecommendation.size) { index ->
            val tvRecom = tvRecommendation[index]
            if (tvRecom != null) {
                ItemTvRecommendation(tvRecom) {
                    navController.navigate(TmdbScreen.TvDetail.route + "/${tvRecom.id}")
                }
            } else {
                Text(text = "Invalid movie data")
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
                val image = if (similar?.posterPath.isNullOrEmpty()) {
                    R.drawable.profile_no_image
                } else {
                    "https://image.tmdb.org/t/p/w500${similar?.posterPath}"
                }
                AsyncImage(
                    model = image, contentDescription = "poster movie",
                    modifier = Modifier
                        .width(120.dp)
                        .height(170.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.no_image),
                    error = painterResource(id = R.drawable.no_image)
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
                val image = if (recommendation?.posterPath.isNullOrEmpty()) {
                    R.drawable.profile_no_image
                } else {
                    "https://image.tmdb.org/t/p/w500${recommendation?.posterPath}"
                }
                AsyncImage(
                    model = image, contentDescription = "poster movie",
                    modifier = Modifier
                        .width(120.dp)
                        .height(170.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.no_image),
                    error = painterResource(id = R.drawable.no_image)
                )
            }
        }
    }
}
