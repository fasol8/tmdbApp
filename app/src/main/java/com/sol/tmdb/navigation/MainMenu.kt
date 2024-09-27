package com.sol.tmdb.navigation

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sol.tmdb.LanguageChangeHelper
import com.sol.tmdb.R
import com.sol.tmdb.presentation.main.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainMenu(mainViewModel: MainViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val languageChangeHelper by lazy { LanguageChangeHelper() }

    val allLanguages = listOf(
        Language("en", "English", R.drawable.ic_movie), //TODO:change flag
        Language("es", "Español", R.drawable.ic_person), //TODO:change flag
    )

    val currentLanguageCode: String = languageChangeHelper.getLanguageCode(context)

    var currentLanguage by remember { mutableStateOf(currentLanguageCode) }
    val onCurrentLanguageChange: (String) -> Unit = { newLanguage ->
        currentLanguage = newLanguage
        languageChangeHelper.changeLanguage(context, newLanguage)

        val activity = (context as? Activity)
        activity?.recreate()
    }

    val items = listOf(
        TmdbScreen.NowPlaying to R.drawable.ic_movie,
        TmdbScreen.PopularMovies to R.drawable.ic_movie,
        TmdbScreen.TopRatedMovies to R.drawable.ic_movie,
        TmdbScreen.UpcomingMovies to R.drawable.ic_movie,
        TmdbScreen.AirToday to R.drawable.ic_tv,
        TmdbScreen.OnAir to R.drawable.ic_tv,
        TmdbScreen.PopularTv to R.drawable.ic_tv,
        TmdbScreen.TopRatedTv to R.drawable.ic_tv,
        TmdbScreen.Person to R.drawable.ic_person,
    )

    val titles = mapOf(
        TmdbScreen.NowPlaying.route to "Now Playing",
        TmdbScreen.PopularMovies.route to "Popular",
        TmdbScreen.TopRatedMovies.route to "Top Rated Movies",
        TmdbScreen.UpcomingMovies.route to "Upcoming",
        TmdbScreen.AirToday.route to "Airing Tv",
        TmdbScreen.OnAir.route to "On the Air",
        TmdbScreen.PopularTv.route to "Popular TV",
        TmdbScreen.TopRatedTv.route to "Top Rated TV",
        TmdbScreen.Person.route to "Person",
    )


    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val currentTitle = titles[currentRoute] ?: "TMDB App"

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 96.dp),
                color = if (drawerState.isOpen) MaterialTheme.colorScheme.primary else Color.Transparent
            ) {
                Column {
                    Text(
                        text = "TMDB App",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(18.dp)
                    )
                    HorizontalDivider(thickness = 2.dp, modifier = Modifier.width(220.dp))
                    items.forEach { (screen, icon) ->
                        Row(modifier = Modifier
                            .padding(24.dp)
                            .clickable {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                coroutineScope.launch { drawerState.close() }
                            }) {
                            Icon(
                                painter = painterResource(id = icon),
                                contentDescription = screen.title
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = screen.title)
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = currentTitle) },
                    navigationIcon = {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { mainViewModel.toggleSearchBar() }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                        }
                        LanguagesDropdown(
                            modifier = Modifier
//                                    .background(MaterialTheme.colorScheme.background)
                                .padding(top = 8.dp),
                            languagesList = allLanguages,
                            currentLanguage = currentLanguage,
                            onCurrentLanguageChange = onCurrentLanguageChange
                        )
                        Log.i("LanguageDropdown", "Current language code: $currentLanguage")
                    }
                )
            }
        ) {
            TmdbNavHost(navController = navController, mainViewModel = mainViewModel)
        }
    }
}

@Composable
fun LanguagesDropdown(
    modifier: Modifier,
    languagesList: List<Language>,
    currentLanguage: String,
    onCurrentLanguageChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember {
        mutableStateOf(languagesList.firstOrNull { it.code == currentLanguage }
            ?: languagesList.first())
    }

    Box(
        modifier = modifier
            .padding(end = 16.dp)
//            .wrapContentSize(Alignment.TopEnd)
    ) {
        Row(
            modifier = Modifier
                .height(24.dp)
                .clickable {
                    expanded = !expanded
                }
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LanguageListItem(selectedItem)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            repeat(languagesList.size) {
                val item = languagesList[it]
                DropdownMenuItem(text = {
                    LanguageListItem(selectedItem = item)
                }, onClick = {
                    selectedItem = item
                    expanded = !expanded
                    onCurrentLanguageChange(selectedItem.code)
                })
            }

        }
    }
}

@Composable
fun LanguageListItem(selectedItem: Language) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape),
            painter = painterResource(selectedItem.flag),
            contentScale = ContentScale.Crop,
            contentDescription = selectedItem.code
        )

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = selectedItem.name,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.W500,
                color = MaterialTheme.colorScheme.onBackground,
            )
        )
    }
}

data class Language(
    val code: String,
    val name: String,
    @DrawableRes val flag: Int
)
