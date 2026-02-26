package com.wpics.baseballcompass.models

data class ScheduleResponse(
    val totalGames: Int?,
    val dates: List<Date>?
)