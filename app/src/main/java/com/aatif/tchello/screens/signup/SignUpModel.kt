package com.aatif.tchello.screens.signup

import android.util.Log
import com.aatif.tchello.common.FormUtils
import com.aatif.tchello.common.firebase.FirebaseHandler
import com.aatif.tchello.common.model.User
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class SignUpModel @Inject constructor(private val firebaseHandler: FirebaseHandler) {

    private var signUpDetails = SignUpDetails("", "","")

    /**
     *  Updates model details
     *  @param name Name of the user.
     *  @param email Email of the user.
     *  @param password String of the user.
     * .*/
    fun updateModel(name: String?=null, email: String?=null, password: String?=null){
        name?.let{signUpDetails = signUpDetails.copy(name = it)}
        email?.let{signUpDetails = signUpDetails.copy(email = it)}
        password?.let{signUpDetails = signUpDetails.copy( password = it)}
    }

     fun signUp(): Flow<FirebaseHandler.FirebaseResult<AuthResult, String>>{
        if(!isValidUser()) return flow { emit(FirebaseHandler.FirebaseResult.Failure("Invalide input email and password.")) }
        val email = signUpDetails.email
        val password = signUpDetails.password
         return firebaseHandler.signUp(email, password).onEach {
             if(it is FirebaseHandler.FirebaseResult.Success){
                createUser()?.let {
                    val result = firebaseHandler.storeUserData(it).firstOrNull()
                    when(result){
                        is FirebaseHandler.FirebaseResult.Success -> {
                            logSuccess("Sucessfully logged in.")
                        }
                        is FirebaseHandler.FirebaseResult.Failure -> {
                            logError(result.message)
                        }
                        else -> Unit
                    }
                }
             }
         }
    }

    private fun createUser(): User?{
        val uid = firebaseHandler.getUser()?.uid ?:return null
        return User(
            id = uid,
            name = signUpDetails.name,
            email = signUpDetails.email
        )
    }

    /** Validates the current user.*/
    fun isValidUser(): Boolean{
        return (FormUtils.isValidName(signUpDetails.name) is FormUtils.FormValidationResult.Success)
                && (FormUtils.isValidEmail(signUpDetails.email) is FormUtils.FormValidationResult.Success)
                && (FormUtils.isValidPassword(signUpDetails.password) is FormUtils.FormValidationResult.Success)
    }

    /**
     * Data class that holds details for signing up a new user.
     * @property name Name of the user.
     * @property email Email of the user.
     * @property password Password of the user.
     */
    data class SignUpDetails(val name: String, val email: String, val password: String)

    private fun logError(log : String) {
        Log.e(TAG, log)
    }

    private fun logSuccess(log: String) {
        Log.d(TAG, log)
    }
    companion object{
        private const val TAG = "SIGNUP_MODEL"
    }
}