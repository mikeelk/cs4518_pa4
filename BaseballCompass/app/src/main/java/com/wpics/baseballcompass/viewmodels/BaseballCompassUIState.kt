package com.wpics.baseballcompass.viewmodels

import com.wpics.baseballcompass.models.ScheduleResponse

sealed interface BaseballCompassUIState {
    object Loading: BaseballCompassUIState

    data class Success(
        val current : ScheduleResponse
    ) : BaseballCompassUIState

    data class Error(val msg : String) : BaseballCompassUIState
}