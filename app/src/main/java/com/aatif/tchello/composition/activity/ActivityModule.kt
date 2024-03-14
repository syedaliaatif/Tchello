package com.aatif.tchello.composition.activity

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.aatif.tchello.screens.splash_screen.SplashMvc
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides

@Module
class ActivityModule {

    @Provides
    fun layoutInflater(activity:AppCompatActivity): LayoutInflater = activity.layoutInflater

    @Provides
    fun splashMvc(layoutInflater: LayoutInflater): SplashMvc = SplashMvc(layoutInflater)

    @Provides
    fun glideRequestManager(activity: AppCompatActivity): RequestManager = Glide.with(activity)
}