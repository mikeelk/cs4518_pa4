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
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace)

}

@Composable
fun DisplayVenues (modifier: Modifier, state: BaseballCompassUIState.Success, boxModifier: Modifier){
    Column(modifier = modifier){
        for (date in state.current.dates!!) {
            Text("${date.date!!} ${state.heading}",
                color = MaterialTheme.colorScheme.onBackground)
            Box(modifier = boxModifier){
                Column() {
                    for (games in date.games!!) {

                        Text(
                            text = "${games.venue?.name}",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = FontFamily.Monospace
                        )

                    }
                }
            }
        }
    }
}






