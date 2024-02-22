package com.aatif.tchello.screens.signup

import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import com.aatif.tchello.R
import com.aatif.tchello.common.FormUtils
import com.aatif.tchello.screens.common.BaseActivity

class SignUpActivity : BaseActivity<SignUpMvc>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        setContentView(mvc.root)
        setCustomActionBar()
        goBackOnNavigationClick()
        hideKeyboardOnOutsideTouch()
        setTextFieldChangeListeners()
    }

    private fun setCustomActionBar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setSupportActionBar(mvc.toolbar)
        supportActionBar?.setTitle(R.string.signup_toolbar_title)
    }

    private fun goBackOnNavigationClick(){
        mvc.setNavigationClickListener {
            onBackPressed()
        }
    }

    private fun setTextFieldChangeListeners(){
        mvc.setNameTextChangeListener {
            val result = FormUtils.isValidName(it)
            when(result){
                is FormUtils.FormValidationResult.Success -> mvc.setNameTextError(null)
                is FormUtils.FormValidationResult.Failure -> mvc.setNameTextError(result.message)
            }
        }

        mvc.setEmailTextChangeListener {
            val result = FormUtils.isValidEmail(it)
            when(result){
                is FormUtils.FormValidationResult.Success -> mvc.setEmailTextError(null)
                is FormUtils.FormValidationResult.Failure -> mvc.setEmailTextError(result.message)
            }
        }

        mvc.setPasswordTextChangeListener {
            val result = FormUtils.isValidPassword(it)
            when(result){
                is FormUtils.FormValidationResult.Success -> mvc.setPasswordTextError(null)
                is FormUtils.FormValidationResult.Failure -> mvc.setPasswordTextError(result.message)
            }
        }
    }

    private fun hideKeyboardOnOutsideTouch(){
        mvc.root.setOnClickListener {
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