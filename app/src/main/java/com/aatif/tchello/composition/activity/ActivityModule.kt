package com.aatif.tchello.composition.activity

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.aatif.tchello.screens.common.BaseActivity
import com.aatif.tchello.screens.splash_screen.SplashMvc
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class ActivityModule {

    companion object {
        @Provides
        fun layoutInflater(activity: BaseActivity<*>): LayoutInflater = activity.layoutInflater

        @Provides
        fun splashMvc(layoutInflater: LayoutInflater): SplashMvc = SplashMvc(layoutInflater)

        @Provides
        fun glideRequestManager(activity: BaseActivity<*>): RequestManager = Glide.with(activity)
    }

    @Binds
    abstract fun appCompatActivity(activity: BaseActivity<*>): AppCompatActivity

    @Binds
    abstract fun activityContext(activity: AppCompatActivity): Context
}