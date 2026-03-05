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
 * Composable for Settings Screen
 */
@Composable
fun SettingsScreen(darkMode: Boolean, onModeChange: (Boolean) -> Unit, viewModel : BaseballCompassViewModel, onRefresh: () -> Unit){
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
                        Column(horizontalAlignment = Alignment.CenterHorizontally){
                            SettingsTitle()

                            DarkModeToggle(modifier = Modifier.padding(16.dp), darkMode = darkMode, onModeChange = onModeChange)
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun SettingsTitle(){
    Text(text = "Settings",
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace,
        )
}

@Composable
fun DarkModeToggle(modifier: Modifier, darkMode : Boolean, onModeChange : (Boolean) -> Unit){
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically){

        Text(text = "Dark Mode",
            modifier = Modifier.weight(1f))
            Switch(checked = darkMode, onCheckedChange = onModeChange)
    }
}

