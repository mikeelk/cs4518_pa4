package com.wpics.baseballcompass.workers

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object WorkScheduler{

    fun scheduleVenueRecommendation(context: Context){

        val request = PeriodicWorkRequestBuilder<VenueWorker>(16, TimeUnit.MINUTES)
            .setInputData(workDataOf())
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "venue-refresh",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )

    }
}