package com.sol.tmdb.presentation.mySpace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sol.tmdb.R
import com.sol.tmdb.data.repository.db.movie.MovieEntity
import com.sol.tmdb.data.repository.db.tv.TvEntity
import com.sol.tmdb.navigation.TmdbScreen

@Composable
fun MySpaceScreen(navController: NavController, viewModel: MySpaceViewModel = hiltViewModel()) {
    val moviesFav by viewModel.moviesFav.observeAsState(emptyList())
    val moviesWatch by viewModel.moviesWatch.observeAsState(emptyList())
    val tvsFav by viewModel.tvsFav.observeAsState(emptyList())
    val tvsWatch by viewModel.tvsWatch.observeAsState(emptyList())

    LaunchedEffect(true) { viewModel.loadAll() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp, start = 4.dp, end = 4.dp, bottom = 28.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 16.dp)
    ) {
        MovieAndTvListTab(moviesWatch, moviesFav, tvsFav, tvsWatch, navController)
    }
}

@Composable
fun MovieAndTvListTab(
    moviesWatch: List<MovieEntity>,
    moviesFav: List<MovieEntity>,
    tvsFav: List<TvEntity>,
    tvsWatch: List<TvEntity>,
    navController: NavController
) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    val tabs = listOf(
        stringResource(R.string.movies_title),
        stringResource(R.string.tvs_title)
    )

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
//                        if (index == 0) {
//                            viewModel.loadTrending("all", "day")
//                        } else {
//                            viewModel.loadTrending("all", "week")
//                        }
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
                        androidx.compose.material3.Text(
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
        0 -> MovieListTabs(moviesFav, moviesWatch, navController)
        1 -> TvListTabs(tvsFav, tvsWatch, navController)
    }

}

@Composable
fun MovieListTabs(
    moviesFav: List<MovieEntity>,
    moviesWatch: List<MovieEntity>,
    navController: NavController
) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.movie_favorites_title),
        stringResource(R.string.movie_watch_list_title)
    )

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
                        androidx.compose.material3.Text(
                            text = title,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> if (moviesFav.isEmpty()) {
                Text(text = "No favorites yet.")
            } else {
                MovieFavTab(movies = moviesFav, navController)
            }

            1 -> if (moviesWatch.isEmpty()) {
                Text(text = "No Watch List yet.")
            } else {
                MovieFavTab(movies = moviesWatch, navController)
            }
        }
    }
}

@Composable
fun MovieFavTab(movies: List<MovieEntity>, navController: NavController) {
    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(movies.size) { index ->
            val movie = movies[index]
            if (movie != null) {
                MovieListItem(movie) {
                    navController.navigate(TmdbScreen.MovieDetail.route + "/${movie.id}")
                }
            }
        }
    }
}

@Composable
fun MovieListItem(movie: MovieEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .padding(4.dp),
        onClick = { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.align(Alignment.TopStart)) {
                val image = if (movie.posterPath.isNullOrEmpty()) {
                    R.drawable.no_image
                } else {
                    "https://image.tmdb.org/t/p/w500${movie.posterPath}"
                }
                AsyncImage(
                    model = image,
                    contentDescription = stringResource(id = R.string.poster_movie_description),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.no_image),
                    error = painterResource(id = R.drawable.no_image)
                )
            }
        }
    }
}

@Composable
fun TvListTabs(tvsFav: List<TvEntity>, tvsWatch: List<TvEntity>, navController: NavController) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    val tabs = listOf(
        stringResource(R.string.movie_favorites_title),
        stringResource(R.string.movie_watch_list_title)
    )

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
                        androidx.compose.material3.Text(
                            text = title,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> if (tvsFav.isEmpty()) {
                Text(text = "No favorites yet.")
            } else {
                TvFavTab(tvs = tvsFav, navController)
            }

            1 -> if (tvsWatch.isEmpty()) {
                Text(text = "No Watch List yet.")
            } else {
                TvFavTab(tvs = tvsWatch, navController)
            }
        }
    }
}

@Composable
fun TvFavTab(tvs: List<TvEntity>, navController: NavController) {
    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(tvs.size) { index ->
            val tv = tvs[index]
            if (tv != null) {
                TvListItem(tv) {
                    navController.navigate(TmdbScreen.TvDetail.route + "/${tv.id}")
                }
            }
        }
    }
}

@Composable
fun TvListItem(tv: TvEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .padding(4.dp),
        onClick = { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.align(Alignment.TopStart)) {
                val image = if (tv.posterPath.isNullOrEmpty()) {
                    R.drawable.no_image
                } else {
                    "https://image.tmdb.org/t/p/w500${tv.posterPath}"
                }
                AsyncImage(
                    model = image,
                    contentDescription = stringResource(id = R.string.poster_movie_description),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.no_image),
                    error = painterResource(id = R.drawable.no_image)
                )
            }
        }
    }
}

