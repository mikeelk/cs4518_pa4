package com.wpics.baseballcompass.models

data class Venue(
    val id: Int?,
    val name: String?,
    var coordinates: Coordinate?,
    var distance: Int?
)