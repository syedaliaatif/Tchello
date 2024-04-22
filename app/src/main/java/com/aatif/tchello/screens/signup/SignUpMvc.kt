package com.aatif.tchello.screens.signup

import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import com.aatif.tchello.R
import com.aatif.tchello.common.FormUtils
import com.aatif.tchello.common.getClicks
import com.aatif.tchello.common.textChanges
import com.aatif.tchello.screens.common.BaseMvc
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class SignUpMvc @Inject constructor(layoutInflater: LayoutInflater):BaseMvc(layoutInflater, R.layout.activity_sign_up) {

    val toolbar: Toolbar by lazy{findViewById(R.id.toolbar_sign_up) as Toolbar}
    private val signUpButton: MaterialButton by lazy { findViewById(R.id.sign_up_page_action_button) as MaterialButton}
    private val nameEt by lazy { findViewById(R.id.edit_text_name) as TextInputEditText }
    private val nameTil by lazy { findViewById(R.id.text_input_layout_name) as TextInputLayout}
    private val emailEt by lazy { findViewById(R.id.edit_text_email) as TextInputEditText }
    private val emailTil by lazy { findViewById(R.id.text_input_layout_email) as TextInputLayout }
    private val passwordEt by lazy { findViewById(R.id.edit_text_password) as TextInputEditText }
    private val passwordTil by lazy { findViewById(R.id.text_input_layout_password) as TextInputLayout}


    fun setNavigationClickListener(listener: ()->Unit){
        toolbar.setNavigationOnClickListener {
            listener.invoke()
        }
    }

    fun setToolbarTitle( @StringRes title: Int){
        toolbar.setTitle(title)
    }

    fun signUpClicks() = signUpButton.getClicks()

    fun nameTextChanges() = nameEt.textChanges().onEach {
        val result = FormUtils.isValidName(it?.toString())
        when(result){
            is FormUtils.FormValidationResult.Failure -> nameTil.error = result.message
            is FormUtils.FormValidationResult.Success -> nameTil.error = null
        }
    }

    fun emailTextChanges() = emailEt.textChanges().onEach {
        val result = FormUtils.isValidEmail(it?.toString())
        when(result){
            is FormUtils.FormValidationResult.Failure -> emailTil.error = result.message
            is FormUtils.FormValidationResult.Success -> emailTil.error = null
        }
    }

    fun passwordTextChanges() = passwordEt.textChanges().onEach {
        val result = FormUtils.isValidPassword(it?.toString())
        when(result){
            is FormUtils.FormValidationResult.Failure -> passwordTil.error = result.message
            is FormUtils.FormValidationResult.Success -> passwordTil.error = null
        }
    }

}