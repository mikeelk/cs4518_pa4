package com.wpics.baseballcompass.viewmodels

import com.wpics.baseballcompass.data.MLBAPI
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val api: MLBAPI) : ViewModelProvider.Factory {
    /**
     * Creates a new instance of the requested ViewModel class.
     *
     * @param modelClass The class of the ViewModel to create.
     * @return A newly created ViewModel instance.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BaseballCompassViewModel::class.java)) {
            return BaseballCompassViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}