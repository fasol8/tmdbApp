@file:OptIn(ExperimentalMaterial3Api::class)

package com.sol.tmdb.presentation.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.sol.tmdb.R
import com.sol.tmdb.domain.model.main.SearchResult
import com.sol.tmdb.domain.model.main.TrendingResult
import com.sol.tmdb.navigation.TmdbScreen

@Composable
fun MainListScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val results by viewModel.searchResults.observeAsState(emptyList())
    val trending by viewModel.trending.observeAsState(emptyList())
    var query by remember { mutableStateOf("") }
    val imageBackground = if (trending.isNotEmpty()) {
        "https://image.tmdb.org/t/p/w500" + trending.random().backdropPath
    } else {
        null
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Blue.copy(alpha = .5f),
                        Color.Transparent
                    )
                )
            )
            .paint(
                painter = rememberAsyncImagePainter(model = imageBackground),
                contentScale = ContentScale.FillHeight,
                alpha = .3f
            )
    ) {
        Column(
            Modifier.padding(4.dp, top = 68.dp)
        ) {
            if (query.isEmpty())
                TextPlaceHolder()

            MultiSearchBar(
                query = query,
                onQueryChange = {
                    query = it
                },
                onSearch = { viewModel.searchMultiByQuery(query) }
            )
            LazyRow {
                items(results!!.size) { index: Int ->
                    val result = results!![index]
                    if (result.mediaType != "person") {
                        CardSearchMovieAndTvResult(result) {
                            if (result.mediaType != "tv") {
                                navController.navigate(TmdbScreen.MovieDetail.route + "/${result.id}")
                            } else {
                                navController.navigate(TmdbScreen.TvDetail.route + "/${result.id}")
                            }
                        }
                    } else {
                        CardSearchPeopleResult(result) {
                            navController.navigate(TmdbScreen.PersonDetail.route + "/${result.id}")
                        }
                    }

                    if (index == results!!.size - 1) {
                        LaunchedEffect(key1 = true) {
                            viewModel.loadNextPage(query)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
            ) {
                Text(
                    text = stringResource(R.string.trending),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 4.dp, start = 8.dp)
                )
                Spacer(modifier = Modifier.height(2.dp))
                TrendingTabs(trending, viewModel, navController)
            }
        }
    }
}

@Composable
fun TextPlaceHolder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.text_presentation),
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            lineHeight = 38.sp
        )
    }
}

@Composable
fun MultiSearchBar(query: String, onQueryChange: (String) -> Unit, onSearch: (String) -> Unit) {
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
        placeholder = { Text(text = stringResource(R.string.search_multi)) },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Game")
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
fun CardSearchMovieAndTvResult(
    result: SearchResult,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .height(250.dp)
            .width(150.dp)
            .padding(4.dp),
        onClick = { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                val image = if (result.posterPath.isNullOrEmpty()) {
                    R.drawable.no_image
                } else {
                    "https://image.tmdb.org/t/p/w500${result.posterPath}"
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
            if (result.voteAverage.toInt() != 0) {
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
                                ((result.voteAverage * 10).toInt()) < 30 -> Color(0xFFEF5350)
                                ((result.voteAverage * 10).toInt()) < 60 -> Color(0xFFFFCA28)
                                else -> Color(0xFF0F9D58)
                            },
                            startAngle = -90f,
                            sweepAngle = (result.voteAverage * 36).toFloat(),
                            useCenter = false,
                            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    Text(
                        text = "${(result.voteAverage * 10).toInt()}",
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
}

@Composable
fun CardSearchPeopleResult(
    result: SearchResult,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .height(250.dp)
            .width(150.dp)
            .padding(4.dp),
        onClick = { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                val image = if (result.profilePath.isNullOrEmpty()) {
                    R.drawable.profile_no_image
                } else {
                    "https://image.tmdb.org/t/p/w500${result.profilePath}"
                }
                AsyncImage(
                    model = image,
                    contentDescription = "poster TV",
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.profile_no_image),
                    error = painterResource(id = R.drawable.profile_no_image)
                )
            }
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
    val tabs = listOf(stringResource(R.string.today_tab), stringResource(R.string.this_week_tab))

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFE0E0E0))
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
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
                                color = if (selectedTabIndex == index) Color(0xFFA8E6CF) else Color(
                                    0xFF102641
                                ),
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.zIndex(2f)
                        )
                    }
                }
            }
        }
    }

    when (selectedTabIndex) {
        0 -> {
            TrendingTab(trending = trending, navController)
        }

        1 -> {
            TrendingTab(trending = trending, navController)
        }
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

            if (trend.voteAverage.toInt() != 0) {
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
}