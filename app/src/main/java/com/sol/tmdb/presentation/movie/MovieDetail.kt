@file:OptIn(ExperimentalPagerApi::class)

package com.sol.tmdb.presentation.movie

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.Button
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
import com.sol.tmdb.domain.model.movie.Cast
import com.sol.tmdb.domain.model.movie.Certification
import com.sol.tmdb.domain.model.movie.CountryResult
import com.sol.tmdb.domain.model.movie.Crew
import com.sol.tmdb.domain.model.movie.MovieCredits
import com.sol.tmdb.domain.model.movie.MovieDetail
import com.sol.tmdb.domain.model.movie.MovieGenre
import com.sol.tmdb.domain.model.movie.MovieImagesBackdrop
import com.sol.tmdb.domain.model.movie.MovieImagesPoster
import com.sol.tmdb.domain.model.movie.MovieImagesResponse
import com.sol.tmdb.domain.model.movie.MovieRecommendationResult
import com.sol.tmdb.domain.model.movie.MovieSimilarResult
import com.sol.tmdb.domain.model.movie.MovieVideosResult
import com.sol.tmdb.domain.model.tv.CountryFlag
import com.sol.tmdb.navigation.TmdbScreen

@Composable
fun MovieDetail(
    movieId: Int,
    navController: NavController,
    viewModel: MovieViewModel = hiltViewModel()
) {
    val movie by viewModel.movieById.observeAsState()
    val movieCertification by viewModel.movieCertifications.observeAsState()
    val movieVideos by viewModel.movieVideos.observeAsState(emptyList())
    val movieImages by viewModel.movieImages.observeAsState()
    val movieCredits by viewModel.movieCredits.observeAsState()
    val movieProvider by viewModel.movieProviders.observeAsState()
    val movieSimilar by viewModel.movieSimilar.observeAsState(emptyList())
    val movieRecommendation by viewModel.movieRecommendation.observeAsState(emptyList())

    val errorMessage by viewModel.errorMessage.observeAsState()

    LaunchedEffect(movieId) { viewModel.searchAllMovie(movieId) }

    when {
        movie != null -> {
            if (movieCredits != null && movieProvider != null) {
                LazyColumn {
                    item {
                        MovieCard(
                            movie,
                            movieCertification,
                            movieVideos,
                            movieImages,
                            movieCredits,
                            movieProvider,
                            movieSimilar,
                            movieRecommendation,
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
                        text = "Error message: ${errorMessage}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun MovieCard(
    movie: MovieDetail?,
    movieCertification: Map<String?, Certification?>?,
    movieVideos: List<MovieVideosResult>,
    movieImages: MovieImagesResponse?,
    movieCredits: MovieCredits?,
    movieProvider: Map<String, CountryResult?>?,
    movieSimilar: List<MovieSimilarResult?>,
    movieRecommendation: List<MovieRecommendationResult?>,
    navController: NavController
) {
    val imageBackground = ("https://image.tmdb.org/t/p/w500" + movie?.backdropPath)
        ?: painterResource(id = R.drawable.no_image)
    val imagePoster = ("https://image.tmdb.org/t/p/w500" + movie?.posterPath)
        ?: painterResource(id = R.drawable.no_image)
    val movieCast = movieCredits?.cast ?: emptyList()
    val movieCrew = movieCredits?.crew ?: emptyList()

    if (movie != null) {
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
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 96.dp, bottom = 16.dp)
                        .offset(y = 30.dp) // Desplaza la imagen hacia abajo, superponiéndola sobre la tarjeta
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
                        .padding(start = 2.dp, end = 2.dp, bottom = 24.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 16.dp)
                ) {
                    Column(Modifier.padding(horizontal = 8.dp)) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = movie.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 24.sp
                        )
                        CertificationAndGenresMovie(movie, movieCertification)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row {
//                        TODO: Favorite - List - Watchlist
                            TrailerButton(movieVideos)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = movie.overview, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        InfoAndProvidersMovieTabs(movie, movieProvider)
                        Spacer(modifier = Modifier.height(4.dp))
                        CastAndCrewMovieTabs(movieCast, movieCrew, navController)
                        Spacer(modifier = Modifier.height(4.dp))
                        MediaMovieTabs(movieImages, movieVideos)
                        Spacer(modifier = Modifier.height(4.dp))
                        RecommendationAndSimilarMovieTabs(
                            tvRecommendations = movieRecommendation,
                            tvSimilar = movieSimilar,
                            navController = navController
                        )
                    }
                }
            }

            if (movie.voteAverage.toInt() != 0) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-28).dp, y = (370).dp),
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(color = Color(0xFFEEEEEE))

                        drawArc(
                            color = when {
                                ((movie.voteAverage * 10).toInt()) < 30 -> Color(0xFFEF5350)
                                ((movie.voteAverage * 10).toInt()) < 60 -> Color(0xFFFFCA28)
                                else -> Color(0xFF0F9D58)
                            },
                            startAngle = -90f,
                            sweepAngle = (movie.voteAverage * 36).toFloat(), // 10 * 36 = 360 grados (cierre del círculo)
                            useCenter = false,
                            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    Text(
                        text = "${(movie.voteAverage * 10).toInt()}%",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun CertificationAndGenresMovie(
    movie: MovieDetail,
    movieCertification: Map<String?, Certification?>?
) {
    Row {
        movieCertification?.let { certs ->
            certs.forEach { (country, certification) ->
                if (country == "MX" && certification?.certification != null) {
                    Text(
                        text = certification.certification ?: "",
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
            movie.genres.mapNotNull { MovieGenre.fromId(it.id)?.genreName }
        Text(
            text = genreNames.joinToString(", "),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(2.dp)
        )
    }
}

@Composable
fun TrailerButton(movieVideos: List<MovieVideosResult>) {
    val context = LocalContext.current
    val trailer = getTrailerVideo(movieVideos)

    if (trailer != null) {
        Button(onClick = { openYoutubeVideo(context, trailer.key) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_play_arrow),
                contentDescription = "Play trailer",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = "Play Trailer")
        }
    } else {
        Text(text = "Trailer not available")
//        Toast.makeText(context, "Trailer not available", Toast.LENGTH_SHORT).show()
    }
}

fun openYoutubeVideo(context: Context, videoKey: String) {
    val videoUrl = "https://www.youtube.com/watch?v=$videoKey"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
    intent.setPackage("com.google.android.youtube") // Intenta abrir la app de YouTube

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        // Si no puede abrir la app de YouTube, abre el navegador
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
        context.startActivity(webIntent)
    }
}

fun getTrailerVideo(movieVideos: List<MovieVideosResult>): MovieVideosResult? {
    return movieVideos.firstOrNull { it.type == "Trailer" && it.site == "YouTube" }
}

@Composable
fun InfoAndProvidersMovieTabs(movie: MovieDetail, movieProvider: Map<String, CountryResult?>?) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    val tabs = listOf("Facts", "Providers")

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
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
            0 -> InfoMovieTab(movie = movie)

            1 -> ProvidersMovieTab(movieProvider = movieProvider)
        }
    }
}

@Composable
fun InfoMovieTab(movie: MovieDetail) {
    Row {
        Text(
            text = "Status \n" + movie.status,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(2.dp)
                .weight(.8f)
        )
        Column(Modifier.weight(1.2f)) {
            Text(
                text = "Budget: $" + movie.budget,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(2.dp)
            )
            Text(
                text = "Revenue: $" + movie.revenue,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(2.dp)
            )
            Text(
                text = "Origin country: " + movie.originCountry.joinToString {
                    CountryFlag.getFlagByCode(it)
                },
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(2.dp)
            )
        }
        Column(Modifier.weight(1f)) {
            Text(text = "Companies", style = MaterialTheme.typography.bodySmall)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.height(60.dp)
            ) {
                items(movie.productionCompanies.size) { index ->
                    val network = movie.productionCompanies[index]
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
                            placeholder = painterResource(id = R.drawable.no_image),
                            error = painterResource(id = R.drawable.no_image)
//                            TODO
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProvidersMovieTab(movieProvider: Map<String, CountryResult?>?) {
    if (movieProvider != null) {
        val mxProviders = movieProvider["MX"]
        val usProviders = movieProvider["US"]

        Column {
            if (mxProviders?.flatrate?.isNotEmpty() == true) {
                LazyRow {
                    items(mxProviders.flatrate.size) { index ->
                        val provide = mxProviders.flatrate[index]
                        val image = if (provide.logoPath.isNullOrEmpty()) {
                            R.drawable.no_image
                        } else {
                            "https://image.tmdb.org/t/p/w500${provide.logoPath}"
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
            } else {
                Text(text = "No providers available for MX")
            }
        }
    } else {
        CircularProgressIndicator()
    }
}

@Composable
fun CastAndCrewMovieTabs(
    movieCast: List<Cast>,
    movieCrew: List<Crew>,
    navController: NavController
) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    val tabs = listOf("Cast", "Crew")

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
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
            0 -> if (movieCast.isNullOrEmpty()) {
                Text(text = "No cast available")
            } else {
                CastMovieTab(movieCast = movieCast, navController = navController)
            }

            1 -> if (movieCrew.isNullOrEmpty()) {
                Text(text = "No crew available")
            } else {
                CrewMovieTab(movieCrew = movieCrew, navController = navController)
            }
        }
    }
}

@Composable
fun CrewMovieTab(movieCrew: List<Crew>, navController: NavController) {
    LazyRow {
        items(movieCrew.size) { index ->
            val crew = movieCrew[index]
            CrewMovieItem(crew) {
                navController.navigate(TmdbScreen.PersonDetail.route + "/${crew.id}")
            }
        }
    }
}

@Composable
fun CastMovieTab(movieCast: List<Cast>, navController: NavController) {
    LazyRow {
        items(movieCast.size) { index ->
            val cast = movieCast[index]
            CastMovieItem(cast) {
                navController.navigate(TmdbScreen.PersonDetail.route + "/${cast.id}")
            }
        }
    }
}

@Composable
fun CastMovieItem(cast: Cast, onClick: () -> Unit) {
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
fun CrewMovieItem(crew: Crew, onClick: () -> Unit) {
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

@Composable
fun MediaMovieTabs(movieImages: MovieImagesResponse?, movieVideos: List<MovieVideosResult>) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    val tabs = listOf("Poster", "Back Drop", "Video")

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
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
            0 -> PostersMovieTab(posters = movieImages?.posters ?: emptyList())
            1 -> BackdropMovieTab(backdrops = movieImages?.backdrops ?: emptyList())
            2 -> VideosMovieTab(videos = movieVideos ?: emptyList())
        }
    }
}

@Composable
fun PostersMovieTab(posters: List<MovieImagesPoster>?) {
    val pagerState = rememberPagerState(initialPage = 0)

    if (posters!!.isEmpty()) {
        Text(text = "No posters available")
        return
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(50.dp),
            count = posters!!.size,
            modifier = Modifier
                .fillMaxSize()
        ) { page ->
            val poster = posters[page]
            PosterMovieItem(poster = poster, isHero = page == pagerState.currentPage)
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

@Composable
fun PosterMovieItem(poster: MovieImagesPoster, isHero: Boolean) {
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
fun BackdropMovieTab(backdrops: List<MovieImagesBackdrop>?) {
    val pagerState = rememberPagerState(initialPage = 0)

    if (backdrops!!.isEmpty()) {
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
            BackDropMovieItem(backdrop = backdrop, isHero = page == pagerState.currentPage)
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

@Composable
fun BackDropMovieItem(backdrop: MovieImagesBackdrop, isHero: Boolean) {
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
fun VideosMovieTab(videos: List<MovieVideosResult>) {
    val context = LocalContext.current
    val videosYoutube = getYoutubeVideos(videos)

    if (videosYoutube.isEmpty()) {
        Text(text = "No YouTube videos available")
    } else {
        LazyRow {
            items(videosYoutube.size) { index ->
                val video = videosYoutube[index]
                VideosYoutubeMovieCard(video) { openYoutubeVideo(context, video.key) }
            }
        }
    }
}

@Composable
fun VideosYoutubeMovieCard(videosYoutube: MovieVideosResult, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(120.dp)
            .padding(4.dp),
        onClick = { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Text(
                text = videosYoutube.name,
                style = MaterialTheme.typography.titleSmall,
                color = Color.Blue
            )
        }
    }
}

fun getYoutubeVideos(movieVideos: List<MovieVideosResult>): List<MovieVideosResult> {
    return movieVideos.filter { it.site == "YouTube" }
}

@Composable
fun RecommendationAndSimilarMovieTabs(
    tvRecommendations: List<MovieRecommendationResult?>,
    tvSimilar: List<MovieSimilarResult?>,
    navController: NavController
) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    val tabs = listOf("Recommendation", "Similar")

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
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
            0 -> RecommendationMovieTab(
                recommendation = tvRecommendations ?: emptyList(),
                navController = navController
            )

            1 -> SimilarMovieTab(
                movieSimilar = tvSimilar ?: emptyList(),
                navController = navController
            )
        }
    }
}

@Composable
fun SimilarMovieTab(
    movieSimilar: List<MovieSimilarResult?>,
    navController: NavController
) {
    LazyRow {
        items(movieSimilar.size) { index ->
            val movieSim = movieSimilar[index]
            if (movieSim != null) {
                SimilarMovieItem(movieSim) {
                    navController.navigate(TmdbScreen.MovieDetail.route + "/${movieSim?.id}")
                }
            } else {
                Text(text = "Invalid movie data")
            }
        }
    }
}

@Composable
fun RecommendationMovieTab(
    recommendation: List<MovieRecommendationResult?>,
    navController: NavController
) {
    LazyRow {
        items(recommendation.size) { index ->
            val movie = recommendation[index]
            if (movie != null) {
                RecommendationMovieItem(movie) {
                    navController.navigate(TmdbScreen.MovieDetail.route + "/${movie?.id}")
                }
            } else {
                Text(text = "Invalid movie data")
            }
        }
    }
}

@Composable
fun SimilarMovieItem(movieSim: MovieSimilarResult?, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .width(120.dp),
        onClick = { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.align(Alignment.TopStart)) {
                val image = if (movieSim?.posterPath.isNullOrEmpty()) {
                    R.drawable.profile_no_image
                } else {
                    "https://image.tmdb.org/t/p/w500${movieSim?.posterPath}"
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
fun RecommendationMovieItem(movieRec: MovieRecommendationResult?, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .width(120.dp),
        onClick = { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.align(Alignment.TopStart)) {
                val image = if (movieRec?.posterPath.isNullOrEmpty()) {
                    R.drawable.profile_no_image
                } else {
                    "https://image.tmdb.org/t/p/w500${movieRec?.posterPath}"
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
