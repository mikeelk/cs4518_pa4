package com.wpics.baseballcompass.data

import android.content.Context
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

    /** The implementation of the WeatherAPI. */
    val api: MLBAPI by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MLBAPI::class.java)
    }
}