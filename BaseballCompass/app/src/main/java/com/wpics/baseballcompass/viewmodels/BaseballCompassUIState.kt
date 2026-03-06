package com.wpics.baseballcompass.viewmodels

import com.wpics.baseballcompass.models.ScheduleResponse
import com.wpics.baseballcompass.models.VenueResponse

sealed interface BaseballCompassUIState {
    object Loading: BaseballCompassUIState

    data class Success(
        val current : ScheduleResponse,
        val heading: Float

    ) : BaseballCompassUIState

    data class Error(val msg : String) : BaseballCompassUIState
}