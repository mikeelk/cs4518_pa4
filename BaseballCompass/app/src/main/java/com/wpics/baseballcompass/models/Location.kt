package com.wpics.baseballcompass.models

data class Location (
    val address1 : String?,
    val city: String?,
    val state: String?,
    val stateAbbrev: String?,
    val postalCode: String?,
    val defaultCoordinates: Coordinate?,
    val country : String?
)