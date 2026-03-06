package com.wpics.baseballcompass.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wpics.baseballcompass.ui.components.BaseballCompassBackground
import com.wpics.baseballcompass.ui.theme.salmon
import com.wpics.baseballcompass.ui.theme.titleFontFamily
import com.wpics.baseballcompass.viewmodels.BaseballCompassUIState
import com.wpics.baseballcompass.viewmodels.BaseballCompassViewModel



/**
 * Primary Composable for the weather UI.
 *
 * @param viewModel A [WeatherViewModel] instance
 * @param onRefresh A callback function for handling refresh
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseballCompassScreen(viewModel: BaseballCompassViewModel, onRefresh: () -> Unit) {
    val uiState by viewModel.state.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val lastUpdated by viewModel.lastUpdated.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(0.dp, 15.dp) // Ensures the UI starts below the webcam/notch
    ) { innerPadding ->
        BaseballCompassBackground() {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                when (val state = uiState) {
                    is BaseballCompassUIState.Loading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is BaseballCompassUIState.Error -> {
                        Box(Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                            Text(state.msg, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.error)
                        }
                    }
                    is BaseballCompassUIState.Success -> {
                        //Creating a Column to have my two composable functions be in two different rows spaced apart by 50 dependent pixels
                        Column(verticalArrangement = Arrangement.spacedBy(25.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                            //Invoking my own composable functions
                            AppTitle()
                            DisplayVenues(modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 5.dp), state = state,
                                        boxModifier = Modifier.fillMaxWidth().clip(shape = RoundedCornerShape(16.dp)).border(width = 3.dp, shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.onBackground).background(MaterialTheme.colorScheme.surface).padding(horizontal = 5.dp, vertical = 5.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppTitle(){
    Text(text  = "Baseball Compass",
        color = salmon,
        fontSize = 50.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = titleFontFamily)
}

@Composable
fun DisplayVenues (modifier: Modifier, state: BaseballCompassUIState.Success, boxModifier: Modifier){
    Column(modifier = modifier){
        for (date in state.current.dates!!) {
            Row() {
                Text("Date: ",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp)
                Text(text = date.date!!, color = MaterialTheme.colorScheme.onBackground, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Row(){
                Text("Heading: ",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp)

                Text(
                    headingToDirection(state.heading),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
            }
            Box(modifier = boxModifier){
                Column() {
                    for (games in date.games!!) {

                        Text(
                            text = "${games.venue?.name}",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = titleFontFamily,
                            fontSize = 24.sp
                        )

                        Text(
                            text = "\t\t\t${games.venue?.distance} mi",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 24.sp
                        )

                    }
                }
            }
        }
    }
}

fun headingToDirection(heading: Float) : String{
    when(heading){
        in 0f..22.5f, in 337.5f..360f -> return "N"
        in 22.5f .. 67.5f -> return "NE"
        in 67.5f .. 112.5f -> return "E"
        in 112.5f .. 157.5f -> return "SE"
        in 157.5f .. 202.5f -> return "S"
        in 202.5f .. 247.5f -> return "SW"
        in 247.5f .. 292.5f -> return "W"
    }
    return "NW"
}






