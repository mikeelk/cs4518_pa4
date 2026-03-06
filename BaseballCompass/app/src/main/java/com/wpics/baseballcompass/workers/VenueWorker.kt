package com.wpics.baseballcompass.workers

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.wpics.baseballcompass.data.VenueRepository
import com.wpics.baseballcompass.util.Notifications


class VenueWorker (
    appContext: Context,
    params: WorkerParameters
    ): CoroutineWorker(appContext, params){

    private fun getLastLocation(): Pair<Double, Double>? {
        val prefs = applicationContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)

        if(!prefs.contains("last_lat") || !prefs.contains("last_lon")) return null

        val lat = java.lang.Double.longBitsToDouble(prefs.getLong("last_lat", 0L))
        val lon = java.lang.Double.longBitsToDouble(prefs.getLong("last_lon", 0L))

        return Pair(lat, lon)
    }


    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result{
        val location = getLastLocation() ?: return Result.success()


        return try{
            val results = VenueRepository().getSchedule(location.first, location.second)

            val closestVenue = results.dates?.get(0)?.games?.get(0)?.venue

            Notifications.ensure(applicationContext)
            Notifications.show(applicationContext, "BaseballCompass", "Want to catch a game? The nearest venue is ${closestVenue?.distance} miles away!")

            Result.success()
        }catch(e: Exception){
            Result.retry()
        }


    }

}