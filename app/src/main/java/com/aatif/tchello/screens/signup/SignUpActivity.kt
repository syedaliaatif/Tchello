package com.aatif.tchello.screens.signup

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.aatif.tchello.R
import com.aatif.tchello.common.FormUtils
import com.aatif.tchello.common.firebase.FirebaseHandler
import com.aatif.tchello.screens.common.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignUpActivity : BaseActivity<SignUpMvc>() {

    @Inject lateinit var model: SignUpModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        setContentView(mvc.root)
        setupUI()
    }

    private fun setupUI(){
        setCustomActionBar()
        goBackOnNavigationClick()
        hideKeyboardOnOutsideTouch()
        setTextFieldChangeListeners()
        setActionButtonListener()
    }

    private fun setActionButtonListener(){
        lifecycleScope.launch {
            mvc.signUpClicks().onEach { withContext(Dispatchers.Main){screenNavigator.showProgressBar("Sign Up", "Wait, signing you up!")} }.flatMapLatest {
                model.signUp().flowOn(Dispatchers.IO)
            }.collect(::handleResult)
        }
    }

    private suspend fun handleResult(result: FirebaseHandler.FirebaseAuthResult<*,String>)  =
        withContext(Dispatchers.Main) {
            screenNavigator.hideProgressBar()
            when (result) {
                is FirebaseHandler.FirebaseAuthResult.Success -> {
                    screenNavigator.navigateToHomePage()
                }

                is FirebaseHandler.FirebaseAuthResult.Failure -> {
                    screenNavigator.showShortToast(result.message.ifBlank { "Error occurred while signing you up." })
                }
            }
        }
    private suspend fun showProgressBar(){
        withContext(Dispatchers.Main){
            screenNavigator.showProgressBar("Sign Up" ,"Wait, signing you up!")
        }
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
            model.updateModel( name = it.toString())
            val result = FormUtils.isValidName(it?.toString())
            when(result){
                is FormUtils.FormValidationResult.Success -> mvc.setNameTextError(null)
                is FormUtils.FormValidationResult.Failure -> mvc.setNameTextError(result.message)
            }
        }

        mvc.setEmailTextChangeListener {
            model.updateModel(email = it.toString())
            val result = FormUtils.isValidEmail(it?.toString())
            when(result){
                is FormUtils.FormValidationResult.Success -> mvc.setEmailTextError(null)
                is FormUtils.FormValidationResult.Failure -> mvc.setEmailTextError(result.message)
            }
        }

        mvc.setPasswordTextChangeListener {
            model.updateModel(password = it.toString())
            val result = FormUtils.isValidPassword(it?.toString())
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