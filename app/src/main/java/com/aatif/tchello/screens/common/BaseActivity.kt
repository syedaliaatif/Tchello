package com.aatif.tchello.screens.common

import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.aatif.tchello.TchelloApplication
import com.aatif.tchello.composition.activity.ActivityComponent
import com.aatif.tchello.composition.activity.ActivityModule
import com.aatif.tchello.navigation.DialogManager
import com.aatif.tchello.navigation.ScreenNavigator
import com.google.firebase.auth.FirebaseAuth

import javax.inject.Inject

open class BaseActivity <MVC: BaseMvc> : AppCompatActivity() {

    protected lateinit var activityComponent : ActivityComponent

    @Inject lateinit var mvc: MVC

    @Inject lateinit var screenNavigator: ScreenNavigator

    @Inject lateinit var dialogManager: DialogManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComponent =
            (application as TchelloApplication).appComponent.newActivityComponent().activity(this)
                .build()
    }
}