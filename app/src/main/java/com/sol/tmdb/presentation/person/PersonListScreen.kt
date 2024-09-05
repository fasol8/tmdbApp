package com.sol.tmdb.presentation.person

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sol.tmdb.domain.model.person.PersonResult
import com.sol.tmdb.navigation.TmdbScreen

@Composable
fun PersonScreen(navController: NavController, viewModel: PersonViewModel = hiltViewModel()) {
    val persons by viewModel.persons.observeAsState(emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp, top = 88.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(8.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(persons.size) { index ->
                    val person = persons[index]
                    ItemPerson(person) {
                        navController.navigate(TmdbScreen.PersonDetail.route + "/${person.id}")
                    }

                    if (index == persons.size - 1) {
                        LaunchedEffect(key1 = Unit) {
                            viewModel.loadPerson()
                        }
                    }
                }

            }
        }
    }
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
                val image = "https://image.tmdb.org/t/p/w500" + person.profilePath
                AsyncImage(
                    model = image,
                    contentDescription = "profile photo",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = person.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

    }
}
