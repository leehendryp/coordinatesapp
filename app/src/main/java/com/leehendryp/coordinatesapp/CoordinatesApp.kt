package com.leehendryp.coordinatesapp

import android.app.Application
import com.leehendryp.coordinatesapp.injection.AppComponent
import com.leehendryp.coordinatesapp.injection.DaggerAppComponent

class CoordinatesApp : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}