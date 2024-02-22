package com.aatif.tchello.screens.splash_screen

import android.graphics.Typeface
import android.view.LayoutInflater
import android.widget.TextView
import com.aatif.tchello.R
import com.aatif.tchello.screens.common.BaseMvc
import javax.inject.Inject

class SplashMvc @Inject constructor(layoutInflater: LayoutInflater): BaseMvc(layoutInflater, R.layout.activity_splash) {

    private val appNameTv: TextView by lazy{findViewById(R.id.tv_app_name) as TextView}
    init {
        val typeface = Typeface.createFromAsset(root.resources.assets,"IndieFlower-Regular.ttf")
        appNameTv.typeface = typeface
    }
}