package com.aatif.tchello.screens.signin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aatif.tchello.R
import com.aatif.tchello.screens.common.BaseActivity

class SignInActivity : BaseActivity<SignInMvc>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        setContentView(mvc.root)
    }
}