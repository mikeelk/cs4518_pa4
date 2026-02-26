package com.wpics.baseballcompass.viewmodels

import android.util.Log
import com.wpics.baseballcompass.data.MLBAPI
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class BaseballCompassViewModel(private val api: MLBAPI) : ViewModel() {

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
    fun fetchData() {
        viewModelScope.launch {
            try {
                val scheduleResponse = api.getSchedule(1, 2026, 'S', "2026-02-25")
                for (date in scheduleResponse.dates!!) {
                    for (games in date.games) {
                        Log.d("BaseballCompassApp", "Venue name is ${games.venue?.name} and venue ID is ${games.venue?.id}")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = BaseballCompassUIState.Error(msg = e.localizedMessage ?: "Check your internet connection")
            } finally {
                _isRefreshing.value = false
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
}