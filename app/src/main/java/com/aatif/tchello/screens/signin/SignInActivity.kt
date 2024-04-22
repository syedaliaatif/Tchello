package com.aatif.tchello.screens.signin

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.lifecycleScope
import com.aatif.tchello.R
import com.aatif.tchello.common.FormUtils
import com.aatif.tchello.common.firebase.FirebaseHandler
import com.aatif.tchello.screens.common.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignInActivity : BaseActivity<SignInMvc>() {

    @Inject lateinit var model: SignInModel

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
        setSignInActionButtonListener()
    }

    private fun setTextFieldChangeListeners(){

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

    private fun setSignInActionButtonListener(){
        lifecycleScope.launch {
            mvc.getSignInButtonClick().flatMapLatest {
                model.signIn()
            }.collect(::handleResult)
        }
    }

    private suspend fun handleResult(result: FirebaseHandler.FirebaseResult<*, String>){
        withContext(Dispatchers.Main){
            when(result){
                is FirebaseHandler.FirebaseResult.Success ->{
                    screenNavigator.navigateToHomePage()
                }
                is FirebaseHandler.FirebaseResult.Failure -> {
                    screenNavigator.showShortToast(result.message.ifBlank { "Error occurred while signing you in." })
                }
            }
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