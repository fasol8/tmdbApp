package com.sol.tmdb.presentation.tv

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.sol.tmdb.domain.model.tv.TvSeasonDetailResponse
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
        }
    }
}


