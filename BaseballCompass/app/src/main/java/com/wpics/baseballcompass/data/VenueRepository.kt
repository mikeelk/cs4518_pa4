package com.wpics.baseballcompass.data

import android.util.Log
import com.wpics.baseballcompass.models.Coordinate
import com.wpics.baseballcompass.models.ScheduleResponse
import com.wpics.baseballcompass.models.VenueResponse
import retrofit2.Retrofit
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

open class VenueRepository (
    private val api: MLBAPI = RetrofitProvider.api
) {

    open suspend fun getSchedule(lastLat : Double, lastLon: Double): ScheduleResponse {


        var scheduleResponse = api.getSchedule(1, 2026, 'S', getTodayDate())
        if (scheduleResponse.dates != null) {
            for (date in scheduleResponse.dates) {
                if (date.games != null) {
                    for (games in date.games) {
                        Log.d(
                            "BaseballCompassApp",
                            "Venue name is ${games.venue?.name} and venue ID is ${games.venue?.id}"
                        )
                        if (games.venue?.id != null) {


                            val venueResponse = api.getVenueDetails(games.venue.id)
                            if (venueResponse.venues != null) {
                                for (venue in venueResponse.venues) {
                                    games.venue.coordinates = Coordinate(
                                        venue.location?.defaultCoordinates?.latitude,
                                        venue.location?.defaultCoordinates?.longitude
                                    )
                                    games.venue.distance = haversine_distance(
                                        lastLat,
                                        lastLon,
                                        venue.location?.defaultCoordinates?.latitude!!,
                                        venue.location?.defaultCoordinates?.longitude!!
                                    ).roundToInt()
                                    Log.d(
                                        "BaseballCompassApp",
                                        "Venue latitude is ${venue.location?.defaultCoordinates?.latitude} and longitude is ${venue.location?.defaultCoordinates?.longitude}"
                                    )
                                    val venueData = StoredVenueIDs(
                                        games.venue.id,
                                        games.venue.name ?: "Blank",
                                        venue.location?.defaultCoordinates?.latitude ?: 0.0,
                                        venue.location?.defaultCoordinates?.longitude ?: 0.0
                                    )

                                }
                            }

                        }
                    }
                }
                date.games = date.games?.sortedBy { it.venue?.distance }
            }

        }

        return scheduleResponse
    }





        fun getTodayDate(): String {
            val now = ZonedDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val format = now.format(formatter)
            return format
        }

        fun haversine_distance(
            lat1: Double,
            lon1: Double,
            lat2: Double,
            lon2: Double
        ): Double {

            val radius_miles = 3959

            val delta_lat = Math.toRadians(lat2 - lat1)
            val delta_lon = Math.toRadians(lon2 - lon1)

            val a = Math.sin(delta_lat / 2).pow(2) + cos(Math.toRadians(lat1)) * cos(
                Math.toRadians(
                    lat2
                )
            ) * sin(delta_lon / 2).pow(2)

            val c = 2 * Math.atan2(sqrt(a), sqrt(1 - a))

            val dist = c * radius_miles

            return dist
        }


    }
