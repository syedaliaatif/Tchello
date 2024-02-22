package com.aatif.tchello.screens.splash_screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.aatif.tchello.screens.common.BaseActivity
import com.aatif.tchello.screens.intro.IntroActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Activity responsible for splash screen. It is the first activity that is launched on application launch.*/
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<SplashMvc>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        setContentView(mvc.root)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        lifecycleScope.launch {
            delay(2000)
            val intent = Intent(applicationContext, IntroActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }

}