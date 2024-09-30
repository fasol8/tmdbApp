package com.sol.tmdb.presentation.person

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.sol.tmdb.R
import com.sol.tmdb.utils.SharedViewModel
import com.sol.tmdb.domain.model.person.Gender
import com.sol.tmdb.domain.model.person.ImagesProfile
import com.sol.tmdb.domain.model.person.MovieCast
import com.sol.tmdb.domain.model.person.MovieCreditsResponse
import com.sol.tmdb.domain.model.person.PersonDetail
import com.sol.tmdb.domain.model.person.TvCast
import com.sol.tmdb.domain.model.person.TvCreditsResponse
import com.sol.tmdb.navigation.TmdbScreen
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PersonDetail(
    personId: Int,
    navController: NavController,
    sharedViewModel: SharedViewModel,
    viewModel: PersonViewModel = hiltViewModel()
) {
    val person by viewModel.personById.observeAsState()
    val creditsMovies by viewModel.creditsMovies.observeAsState()
    val creditsTvs by viewModel.creditsTv.observeAsState()
    val imagesProfile by viewModel.personImages.observeAsState(emptyList())
    val errorMessage by viewModel.errorMessage.observeAsState()

    LaunchedEffect(key1 = personId, sharedViewModel) {
        viewModel.observeLanguage(sharedViewModel, personId)
    }

    when {
        person != null -> {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn {
                    item {
                        person?.let {
                            PersonCard(
                                person = it,
                                creditsMovies,
                                creditsTvs,
                                imagesProfile,
                                navController
                            )
                        } ?: run {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(100.dp)
                            )
                        }
                    }
                }
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
fun PersonCard(
    person: PersonDetail,
    personMovieCredits: MovieCreditsResponse?,
    personCreditsTvs: TvCreditsResponse?,
    imagesProfile: List<ImagesProfile?>,
    navController: NavController
) {
    val movieCredits = personMovieCredits?.cast ?: emptyList()
    movieCredits.plus(personMovieCredits?.crew) // Solo se utiliza el poster
    val tvCredits = personCreditsTvs?.cast ?: emptyList()
    tvCredits.plus(personCreditsTvs?.crew)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp, start = 4.dp, end = 4.dp, bottom = 28.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                val image = "https://image.tmdb.org/t/p/w500" + person.profilePath
                AsyncImage(
                    model = image, contentDescription = "person profile",
                    modifier = Modifier.size(380.dp),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                    Text(
                        text = person.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp)
                    )
                    BirthDayAndGenre(person)
                    PlaceOfBirthDayAndDepartment(person)
                    Spacer(modifier = Modifier.height(4.dp))
                    BiographyText(person.biography)
                    Text(
                        text = "Known For",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    MovieAndTvTabs(movieCredits, tvCredits, navController)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Gallery",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    ImageCarousel(imagesProfile)
                }
            }
        }
    }
}

@Composable
fun BirthDayAndGenre(person: PersonDetail) {
    Row {
        if (person.birthday != null) {
            Text(
                text = "(${calculateAge(person.birthday)} years old) " + person.birthday + if (person.deathDay != null) "-${person.deathDay}" else "",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .padding(2.dp)
                    .weight(1f)
            )
        } else {
            Text(
                text = "(?? years old)",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .padding(2.dp)
                    .weight(1f)
            )
        }
        Text(
            text = Gender.fromValue(person.gender).name.replace("_", "  "),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .border(1.dp, MaterialTheme.colorScheme.primary)
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateAge(birthday: String, format: String = "yyyy-MM-dd"): Int {
    return if (birthday != null) {
        val formatter = DateTimeFormatter.ofPattern(format)
        val birthLocalDate = LocalDate.parse(birthday, formatter)
        val currentDate = LocalDate.now()
        Period.between(birthLocalDate, currentDate).years
    } else {
        0
    }
}

@Composable
fun PlaceOfBirthDayAndDepartment(person: PersonDetail) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(
            text = person.placeOfBirth ?: "Unknown",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(2.dp)
                .weight(1f)
        )
        Text(
            text = person.knownForDepartment,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .border(1.dp, MaterialTheme.colorScheme.secondary)
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun BiographyText(biography: String) {
    var expand by rememberSaveable { mutableStateOf(false) }
    val showReadMore = biography.length > 196

    Box(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text(
            text = if (expand) biography else biography.take(196),
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium
        )
        if (showReadMore) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { expand = !expand }) {
                    Text(
                        text = if (expand) "< Read less" else "Read more >",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(top = 59.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MovieAndTvTabs(
    movieCredits: List<MovieCast>,
    tvCredits: List<TvCast>,
    navController: NavController
) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
    val tabs = listOf("Movie", "Tv Series")

    Column {
        TabRow(selectedTabIndex = selectedTabIndex, modifier = Modifier.fillMaxWidth()) {
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
                    })
            }
        }
        when (selectedTabIndex) {
            0 -> MovieTab(movie = movieCredits, navController = navController)
            1 -> TvTab(tvs = tvCredits, navController = navController)
        }
    }
}

@Composable
fun TvTab(tvs: List<TvCast>, navController: NavController) {
    LazyRow {
        items(tvs.size) { index ->
            val tv = tvs[index]
            tv.let {
                ItemCreditTv(it) {
                    navController.navigate(TmdbScreen.TvDetail.route + "/${it.id}")
                }
            }
        }
    }
}

@Composable
fun MovieTab(movie: List<MovieCast>, navController: NavController) {
    LazyRow {
        items(movie.size) { index ->
            val credit = movie[index]
            ItemCreditsMovie(credit) {
                navController.navigate(TmdbScreen.MovieDetail.route + "/${credit.id}")
            }
        }
    }
}

@Composable
fun ItemCreditsMovie(movie: MovieCast, onClick: () -> Unit) { // Solo se utiliza el poster
    Card(
        modifier = Modifier
            .padding(4.dp)
            .width(120.dp),
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
                    contentDescription = "poster movie",
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
fun ItemCreditTv(tv: TvCast, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .width(120.dp),
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
                    contentDescription = "poster tv",
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageCarousel(imagesProfile: List<ImagesProfile?>) {
    val pagerState = rememberPagerState(initialPage = 0)

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(50.dp),
            count = imagesProfile.size,
            modifier = Modifier
                .fillMaxSize()
        ) { page ->
            val imageProfile = imagesProfile[page]
            HeroItemImage(imageProfile = imageProfile, isHero = page == pagerState.currentPage)
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
fun HeroItemImage(imageProfile: ImagesProfile?, isHero: Boolean) {
    val scale = animateFloatAsState(if (isHero) 1.2f else 0.85f).value

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(8.dp)
            .scale(scale),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        val image = imageProfile?.filePath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: ""
        AsyncImage(
            model = image,
            contentDescription = "profile image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.no_image),
            error = painterResource(id = R.drawable.no_image)
        )
    }
}