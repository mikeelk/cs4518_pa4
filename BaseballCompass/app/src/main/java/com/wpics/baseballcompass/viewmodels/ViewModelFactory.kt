package com.wpics.baseballcompass.viewmodels

import com.wpics.baseballcompass.data.MLBAPI
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wpics.baseballcompass.data.VenueDAO

class ViewModelFactory(private val api: MLBAPI, private val venueDAO: VenueDAO) : ViewModelProvider.Factory {
    /**
     * Creates a new instance of the requested ViewModel class.
     *
     * @param modelClass The class of the ViewModel to create.
     * @return A newly created ViewModel instance.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BaseballCompassViewModel::class.java)) {
            return BaseballCompassViewModel(api, venueDAO) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}