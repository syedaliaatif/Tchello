package com.aatif.tchello.screens.profile

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.aatif.tchello.R
import com.aatif.tchello.common.FormUtils
import com.aatif.tchello.common.firebase.User
import com.aatif.tchello.common.getClicks
import com.aatif.tchello.common.image.ImageLoader
import com.aatif.tchello.common.textChanges
import com.aatif.tchello.screens.common.BaseMvc
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileMvc @Inject constructor(layoutInflater: LayoutInflater, private val imageLoader: ImageLoader) : BaseMvc(layoutInflater, R.layout.activity_profile){
    suspend fun updateProfile(user: User?) {
        withContext(Dispatchers.Main) {
            user?.let {
                nameEt.setText(it.name)
                emailEt.setText(it.email)
                imageLoader.loadImageWithFallback(
                    it.image,
                    profilePhoto,
                    R.drawable.profile_photo_placeholder
                )
                mobileEt.setText(it.mobile)
            }
        }
    }

    fun updateClicks(): Flow<Unit> = updateButton.getClicks()
    
    fun nameChanges() = nameEt.textChanges().onEach { 
       val result = FormUtils.isValidName(it?.toString())
        when(result){
            is FormUtils.FormValidationResult.Failure -> {
                nameTil.error = result.message
            }
            else -> nameTil.error = null
        }
    }
    
    fun emailChanges() = emailEt.textChanges().onEach {
        val result = FormUtils.isValidEmail(it?.toString())
        when(result){
            is FormUtils.FormValidationResult.Failure -> {
                emailTil.error = result.message
            }
            else -> emailTil.error = null
        }
    }

    fun mobileChanges() = mobileEt.textChanges().onEach {
        val result = FormUtils.isValidMobile(it?.toString())
        when(result){
            is FormUtils.FormValidationResult.Failure -> {
                mobileTil.error = result.message
            }
            else -> mobileTil.error = null
        }
    }

    fun editProfilePictureClicks() = profilePictureEditButton.getClicks()
    fun setProfilePhoto(bitmap: Bitmap?) {
        profilePhoto.setImageBitmap(bitmap)
    }

    private val nameEt by lazy { findViewById(R.id.profile_name_et) as TextInputEditText }
    private val nameTil by lazy { findViewById(R.id.profile_name_til) as TextInputLayout }
    private val emailEt by lazy { findViewById(R.id.profile_email_et) as TextInputEditText }
    private val emailTil by lazy { findViewById(R.id.profile_email_til) as TextInputLayout }
    private val mobileEt by lazy { findViewById(R.id.profile_mobile_et) as TextInputEditText }
    private val mobileTil by lazy { findViewById(R.id.profile_mobile_til) as TextInputLayout }
    private val profilePhoto by lazy { findViewById(R.id.profile_photo) as ImageView}
    private val updateButton by lazy { findViewById(R.id.update_name_button) as MaterialButton }
    private val profilePictureEditButton by lazy { findViewById(R.id.profile_picture_edit_button) as ImageView }
    val toolbar by lazy { findViewById(R.id.toolbar) as Toolbar }
}
