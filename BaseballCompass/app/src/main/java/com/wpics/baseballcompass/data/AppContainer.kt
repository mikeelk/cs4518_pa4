package com.wpics.baseballcompass.data

import android.content.Context
import androidx.room.Room
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A container for manual dependency injection. This class instantiates the network layer
 * once and provides it to the rest of the app.
 *
 * @param context The application context, useful for future local storage (DataStore) additions.
 */
class AppContainer(context: Context) {

    private val baseUrl = "https://statsapi.mlb.com/api/v1/"

    private val database: BaseballDatabase by lazy {
        Room.databaseBuilder(
            context,
            BaseballDatabase::class.java,
            "baseball_compass.db"
        ).build()
    }

    val venueDao : VenueDAO by lazy {
        database.venueDao()
    }

    /** The implementation of the MLBAPI. */
    val api: MLBAPI by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MLBAPI::class.java)
    }
}