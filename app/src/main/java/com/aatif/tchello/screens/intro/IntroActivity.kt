package com.aatif.tchello.screens.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.aatif.tchello.R
import com.aatif.tchello.screens.common.BaseActivity
import com.aatif.tchello.screens.signin.SignInActivity
import com.aatif.tchello.screens.signup.SignUpActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class IntroActivity : BaseActivity<IntroMvc>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        setContentView(mvc.root)
        lifecycleScope.launch {
            mvc.getSignInClicks()
                .collect{
                    withContext(Dispatchers.Main){
                        screenNavigator.navigateToSignInPage()
                    }
                }
        }

        lifecycleScope.launch {
            mvc.getSignUpClicks()
                .collect{
                    withContext(Dispatchers.Main){
                        screenNavigator.navigateToSignUpPage()
                    }
                }
        }

    }
}