package com.aatif.tchello.common

import android.text.Editable

/** Utility object class that has static functions to validate form entries.*/
object FormUtils {

    /**
     * Validates whether name provided is valid or not.
     * @param name Name to be validated.
     * */
    fun isValidName(name: String?): FormValidationResult{
        if(name == null)return FormValidationResult.Failure("Name can't be empty")
        return if(name.isBlank()) FormValidationResult.Failure("Name can't be empty.")
        else FormValidationResult.Success
    }

    /**
     * Validates whether email provided is valid or not.
     * @param email Email to be validated.
     * */
    fun isValidEmail(email: String?): FormValidationResult{
        if(email == null) return FormValidationResult.Failure("Email can't be empty.")
        return if(Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}").matchEntire(email) != null) FormValidationResult.Success
        else FormValidationResult.Failure("Enter a valid email.")
    }

    /**
     * Validates whether password provided is valid or not.
     * @param password Password to be validated.
     * */
    fun isValidPassword(password: String?): FormValidationResult{
        if(password == null) return FormValidationResult.Failure("Password can't be empty")
        else if (password.length < 8)return FormValidationResult.Failure("Password can't be less than 8 characters.")
        return FormValidationResult.Success
    }

    /** Class that defines form validation results.*/
    sealed class FormValidationResult{

        /** If form validation passed.*/
        object Success:FormValidationResult()

        /**
         * It holds data for the form validation failure.
         * @property message Error message.
         * */
        data class Failure(val message: String): FormValidationResult()
    }

}