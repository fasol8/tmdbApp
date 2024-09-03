package com.sol.tmdb.presebtation.tv

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.sol.tmdb.domain.model.tv.TvResult

@Composable
fun TvScreen(viewModel: TvViewModel = hiltViewModel()) {
    val tvs by viewModel.tvs.observeAsState(emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column {
            Text(text = "Popular TV", style = MaterialTheme.typography.titleLarge, fontSize = 36.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(tvs.size) { index ->
                    val tv = tvs[index]
                    ItemTv(tv)

                    if (index == tvs.size - 1) {
                        LaunchedEffect(key1 = Unit) {
                            viewModel.loadTv()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemTv(tv: TvResult) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                val image = "https://image.tmdb.org/t/p/w500" + tv.posterPath
                AsyncImage(
                    model = image,
                    contentDescription = "poster TV",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = tv.name,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 8.dp, start = 8.dp)
                )
                Text(
                    text = tv.firstAirDate,
                    modifier = Modifier.padding(top = 4.dp, start = 16.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = (-8).dp, y = (-48).dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(color = Color(0xFFEEEEEE))

                    drawArc(
                        color = when {
                            ((tv.voteAverage * 10).toInt()) < 30 -> Color(0xFFEF5350)
                            ((tv.voteAverage * 10).toInt()) < 60 -> Color(0xFFFFCA28)
                            else -> Color(0xFF0F9D58)
                        },
                        startAngle = -90f,
                        sweepAngle = (tv.voteAverage * 36).toFloat(),
                        useCenter = false,
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Text(
                    text = "${(tv.voteAverage * 10).toInt()}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }
        }
    }
}
