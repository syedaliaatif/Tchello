package com.aatif.tchello.screens.signup

import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import com.aatif.tchello.R
import com.aatif.tchello.screens.common.BaseMvc
import javax.inject.Inject

class SignUpMvc @Inject constructor(layoutInflater: LayoutInflater):BaseMvc(layoutInflater, R.layout.activity_sign_up) {

    val toolbar: Toolbar by lazy{findViewById(R.id.toolbar_sign_up) as Toolbar}

    fun setNavigationClickListener(listener: ()->Unit){
        toolbar.setNavigationOnClickListener {
            listener.invoke()
        }
    }

    fun setToolbarTitle( @StringRes title: Int){
        toolbar.setTitle(title)
    }

}