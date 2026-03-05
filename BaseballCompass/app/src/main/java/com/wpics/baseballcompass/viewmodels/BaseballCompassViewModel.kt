package com.wpics.baseballcompass.viewmodels

import android.util.Log
import com.wpics.baseballcompass.data.MLBAPI
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wpics.baseballcompass.data.StoredVenueIDs
import com.wpics.baseballcompass.data.VenueDAO
import com.wpics.baseballcompass.models.VenueResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class BaseballCompassViewModel(private val api: MLBAPI, private val venueDao: VenueDAO) : ViewModel() {

    /** Internal mutable state flow for the UI state. */
    private val _state = MutableStateFlow<BaseballCompassUIState>(BaseballCompassUIState.Loading)

    /** Public read-only state flow for the UI state. */
    val state: StateFlow<BaseballCompassUIState> = _state.asStateFlow()


    /** Internal mutable state flow for the swipe-to-refresh status. */
    private val _isRefreshing = MutableStateFlow(false)

    /** Public read-only state flow for the refreshing status. */
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    /** Internal mutable state flow for the last updated timestamp. */
    private val _lastUpdated = MutableStateFlow<String?>(null)

    /** Public read-only state flow for the last updated timestamp. */
    val lastUpdated: StateFlow<String?> = _lastUpdated

    /** Cache coordinates to allow re-fetching when units change **/
    private var lastLat: Double? = null
    private var lastLon: Double? = null

    private val _heading = MutableStateFlow(0f)
    val heading: StateFlow<Float> = _heading
    fun fetchData() {
        viewModelScope.launch {
            try {
                val date = getTodayDate()
                val venueResponse = VenueResponse(null)
                val scheduleResponse = api.getSchedule(1, 2026, 'S', date)
                if (scheduleResponse.dates != null) {
                    for (date in scheduleResponse.dates) {
                        if (date.games != null) {
                            for (games in date.games) {
                                Log.d("BaseballCompassApp", "Venue name is ${games.venue?.name} and venue ID is ${games.venue?.id}")
                                if (games.venue?.id != null) {
                                    val venueDetailsFromDB = venueDao.getVenueByID(games.venue.id)
                                    if (venueDetailsFromDB != null) {
                                        Log.d("BaseballCompasApp", "Venue details are saved in database and they are: ${venueDetailsFromDB.name} and ${venueDetailsFromDB.id} and ${venueDetailsFromDB.latitude} and ${venueDetailsFromDB.longitude}")
                                    }
                                    else {
                                        val venueResponse = api.getVenueDetails(games.venue.id)
                                        if (venueResponse.venues != null) {
                                            for (venue in venueResponse.venues) {
                                                Log.d("BaseballCompassApp", "Venue latitude is ${venue.location?.defaultCoordinates?.latitude} and longitude is ${venue.location?.defaultCoordinates?.longitude}")
                                                val venueData = StoredVenueIDs(games.venue.id, games.venue.name ?: "Blank", venue.location?.defaultCoordinates?.latitude ?: 0.0, venue.location?.defaultCoordinates?.longitude ?: 0.0)
                                                venueDao.insertVenue(venueData)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                _state.value = BaseballCompassUIState.Success(current = scheduleResponse, venueResponse, heading = heading.value)

            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = BaseballCompassUIState.Error(msg = e.localizedMessage ?: "Check your internet connection")
            } finally {
                _isRefreshing.value = false
            }
        }
    }


    fun updateHeading(heading: Float){

        val state = _state.value
        if(state is BaseballCompassUIState.Success){

            val prev = state.heading
            val change = Math.abs(((heading - prev + 540f) %360f) - 180f)

            if(change >= 5f) {
                _state.value = state.copy(heading = heading)
                Log.d("BaseballCompassApp", "New heading $heading")
            }
        }

    }


    /**
     * Sets the refreshing state for the Pull-to-Refresh UI component.
     *
     * @param value True if refreshing, false otherwise.
     */
    fun setRefreshing(value: Boolean) {
        _isRefreshing.value = value
    }

    fun getTodayDate() : String {
        val now = ZonedDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val format = now.format(formatter)
        return format
    }
}