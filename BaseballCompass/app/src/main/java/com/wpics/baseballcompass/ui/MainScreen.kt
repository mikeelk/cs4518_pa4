package com.wpics.baseballcompass.ui



import androidx.compose.foundation.layout.*

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.runtime.saveable.rememberSaveable

import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination

import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost

import com.wpics.baseballcompass.viewmodels.BaseballCompassViewModel
import com.wpics.baseballcompass.data.NavigationItem
import com.wpics.baseballcompass.ui.components.BaseballCompassBackground


/**
 * The root composable that sets up the Navigation host
 */
@Composable
fun MainScreen (locViewModel: BaseballCompassViewModel, onRefresh : () -> Unit, darkMode : Boolean, onModeChange: (Boolean) -> Unit){

    val navController = rememberNavController()

    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Guide,
        NavigationItem.Privacy,
        NavigationItem.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar{
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = {Text(item.title)},
                        selected = currentDestination?.hierarchy?.any {it.route == item.route} == true,
                        onClick = {
                            navController.navigate(item.route){
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
    ) { innerPadding ->
        BaseballCompassBackground() {
            NavHost(
                navController = navController,
                startDestination = NavigationItem.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(NavigationItem.Home.route) {
                    BaseballCompassScreen(locViewModel, {
                        locViewModel.setRefreshing(true)
                        onRefresh()
                    })
                }

                composable(NavigationItem.Guide.route) {
                    GuideScreen(locViewModel, { locViewModel.setRefreshing(true) })
                }

                composable(NavigationItem.Privacy.route) {
                    PrivacyScreen(locViewModel, { locViewModel.setRefreshing(true) })
                }

                composable(NavigationItem.Settings.route) {
                    SettingsScreen(
                        darkMode = darkMode,
                        onModeChange = onModeChange,
                        locViewModel,
                        { locViewModel.setRefreshing(true) })
                }
            }
        }

    }


}

