package com.aatif.tchello.screens.splash_screen

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.aatif.tchello.common.firebase.FirebaseHandler
import com.aatif.tchello.screens.common.BaseActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Activity responsible for splash screen. It is the first activity that is launched on application launch.*/
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<SplashMvc>() {

    @Inject lateinit var firebaseHandler: FirebaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        val newActivityLauncher = if(!isLoggedIn()) screenNavigator::navigateToIntroPage else screenNavigator::navigateToHomePage
        setContentView(mvc.root)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        lifecycleScope.launch {
            delay(2000)
            newActivityLauncher.invoke(true)
        }
    }

    private fun isLoggedIn(): Boolean {
        return firebaseHandler.isLoggedIn()
    }

}