package com.wpics.baseballcompass.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
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
 * Composable for Privacy Screen
 */
@Composable
fun PrivacyScreen(viewModel : BaseballCompassViewModel, onRefresh: () -> Unit){
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
                        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)){
                            item{PrivacyTitle()}
                            item{PrivacyDetails()}
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun PrivacyTitle(){
    Text(text = "Privacy Notice",
        fontSize = 50.sp,
        color = salmon,
        fontWeight = FontWeight.Bold,
        fontFamily = titleFontFamily
    )
}

@Composable
fun PrivacyDetails() {
    Text(text = "This app uses the following permissions: ", fontSize = 18.sp)
    PrivacyList()
    Text(text = "No location data is stored to the cloud or stored locally, only stadium coordinates are stored locally", fontSize = 18.sp)
    Text(text = "No sensor data (accelerometer or gyroscope) is saved locally or to the cloud", fontSize = 18.sp)
    Text(text = "You can disable notifications in the settings menu", fontSize = 18.sp)
    AsyncImage(model = "https://dataprivacymanager.net/wp-content/uploads/2019/10/Data-Privacy-vs.-Data-Security.png", contentDescription = "A privacy icon")
}

@Composable
fun PrivacyList() {
    val itemOne = "Internet"
    val itemTwo = "Location"
    val itemThree = "Notifications"
    val items = listOf(itemOne, itemTwo, itemThree)
    Column() {
        items.forEach { item ->
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = "\u2022",
                    fontSize = 30.sp,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                )
                Text(
                    text = item,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrivacyPreview() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(0.dp, 15.dp)
    ){ innerPadding ->
        BaseballCompassBackground () {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                PrivacyTitle()
                PrivacyDetails()
            }
        }
    }
}


