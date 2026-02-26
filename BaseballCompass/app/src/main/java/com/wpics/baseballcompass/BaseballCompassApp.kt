package com.wpics.baseballcompass

import android.app.Application
import com.wpics.baseballcompass.data.AppContainer

class BaseballCompassApp : Application() {
    /** The dependency injection container. @version 1.0 */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}