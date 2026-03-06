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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.google.android.gms.location.LocationServices
import com.wpics.baseballcompass.ui.BaseballCompassScreen
import com.wpics.baseballcompass.ui.MainScreen
import com.wpics.baseballcompass.ui.theme.BaseballCompassTheme
import com.wpics.baseballcompass.viewmodels.BaseballCompassViewModel
import com.wpics.baseballcompass.viewmodels.ViewModelFactory

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.activity.result.ActivityResult
import com.wpics.baseballcompass.workers.WorkScheduler
import kotlin.apply

class MainActivity : ComponentActivity() {

    /** Initialization of the ViewModel using the Manual DI container. */
    private val viewModel: BaseballCompassViewModel by viewModels {
        val container = (application as BaseballCompassApp).container
        ViewModelFactory(api = container.api, venueDAO = container.venueDao)
    }

    /** Launcher to handle location permission requests. */
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            getLocationAndFetch()
        }

        if(permissions[Manifest.permission.POST_NOTIFICATIONS] == true){
            Log.d("BaseballComPass", "Notification Permission Granted")
        }
    }


    private lateinit var sensorManager: SensorManager
    private var rotation: Sensor? = null

    private val rotationMatrix = FloatArray(9)
    private val orientation = FloatArray(3)

    private val compass = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            SensorManager.getOrientation(rotationMatrix, orientation)

            val compassHeading = (Math.toDegrees(orientation[0].toDouble()).toFloat() +360f) % 360f

            viewModel.updateHeading(compassHeading)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.POST_NOTIFICATIONS))

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        rotation = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)


        WorkScheduler.scheduleVenueRecommendation(this)
        Log.d("BaseballCompassNotification", "Notification Scheduled")

        setContent {
            var darkMode by rememberSaveable {mutableStateOf(false)}

            BaseballCompassTheme (darkTheme = darkMode, dynamicColor = false) {
                Surface(color= MaterialTheme.colorScheme.background) {
                    MainScreen(
                        locViewModel = viewModel,
                        onRefresh = {getLocationAndFetch()},
                        darkMode = darkMode,
                        onModeChange = {darkMode = it}
                    )
                }
            }
        }
    }


    override fun onResume(){
        super.onResume()
        rotation?.let{
            sensorManager.registerListener(compass, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause(){
        super.onPause()
        sensorManager.unregisterListener(compass)
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
                    viewModel.fetchData(location.latitude, location.longitude)
                    saveLastLocation(location.latitude, location.longitude)
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


    private fun saveLastLocation(lat: Double, lon: Double){
        getSharedPreferences("prefs", MODE_PRIVATE).edit().putLong("last_lat", java.lang.Double.doubleToRawLongBits(lat))
            .putLong("last_lon", java.lang.Double.doubleToRawLongBits(lon)).apply()
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


