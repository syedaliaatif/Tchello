package com.aatif.tchello.screens.intro

import android.graphics.Typeface
import android.view.LayoutInflater
import android.widget.TextView
import com.aatif.tchello.R
import com.aatif.tchello.screens.common.BaseMvc
import com.google.android.material.button.MaterialButton
import javax.inject.Inject

class IntroMvc @Inject constructor(layoutInflater: LayoutInflater): BaseMvc(layoutInflater, R.layout.activity_intro) {

    private val appNameTv: TextView by lazy{findViewById(R.id.tv_app_name) as TextView}
    private val signInButton: MaterialButton by lazy{findViewById(R.id.button_sign_in) as MaterialButton}
    private val signUpButton: MaterialButton by lazy{findViewById(R.id.button_sign_up) as MaterialButton}
    init {
        appNameTv.typeface = Typeface.createFromAsset(root.resources.assets, "IndieFlower-Regular.ttf")
    }

    /**
     * Sets listener for signIn button click.
     * @param listener Listener for sign in button click.
     * */
    fun setSignInListener(listener: ()->Unit){
        signInButton.setOnClickListener {
            listener.invoke()
        }
    }

    /**
     * Sets listener for sign up button click.
     * @param listener Listener for sign up button click.
     * */
    fun setSignUpListener(listener: ()->Unit){
        signUpButton.setOnClickListener {
            listener.invoke()
        }
    }

}