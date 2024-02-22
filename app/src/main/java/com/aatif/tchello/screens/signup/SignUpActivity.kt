package com.aatif.tchello.screens.signup

import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import com.aatif.tchello.R
import com.aatif.tchello.screens.common.BaseActivity

class SignUpActivity : BaseActivity<SignUpMvc>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        setContentView(mvc.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setSupportActionBar(mvc.toolbar)
        supportActionBar?.setTitle(R.string.signup_toolbar_title)
        mvc.setNavigationClickListener {
            onBackPressed()
        }
        mvc.root.setOnClickListener{
            hideKeyboard()
        }
    }

    private fun hideKeyboard(){
        val service = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if(service.isAcceptingText){
            service.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        }
    }
}