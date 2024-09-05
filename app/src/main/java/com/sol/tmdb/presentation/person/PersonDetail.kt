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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.sol.tmdb.domain.model.person.Gender
import com.sol.tmdb.domain.model.person.PersonDetail

@Composable
fun PersonDetail(personId: Int, viewModel: PersonViewModel = hiltViewModel()) {
    val person by viewModel.personById.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()

    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(key1 = personId) {
        viewModel.searchPersonById(personId)
    }

    LaunchedEffect(key1 = person) {
        if (person != null)
            isLoading.value = true
    }

    when {
        person != null -> {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn {
                    item {
                        PersonCard(person!!)
                    }
                }
            }
        }

        isLoading.value -> {
            CircularProgressIndicator()
        }

        else -> {
            Text(
                text = errorMessage ?: "Unknown error",
                modifier = Modifier.padding(16.dp, top = 96.dp)
            )
        }
    }
}

@Composable
fun PersonCard(person: PersonDetail) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp, start = 12.dp, end = 12.dp, bottom = 28.dp),
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
                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                    Text(
                        text = person.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp)
                    )
                    Row {
                        Text(
                            text = person.birthday + if (person.deathDay != null) "-${person.deathDay}" else "",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier
                                .padding(2.dp)
                                .weight(1f)
                        )
                        Text(
                            text = Gender.fromValue(person.gender).name.replace("_", "  "),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .padding(2.dp)
                                .weight(1f)
                        )
                    }
                    Row {
                        Text(
                            text = person.placeOfBirth ?: "Unknown",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .padding(2.dp)
                                .weight(2f)
                        )
                        Text(
                            text = person.knownForDepartment,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .padding(2.dp)
                                .weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = person.biography, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
