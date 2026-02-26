package com.wpics.baseballcompass.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
                        Column(verticalArrangement = Arrangement.spacedBy(50.dp)) {
                            //Invoking my own composable functions
                        }
                    }
                }
            }
        }
    }
}




