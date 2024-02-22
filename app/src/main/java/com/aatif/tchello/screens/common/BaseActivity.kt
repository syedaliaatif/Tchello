package com.aatif.tchello.screens.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aatif.tchello.TchelloApplication
import com.aatif.tchello.composition.activity.ActivityComponent
import com.aatif.tchello.composition.activity.ActivityModule
import com.aatif.tchello.composition.activity.DaggerActivityComponent
import javax.inject.Inject

open class BaseActivity <MVC: BaseMvc> : AppCompatActivity() {

    protected lateinit var activityComponent : ActivityComponent

    @Inject lateinit var mvc: MVC
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComponent = DaggerActivityComponent.builder()
            .activity(this)
            .activityModule(ActivityModule())
            .appComponent((application as TchelloApplication).appComponent)
            .build()
    }
}