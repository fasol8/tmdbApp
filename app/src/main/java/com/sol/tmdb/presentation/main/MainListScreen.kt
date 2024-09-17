package com.sol.tmdb.presentation.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sol.tmdb.R
import com.sol.tmdb.domain.model.main.TrendingResult
import com.sol.tmdb.navigation.TmdbScreen

@Composable
fun MainListScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val trending by viewModel.trending.observeAsState(emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp, top = 88.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(8.dp))
//            TODO: SearchBar
            Text(text = "Main Screen")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Trending", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(2.dp))
            TrendingTabs(trending, viewModel, navController)
        }
    }
}

@Composable
fun TrendingTabs(
    trending: List<TrendingResult>,
    viewModel: MainViewModel,
    navController: NavController
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Today", "This Week")

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFE0E0E0))
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            indicator = { tabPositions ->
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(36.dp)
                        .width(tabPositions[selectedTabIndex].width * 0.9f)
                        .clip(RoundedCornerShape(18.dp))
                        .padding(horizontal = 4.dp)
                        .background(Color(0xFF102641))
                )
            },
            modifier = Modifier.fillMaxWidth(),
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        if (index == 0) {
                            viewModel.loadTrending("all", "day")
                        } else {
                            viewModel.loadTrending("all", "week")
                        }
                    },
                    modifier = Modifier
                        .height(36.dp)
                        .zIndex(1f)
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = if (selectedTabIndex == index) Color(0xFFA8E6CF) else Color(0xFF102641),
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.zIndex(2f) // El texto siempre está al frente, encima del box
                        )
                    }
                }
            }
        }
    }

    when (selectedTabIndex) {
        0 -> { TrendingTab(trending = trending, navController) }

        1 -> { TrendingTab(trending = trending, navController) }
    }
}

@Composable
fun TrendingTab(trending: List<TrendingResult>, navController: NavController) {
    LazyRow {
        items(trending.size) { index ->
            val trend = trending[index]
            ItemTrending(trend) {
                if (trend.mediaType == "movie") {
                    navController.navigate(TmdbScreen.MovieDetail.route + "/${trend.id}")
                } else {
                    navController.navigate(TmdbScreen.TvDetail.route + "/${trend.id}")
                }
            }
        }
    }
}

@Composable
fun ItemTrending(trend: TrendingResult, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .height(340.dp)
            .width(250.dp)
            .padding(4.dp),
        onClick = { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                val image = if (trend.posterPath.isNullOrEmpty()) {
                    R.drawable.no_image
                } else {
                    "https://image.tmdb.org/t/p/w500${trend.posterPath}"
                }
                AsyncImage(
                    model = image,
                    contentDescription = "poster TV",
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.no_image),
                    error = painterResource(id = R.drawable.no_image)
                )
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = (-4).dp, y = (-8).dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(color = Color(0xFFEEEEEE))

                    drawArc(
                        color = when {
                            ((trend.voteAverage * 10).toInt()) < 30 -> Color(0xFFEF5350)
                            ((trend.voteAverage * 10).toInt()) < 60 -> Color(0xFFFFCA28)
                            else -> Color(0xFF0F9D58)
                        },
                        startAngle = -90f,
                        sweepAngle = (trend.voteAverage * 36).toFloat(),
                        useCenter = false,
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Text(
                    text = "${(trend.voteAverage * 10).toInt()}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }
        }
    }
}