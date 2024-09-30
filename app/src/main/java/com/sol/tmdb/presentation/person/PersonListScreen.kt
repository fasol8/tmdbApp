@file:OptIn(ExperimentalMaterial3Api::class)

package com.sol.tmdb.presentation.person

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sol.tmdb.R
import com.sol.tmdb.utils.SharedViewModel
import com.sol.tmdb.domain.model.person.PersonResult
import com.sol.tmdb.navigation.TmdbScreen

@Composable
fun PersonScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    viewModel: PersonViewModel = hiltViewModel()
) {
    val isSearchIsVisible by sharedViewModel.isSearchBarVisible.observeAsState(false)
    val persons by viewModel.persons.observeAsState(emptyList())
    var query by rememberSaveable { mutableStateOf("") }

    if (persons != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp, top = 58.dp)
        ) {
            Column {
                if (isSearchIsVisible) {
                    PersonSearchBar(
                        query = query,
                        onQueryChange = { query = it },
                        onSearch = { viewModel.searchPerson(query) }
                    )
                } else {
                    Spacer(modifier = Modifier.height(38.dp))
                }
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    items(persons.size) { index ->
                        val person = persons[index]
                        ItemPerson(person) {
                            navController.navigate(TmdbScreen.PersonDetail.route + "/${person.id}")
                        }

                        if (index == persons.size - 1) {
                            LaunchedEffect(key1 = Unit) {
                                if (isSearchIsVisible) {
                                    viewModel.searchPerson(query)
                                } else {
                                    viewModel.loadPerson()
                                }
                            }
                        }
                    }

                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(100.dp)
                )
            }
        }
    }
}

@Composable
fun PersonSearchBar(query: String, onQueryChange: (String) -> Unit, onSearch: (String) -> Unit) {
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
        placeholder = { Text(text = "Search Person") },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Movie")
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
fun ItemPerson(person: PersonResult, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                val image = if (person.profilePath.isNullOrEmpty()) {
                    R.drawable.profile_no_image
                } else {
                    "https://image.tmdb.org/t/p/w500${person.profilePath}"
                }
                AsyncImage(
                    model = image,
                    contentDescription = "profile photo",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.profile_no_image),
                    error = painterResource(id = R.drawable.profile_no_image)
                )
                Text(
                    text = person.name ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
