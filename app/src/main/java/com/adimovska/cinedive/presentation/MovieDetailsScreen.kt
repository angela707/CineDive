package com.adimovska.cinedive.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.adimovska.cinedive.R
import com.adimovska.cinedive.domain.models.MediaItem
import com.adimovska.cinedive.presentation.navigation.HomeGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@HomeGraph
@Destination
@Composable
fun MovieDetailsScreen(
    modifier: Modifier = Modifier,
    mediaItem: MediaItem,
    navController: DestinationsNavigator
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        MovieDetails(
            modifier = Modifier.padding(paddingValues),
            title = mediaItem.title,
            releaseDate = mediaItem.releaseDate,
            overview = mediaItem.overview,
            backdropPath = mediaItem.backdropPath,
            posterPath = mediaItem.posterPath,
            adult = mediaItem.adult,
            rating = mediaItem.voteAverage,
            onBackClicked = navController::popBackStack
        )
    }
}

@Composable
fun MovieDetails(
    modifier: Modifier = Modifier,
    title: String,
    releaseDate: String?,
    overview: String,
    backdropPath: String?,
    posterPath: String?,
    adult: Boolean,
    rating: Double,
    onBackClicked: () -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        item {
            MovieImages(
                backdropPath = backdropPath,
                posterPath = posterPath,
                onBackClicked = onBackClicked
            )
        }

        item {
            MovieInformation(
                modifier = Modifier.padding(16.dp),
                releaseDate = releaseDate,
                title = title,
                adult = adult,
                rating = rating,
                overview = overview
            )
        }
    }
}

@Composable
fun MovieInformation(
    modifier: Modifier = Modifier,
    releaseDate: String?,
    title: String,
    overview: String,
    adult: Boolean,
    rating: Double,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = title.uppercase(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            MovieRating(
                rating = rating,
                modifier = Modifier.padding(start = 8.dp)
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (releaseDate != null) {
                Text(
                    text = releaseDate,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            if (adult) {
                Icon(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(24.dp),
                    contentDescription = "Rating icon",
                    painter = painterResource(R.drawable.ic_r_rating),
                    tint = Color.Red
                )
            }

        }

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = overview,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
fun MovieRating(
    modifier: Modifier = Modifier,
    rating: Double,
    maxRating: Int = 10,
    animationDuration: Int = 1000
) {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(rating) {
        animatedProgress.animateTo(
            targetValue = (rating / maxRating).toFloat(),
            animationSpec = tween(
                durationMillis = animationDuration,
                easing = LinearOutSlowInEasing
            )
        )
    }

    val progressColor = when (animatedProgress.value * maxRating) {
        in 0.0..3.3 -> lerp(Color.Red, Color.Blue, animatedProgress.value / (3.3f / maxRating))
        in 3.4..6.6 -> lerp(
            Color.Blue,
            Color.Green,
            (animatedProgress.value - (3.3f / maxRating)) / (3.3f / maxRating)
        )

        else -> Color.Green
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(60.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = animatedProgress.value * 360f,
                useCenter = false,
                style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Butt)
            )
        }

        Text(
            text = "$rating/$maxRating",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}


@Composable
fun MovieImages(
    modifier: Modifier = Modifier,
    backdropPath: String?,
    posterPath: String?,
    onBackClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16 / 9f)
    ) {


        backdropPath?.let { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                error = painterResource(R.drawable.error_image),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        posterPath?.let { posterUrl ->
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .height(150.dp)
                    .aspectRatio(2f / 3f)
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = posterUrl,
                    contentDescription = null,
                    error = painterResource(R.drawable.error_image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Icon(
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp)
                .align(Alignment.TopStart)
                .clickable(onClick = onBackClicked),
            contentDescription = "Back icon",
            painter = painterResource(R.drawable.ic_arrow_back),
            tint = Color.White
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MovieInformationPreview() {
    MovieInformation(
        title = "Wicked",
        releaseDate = "28th November 2024",
        overview = "Set in the Land of Oz, before Dorothy Gale's arrival from Kansas, its plot follows Elphaba, the future Wicked Witch of the West, and her friendship with her classmate Galinda, who becomes Glinda the Good. ",
        adult = true,
        rating = 5.7
    )
}