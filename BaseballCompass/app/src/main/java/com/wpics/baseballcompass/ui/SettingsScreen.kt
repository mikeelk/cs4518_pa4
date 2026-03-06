package com.wpics.baseballcompass.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.wpics.baseballcompass.data.VenueRepository
import com.wpics.baseballcompass.ui.components.BaseballCompassBackground
import com.wpics.baseballcompass.ui.theme.salmon
import com.wpics.baseballcompass.ui.theme.titleFontFamily
import com.wpics.baseballcompass.util.Notifications
import com.wpics.baseballcompass.viewmodels.BaseballCompassUIState
import com.wpics.baseballcompass.viewmodels.BaseballCompassViewModel
import com.wpics.baseballcompass.workers.VenueWorker
import com.wpics.baseballcompass.workers.WorkScheduler


/**
 * Composable for Settings Screen
 */
@Composable
@androidx.annotation.RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
fun SettingsScreen(darkMode: Boolean, onModeChange: (Boolean) -> Unit, notifEnabled: Boolean, viewModel : BaseballCompassViewModel, onRefresh: () -> Unit){
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
                        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally){
                            item{SettingsTitle()}

                            item{DarkModeToggle(modifier = Modifier.padding(16.dp), darkMode = darkMode, onModeChange = onModeChange)}

                            item{NotificationsToggle(modifier = Modifier.padding(16.dp), notifEnabled)}

                            item{NotifTestButton(modifier = Modifier.fillMaxWidth().padding(16.dp).height(50.dp))}
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
        color = salmon,
        fontSize = 50.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = titleFontFamily,
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


@Composable
fun NotificationsToggle(modifier: Modifier, enabled: Boolean){

    val context = LocalContext.current
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically){
        Text(text = "Notifications On/Off",
            modifier = Modifier.weight(1f))
        Switch(checked =  enabled, {if (enabled){
            WorkScheduler.scheduleVenueRecommendation(context)
        } else{
            WorkManager.getInstance(context).cancelUniqueWork("venue-refresh")
        }})

    }
}


@Composable
@androidx.annotation.RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
fun NotifTestButton(modifier: Modifier){
    val context = LocalContext.current
    Button(
        modifier = modifier,
        onClick =  {
            triggerWorkImmediately(context)
        },
    ){
        Text(text = "Notif Test",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            fontFamily = FontFamily.Monospace,)
    }
}

fun triggerWorkImmediately(context: Context) {
    val immediateWorkRequest = OneTimeWorkRequestBuilder<VenueWorker>().build()
    WorkManager.getInstance(context).enqueue(immediateWorkRequest)
}

