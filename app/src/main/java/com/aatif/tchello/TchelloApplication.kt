package com.aatif.tchello

import android.app.Application
import com.aatif.tchello.composition.application.AppComponent
import com.aatif.tchello.composition.application.AppModule
import com.aatif.tchello.composition.application.DaggerAppComponent

class TchelloApplication: Application() {

     lateinit var appComponent: AppComponent
    override fun onCreate() {
        appComponent = DaggerAppComponent.builder().build()
        super.onCreate()
    }

}