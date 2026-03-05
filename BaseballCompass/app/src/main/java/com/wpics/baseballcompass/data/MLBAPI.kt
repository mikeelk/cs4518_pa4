package com.wpics.baseballcompass.data

import com.wpics.baseballcompass.models.ScheduleResponse
import com.wpics.baseballcompass.models.VenueResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service interface for MLB API communication.
 *
 * @version 1.0
 */
interface MLBAPI {
    @GET("schedule")
    suspend fun getSchedule(
        @Query("sportId") sportId: Int,
        @Query("season") season: Int,
        @Query("gameType") gameType: Char,
        @Query("date") date: String
    ): ScheduleResponse

    @GET("venues/{venueId}")
    suspend fun getVenueDetails(
        @Path("venueId") id : Int,
        @Query("hydrate") hydrate : String = "location"
    ) : VenueResponse
}