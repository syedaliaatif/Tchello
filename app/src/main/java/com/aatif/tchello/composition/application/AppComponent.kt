package com.aatif.tchello.composition.application

import com.aatif.tchello.TchelloApplication
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(application: TchelloApplication)

}