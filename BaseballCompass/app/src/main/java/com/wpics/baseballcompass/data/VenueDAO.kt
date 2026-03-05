package com.wpics.baseballcompass.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VenueDAO {
    @Query("SELECT * FROM stored_venue_ids")
    fun getVenueFlow(): Flow<List<StoredVenueIDs>>?

    @Query("SELECT * FROM stored_venue_ids WHERE id = :id")
    suspend fun getVenueByID(id: Int) : StoredVenueIDs?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVenue(venue : StoredVenueIDs)
}