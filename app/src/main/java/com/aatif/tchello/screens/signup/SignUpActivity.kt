package com.aatif.tchello.screens.signup

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.lifecycleScope
import com.aatif.tchello.R
import com.aatif.tchello.common.firebase.FirebaseHandler
import com.aatif.tchello.screens.common.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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
            mvc.signUpClicks().onEach { withContext(Dispatchers.Main){dialogManager.showProgressBar("Sign Up", "Wait, signing you up!")} }.flatMapLatest {
                model.signUp().flowOn(Dispatchers.IO)
            }.collect(::handleResult)
        }
    }

    private suspend fun handleResult(result: FirebaseHandler.FirebaseResult<*,String>)  =
        withContext(Dispatchers.Main) {
            dialogManager.hideProgressBar()
            when (result) {
                is FirebaseHandler.FirebaseResult.Success -> {
                    screenNavigator.navigateToHomePage()
                }

                is FirebaseHandler.FirebaseResult.Failure -> {
                    screenNavigator.showShortToast(result.message.ifBlank { "Error occurred while signing you up." })
                }
            }
        }
    private suspend fun showProgressBar(){
        withContext(Dispatchers.Main){
            dialogManager.showProgressBar("Sign Up" ,"Wait, signing you up!")
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
        mvc.nameTextChanges()
            .onEach{ model.updateModel( name = it.toString()) }
            .flowOn(Dispatchers.Main)
            .launchIn(lifecycleScope)

        mvc.emailTextChanges()
            .onEach { model.updateModel(email = it.toString()) }
            .flowOn(Dispatchers.Main)
            .launchIn(lifecycleScope)

        mvc.passwordTextChanges()
            .onEach { model.updateModel(password = it.toString()) }
            .flowOn(Dispatchers.Main)
            .launchIn(lifecycleScope)
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