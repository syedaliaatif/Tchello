package com.aatif.tchello.screens.signup

import android.text.Editable
import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import com.aatif.tchello.R
import com.aatif.tchello.screens.common.BaseMvc
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import javax.inject.Inject

/**
 * View for sign up page.
 */
class SignUpMvc @Inject constructor(layoutInflater: LayoutInflater):BaseMvc(layoutInflater, R.layout.activity_sign_up) {

    val toolbar: Toolbar by lazy{findViewById(R.id.toolbar_sign_up) as Toolbar}
    private val nameTextLayout by lazy { findViewById(R.id.text_input_layout_name) as TextInputLayout }
    private val nameTextField : TextInputEditText by lazy{findViewById(R.id.edit_text_name) as TextInputEditText}
    private val emailTextLayout by lazy { findViewById(R.id.text_input_layout_email) as TextInputLayout }
    private val emailTextField: TextInputEditText by lazy{findViewById(R.id.edit_text_email) as TextInputEditText}
    private val passwordTextLayout by lazy { findViewById(R.id.text_input_layout_password) as TextInputLayout }
    private val passwordTextField: TextInputEditText by lazy{findViewById(R.id.edit_text_password) as TextInputEditText}

    /**
     * Sets listener for navigation clicks.
     * @param listener Listener for navigation click.
     * */
    fun setNavigationClickListener(listener: ()->Unit){
        toolbar.setNavigationOnClickListener {
            listener.invoke()
        }
    }

    /**
     * Sets listener for name text change field.
     * @param listener Listener for name text field.
     */
    fun setNameTextChangeListener(listener: (Editable?)->Unit){
        nameTextField.addTextChangedListener {
            listener.invoke(it)
        }
    }

    /**
     * Sets listener for email text change field.
     * @param listener Listener for email change.
     */
    fun setEmailTextChangeListener(listener: (Editable?)->Unit){
        emailTextField.addTextChangedListener{
            listener.invoke(it)
        }
    }

    /**
     * Sets listener for password text change.
     * @param listener Listener for password change.
     */
    fun setPasswordTextChangeListener(listener: (Editable?) -> Unit){
        passwordTextField.addTextChangedListener {
            listener.invoke(it)
        }
    }

    /**
     * Sets error in name text.
     * @param error Nullable error message to be displayed. It is a valid name if error is null here.
     */
    fun setNameTextError(error: String?){
        nameTextLayout.error = error
    }

    /**
     * Sets error in email text.
     * @param error Nullable error message to be displayed. It is a valid name if error is null here.
     */
    fun setEmailTextError(error: String?){
        emailTextLayout.error = error
    }

    /**
     * Sets error in password text.
     * @param error Nullable error message to be displayed. It is a valid name if error is null here.
     */
    fun setPasswordTextError(error: String?){
        passwordTextLayout.error = error
    }

}