package com.wpics.baseballcompass.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stored_venue_ids")
data class StoredVenueIDs(
    @PrimaryKey val id: Int,
    val name : String,
    val latitude: Double,
    val longitude: Double
)
