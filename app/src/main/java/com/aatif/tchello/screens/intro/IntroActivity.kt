package com.aatif.tchello.screens.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aatif.tchello.R
import com.aatif.tchello.screens.common.BaseActivity
import com.aatif.tchello.screens.signin.SignInActivity
import com.aatif.tchello.screens.signup.SignUpActivity
import javax.inject.Inject

class IntroActivity : BaseActivity<IntroMvc>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        setContentView(mvc.root)
        mvc.setSignInListener {
            launchSignInPage()
        }

        mvc.setSignUpListener {
            launchSignUpPage()
        }
    }

    private fun launchSignUpPage(){
        startActivity(Intent(applicationContext, SignUpActivity::class.java))
    }

    private fun launchSignInPage(){
        startActivity(Intent(applicationContext, SignInActivity::class.java))
    }
}