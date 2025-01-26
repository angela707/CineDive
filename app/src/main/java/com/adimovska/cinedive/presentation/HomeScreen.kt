package com.adimovska.cinedive.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.adimovska.cinedive.presentation.destinations.MovieDetailsScreenDestination
import com.adimovska.cinedive.presentation.navigation.HomeGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@HomeGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: DestinationsNavigator
) {

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hello from home screen!",
        )

        Button(
            onClick = {
                navController.navigate(MovieDetailsScreenDestination)
            }
        ) {
            Text("Go to details")
        }
    }
}