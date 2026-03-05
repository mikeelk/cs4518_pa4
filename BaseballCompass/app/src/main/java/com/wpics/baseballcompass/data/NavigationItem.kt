package com.wpics.baseballcompass.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Sealed class representing the different screens in the app.
 * Each item contains the route string, a display title, and an icon.
 *
 * @property route The navigation path used by the NavHost.
 * @property title The string displayed in the Bottom Navigation Bar.
 * @property icon The ImageVector icon displayed in the Bottom Navigation Bar.
 */
sealed class NavigationItem(val route: String, val title: String, val icon: ImageVector) {

    object Home : NavigationItem("home", "Home", Icons.Default.Home)
    object Guide : NavigationItem("guide", "Guide", Icons.Default.Menu)
    object Privacy : NavigationItem("privacy", "Privacy", Icons.Default.PrivacyTip)
    object Settings : NavigationItem("settings", "Settings", Icons.Default.Settings)

    /**
     * Detail route for breweries.
     * Note: This isn't in the bottom bar, but still needs a route definition.
     */
    object Detail : NavigationItem("detail/{breweryId}", "Details", Icons.Default.Search) {
        fun createRoute(id: String) = "detail/$id"
    }
}