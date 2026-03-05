package com.wpics.baseballcompass.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [StoredVenueIDs::class], version = 1, exportSchema = false)
abstract class BaseballDatabase : RoomDatabase() {
    abstract fun venueDao(): VenueDAO
}