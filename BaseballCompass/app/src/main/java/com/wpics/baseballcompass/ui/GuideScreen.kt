package com.wpics.baseballcompass.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.wpics.baseballcompass.ui.components.BaseballCompassBackground
import com.wpics.baseballcompass.ui.theme.salmon
import com.wpics.baseballcompass.ui.theme.titleFontFamily
import com.wpics.baseballcompass.viewmodels.BaseballCompassUIState
import com.wpics.baseballcompass.viewmodels.BaseballCompassViewModel


/**
 * Composable for GuideScreen
 */
@Composable
fun GuideScreen(viewModel : BaseballCompassViewModel, onRefresh: () -> Unit){
    val uiState by viewModel.state.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val lastUpdated by viewModel.lastUpdated.collectAsState()


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(0.dp, 15.dp)
    ){ innerPadding ->
        BaseballCompassBackground () {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            ) {
                when (val state = uiState){
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
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)){
                            GuideTitle()
                            GuideDetails()
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun GuideTitle(){
    Text(text = "App Guide",
        fontSize = 50.sp,
        color = salmon,
        fontWeight = FontWeight.Bold,
        fontFamily = titleFontFamily
    )
}

@Composable
fun GuideDetails() {
    val listState = rememberLazyListState()
    LazyColumn(state = listState, modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        item {Text(text = "This app uses Retrofit 2 to query the MLB API to get the schedule of games for today and get the venue details of those games")}
        item {AsyncImage(model = "https://codingwithmitch.s3.amazonaws.com/static/blog/f099482c-28a2-11e9-b183-2aabe8ede8eb/retrofit2_getting_started.png", contentDescription = "Retrofit2 logo")}
        item {Text(text = "Also this app uses Fused Location Provider from Google to get the user's location to determine the distance from baseball stadiums")}
        item {Text(text = "We use a combination of the accelerometer and gyroscope to get the phone's heading so when it updates we can update the heading to the nearest stadium")}
        item {AsyncImage(model = "https://cdn-icons-png.flaticon.com/512/4336/4336206.png", contentDescription = "A compass icon")}
        item {Text(text = "In addition we use WorkManager to schedule background tasks for occasional notifications informing user of how close they are to the nearest stadium with a game")}
        item {Text(text = "Lastly, we use RoomDatabase to store stadium details such as its latitude and longitude so those don't have to be re-queried every time the app runs")}
    }
    /*
    val scrollState = rememberScrollState()
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "This app uses Retrofit 2 to query the MLB API to get the schedule of games for today and get the venue details of those games")
        AsyncImage(model = "https://codingwithmitch.s3.amazonaws.com/static/blog/f099482c-28a2-11e9-b183-2aabe8ede8eb/retrofit2_getting_started.png", contentDescription = "Retrofit2 logo")
        Text(text = "Also this app uses Fused Location Provider from Google to get the user's location to determine the distance from baseball stadiums")
        Text(text = "We use a combination of the accelerometer and gyroscope to get the phone's heading so when it updates we can update the heading to the nearest stadium")
        AsyncImage(model = "https://cdn-icons-png.flaticon.com/512/4336/4336206.png", contentDescription = "A compass icon")
        Text(text = "In addition we use WorkManager to schedule background tasks for occasional notifications informing user of how close they are to the nearest stadium with a game")
        Text(text = "Lastly, we use RoomDatabase to store stadium details such as its latitude and longitude so those don't have to be re-queried every time the app runs")
    }*/

    //Text(text = "This app uses Retrofit 2 to query the MLB API to get the schedule of games for today and get the venue details of those games")
    //AsyncImage(model = "https://codingwithmitch.s3.amazonaws.com/static/blog/f099482c-28a2-11e9-b183-2aabe8ede8eb/retrofit2_getting_started.png", contentDescription = "Retrofit2 logo")
    //Text(text = "Also this app uses Fused Location Provider from Google to get the user's location to determine the distance from baseball stadiums")
    //Text(text = "We use a combination of the accelerometer and gyroscope to get the phone's heading so when it updates we can update the heading to the nearest stadium")
    //AsyncImage(model = "https://cdn-icons-png.flaticon.com/512/4336/4336206.png", contentDescription = "A compass icon")
    //Text(text = "In addition we use WorkManager to schedule background tasks for occasional notifications informing user of how close they are to the nearest stadium with a game")
    //Text(text = "Lastly, we use RoomDatabase to store stadium details such as its latitude and longitude so those don't have to be re-queried every time the app runs")
}

@Preview(showBackground = true)
@Composable
fun GuidePreview() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(0.dp, 15.dp)
    ) {
        innerPadding ->
        BaseballCompassBackground() {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                GuideTitle()
                GuideDetails()
            }
        }
    }
}

