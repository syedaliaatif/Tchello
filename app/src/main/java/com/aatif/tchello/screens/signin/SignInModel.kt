package com.aatif.tchello.screens.signin

import com.aatif.tchello.common.FormUtils
import com.aatif.tchello.common.firebase.FirebaseHandler
import com.aatif.tchello.common.toFlow
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignInModel @Inject constructor(private val firebaseHandler: FirebaseHandler) {
    
    private var signInDetails = SignInDetails("", "")

    /**
     *  Updates model details
     *  @param email Email of the user.
     *  @param password String of the user.
     */
    fun updateModel(email: String?=null, password: String?=null){
        email?.let{ signInDetails = signInDetails.copy(email = it)}
        password?.let{signInDetails = signInDetails.copy( password = it)}
    }

    /** Validates the current user.*/
    fun isValidUser(): Boolean{
        return  (FormUtils.isValidEmail(signInDetails.email) is FormUtils.FormValidationResult.Success)
                && (FormUtils.isValidPassword(signInDetails.password) is FormUtils.FormValidationResult.Success)
    }

    /**
     * This returns a callback flow that notifies whether user was successfully logged in or not.
     */
     fun signIn():Flow<FirebaseHandler.FirebaseAuthResult<AuthResult, String>> = firebaseHandler.signIn(signInDetails.email, signInDetails.password)


    /**
     * Data class that holds details for signing up a new user.
     * @property email Email of the user.
     * @property password Password of the user.
     */
    data class SignInDetails(val email: String, val password: String)
}
