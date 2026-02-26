package com.wpics.baseballcompass

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.location.Location
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.google.android.gms.location.LocationServices
import com.wpics.baseballcompass.ui.BaseballCompassScreen
import com.wpics.baseballcompass.ui.theme.BaseballCompassTheme
import com.wpics.baseballcompass.viewmodels.BaseballCompassViewModel
import com.wpics.baseballcompass.viewmodels.ViewModelFactory

class MainActivity : ComponentActivity() {

    /** Initialization of the ViewModel using the Manual DI container. */
    private val viewModel: BaseballCompassViewModel by viewModels {
        ViewModelFactory((application as BaseballCompassApp).container.api)
    }

    /** Launcher to handle location permission requests. */
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            getLocationAndFetch()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))

        setContent {
            BaseballCompassTheme {
                Surface(color= MaterialTheme.colorScheme.background) {
                    BaseballCompassScreen(
                        viewModel = viewModel,
                        onRefresh = {
                            viewModel.setRefreshing(true)
                            getLocationAndFetch()

                        }
                    )
                }
            }
        }
    }

    /**
     * Utilizes FusedLocationProvider to get coordinates and trigger the ViewModel.
     */
    private fun getLocationAndFetch() {
        val fusedClient = LocationServices.getFusedLocationProviderClient(this)

        // Define a Priority for high accuracy
        val priority = com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY

        try {
            // CancellationToken allows the request to be canceled if it takes too long
            val cts = com.google.android.gms.tasks.CancellationTokenSource()

            fusedClient.getCurrentLocation(priority, cts.token).addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.d("BaseballCompassApp", "Fetched Location: ${location.latitude}, ${location.longitude}")
                    viewModel.fetchData()
                } else {
                    // Handle case where GPS is off
                    Log.e("BaseballCompassApp", "Location is null. GPS may be disabled.")
                    viewModel.setRefreshing(false)
                }
            }.addOnFailureListener { e ->
                Log.e("BaseballCompassApp", "Location fetch failed: ${e.message}")
                viewModel.setRefreshing(false)
            }
        } catch (e: SecurityException) {
            Log.e("BaseballCompassApp", "Permission error: ${e.message}")
            viewModel.setRefreshing(false)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BaseballCompassTheme {
        Greeting("Android")
    }
}