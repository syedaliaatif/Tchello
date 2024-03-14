package com.aatif.tchello.composition.application

import com.aatif.tchello.TchelloApplication
import com.aatif.tchello.composition.activity.ActivityComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(application: TchelloApplication)

    fun newActivityComponent(): ActivityComponent.Builder

}