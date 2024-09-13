package com.sol.tmdb.presentation.tv

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sol.tmdb.R
import com.sol.tmdb.domain.model.tv.TvCast
import com.sol.tmdb.domain.model.tv.TvCrew
import com.sol.tmdb.domain.model.tv.TvSeasonDetailEpisode
import com.sol.tmdb.domain.model.tv.TvSeasonDetailResponse
import com.sol.tmdb.navigation.TmdbScreen
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TvSeason(
    tvId: Int,
    numberOfSeasons: Int,
    navController: NavController,
    viewModel: TvViewModel = hiltViewModel()
) {

    LaunchedEffect(tvId, numberOfSeasons) {
        viewModel.loadAllSeasons(tvId, numberOfSeasons)
    }

    val seasonDetails by viewModel.seasonDetails.observeAsState(listOf())
    val errorMessage by viewModel.errorMessage.observeAsState("") //TODO: Toast.short

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 96.dp)
    ) {
        Column {
            LazyColumn {
                items(seasonDetails.size) { index ->
                    val season = seasonDetails[index]
                    SeasonCard(season = season, navController)
                }
            }
        }
    }
}

@Composable
fun SeasonCard(season: TvSeasonDetailResponse, navController: NavController) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { isExpanded = !isExpanded }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(Modifier.align(Alignment.TopStart)) {
                    val image = if (season.posterPath.isNullOrEmpty()) {
                        R.drawable.no_image
                    } else {
                        "https://image.tmdb.org/t/p/w500${season.posterPath}"
                    }
                    AsyncImage(
                        model = image,
                        contentDescription = "Season poster",
                        modifier = Modifier
                            .width(100.dp)
                            .fillMaxHeight(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.no_image),
                        error = painterResource(id = R.drawable.no_image)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(
                            text = season.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (season.voteAverage.toInt() != 0) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_star),
                                    contentDescription = "Star Icon",
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFFFFD700) // Color dorado
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${(season.voteAverage * 10).toInt()}%",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            val year = season.airDate.let {
                                LocalDate.parse(it, DateTimeFormatter.ISO_DATE).year.toString()
                            } ?: "N/A"
                            Text(
                                text = year,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Text(
                                text = " • ${season.episodes.size} Episodes",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = season.overview,
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            if (isExpanded) {
                val heightPerEpisode = 100.dp
                val totalHeight = (season.episodes.size * heightPerEpisode.value).dp
                val maxAllowedHeight = 800.dp
                Column(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp)
                        .heightIn(min = 100.dp, max = totalHeight.coerceAtMost(maxAllowedHeight))
                        .verticalScroll(rememberScrollState())
                ) {
                    season.episodes.forEach { episode ->
                        ItemEpisodes(episode, navController)
                    }
                }
            }
        }

    }
}

@Composable
fun ItemEpisodes(episode: TvSeasonDetailEpisode, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(Modifier.align(Alignment.TopStart)) {
                Row {
                    val image = if (episode.stillPath.isNullOrEmpty()) {
                        R.drawable.no_image
                    } else {
                        "https://image.tmdb.org/t/p/w500${episode.stillPath}"
                    }
                    AsyncImage(
                        model = image,
                        contentDescription = "logo provider",
                        modifier = Modifier
                            .width(100.dp)
                            .fillMaxHeight(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.no_image),
                        error = painterResource(id = R.drawable.no_image)
                    )
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "Episode ${episode.episodeNumber}: ${episode.name}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Air Date: ${episode.airDate}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = episode.overview,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                InfoGalleryAndGuestStarsTabs(episode, navController)
                HorizontalDivider(thickness = 2.dp, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun InfoGalleryAndGuestStarsTabs(episode: TvSeasonDetailEpisode, navController: NavController) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Crew", "Guest Stars", "Images")

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = selectedTabIndex == index, onClick = { selectedTabIndex = index }) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        when (selectedTabIndex) {
            0 -> EpisodeCrewTab(crew = episode.crew, navController = navController)
            1 -> EpisodeGuestStarsTab(stars = episode.guestStars, navController)
            2 -> EpisodeImages(images = episode.stillPath)
        }
    }
}

@Composable
fun EpisodeImages(images: String) {
//    TODO: change a list to make a carousel
}

@Composable
fun EpisodeGuestStarsTab(stars: List<TvCast>, navController: NavController) {
    LazyRow {
        items(stars.size) { index ->
            val oneCast = stars[index]
            EpisodeGuestStar(oneCast) {
                navController.navigate(TmdbScreen.PersonDetail.route + "/${oneCast.id}")
            }
        }
    }
}

@Composable
fun EpisodeGuestStar(cast: TvCast, onClick: () -> Unit) {
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
                        .width(100.dp)
                        .height(100.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.profile_no_image),
                    error = painterResource(id = R.drawable.profile_no_image)
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
fun EpisodeCrewTab(crew: List<TvCrew>, navController: NavController) {
    LazyRow {
        items(crew.size) { index ->
            val oneCrew = crew[index]
            EpisodeItemCrew(oneCrew) {
                navController.navigate(TmdbScreen.PersonDetail.route + "/${oneCrew.id}")
            }
        }
    }
}

@Composable
fun EpisodeItemCrew(crew: TvCrew, onClick: () -> Unit) {
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