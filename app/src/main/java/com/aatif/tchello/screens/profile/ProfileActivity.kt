package com.aatif.tchello.screens.profile

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toFile
import androidx.lifecycle.lifecycleScope
import com.aatif.tchello.R
import com.aatif.tchello.common.FormUtils
import com.aatif.tchello.common.firebase.FirebaseHandler
import com.aatif.tchello.common.navigationClicks
import com.aatif.tchello.navigation.CustomDialogBuilder
import com.aatif.tchello.screens.common.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class ProfileActivity : BaseActivity<ProfileMvc>() {

    @Inject lateinit var model: ProfileModel

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()){
        handleImageTaken(it)
    }

    private val galleryOpener = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
        handleImageSelected(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        setupUI()
    }

    private fun setupUI(){
        setContentView(mvc.root)
        setupToolbar()
        fetchProfile()
        setupProfileUIEvents()
    }


    private fun setupProfileUIEvents() {
        mvc.updateClicks()
            .onEach { updateProfile() }
            .flowOn(Dispatchers.Main)
            .launchIn(lifecycleScope)
        mvc.nameChanges()
            .onEach { model.update(name = it?.toString().orEmpty()) }
            .flowOn(Dispatchers.Main)
            .launchIn(lifecycleScope)
        mvc.emailChanges()
            .onEach { model.update(email = it?.toString().orEmpty()) }
            .flowOn(Dispatchers.Main)
            .launchIn(lifecycleScope)
        mvc.mobileChanges()
            .onEach { model.update(mobile = it?.toString().orEmpty()) }
            .flowOn(Dispatchers.Main)
            .launchIn(lifecycleScope)
        mvc.editProfilePictureClicks()
            .onEach { launchEditProfilePicture() }
            .flowOn(Dispatchers.Main)
            .launchIn(lifecycleScope)
    }

    private fun launchEditProfilePicture() {
        AlertDialog.Builder(this)
            .setItems(arrayOf("Take a Picture", "Select from Gallery")) { dialog, index ->
               when {
                   index == 0 -> openCameraToTakePicture()
                   index == 1 -> openGalleryToSelectPicture()
                   else -> Unit
               }
            }
            .create()
            .show()
    }

    private fun openCameraToTakePicture() {
        cameraLauncher.launch(null)
    }

    private fun openGalleryToSelectPicture() {
        galleryOpener.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun uploadImage(stream: InputStream, ext: String, onSuccess: ()->Unit){
        model.uploadImage(stream, ext)
            .flowOn(Dispatchers.IO)
            .flatMapConcat {
                when(it){
                    is FirebaseHandler.FirebaseAuthResult.Success -> {
                        Log.d("HandleImageSelected", "Image location is ${it.result.storage.name}")
                        model.update(image = it.result.storage.name)
                        model.updateUserDetails()
                    }
                    is FirebaseHandler.FirebaseAuthResult.Failure -> {
                        flow {
                            emit(FirebaseHandler.FirebaseAuthResult.Failure(it.message))
                        }
                    }
                }
            }.onEach {
                dialogManager.hideProgressBar()
                when(it) {
                    is FirebaseHandler.FirebaseAuthResult.Success -> {
                        screenNavigator.showShortToast("Succesfully update Image.")
                        onSuccess.invoke()
                    }
                    is FirebaseHandler.FirebaseAuthResult.Failure -> {
                        screenNavigator.showShortToast(it.message)
                    }
                }

            }
            .onStart { dialogManager.showProgressBar("Uploading Image", "Please wait for a while ...") }
            .flowOn(Dispatchers.Main)
            .launchIn(lifecycleScope)
    }

    private fun handleImageSelected(uri: Uri?) {
        if(uri == null)return
        val ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri))
        val stream = contentResolver.openInputStream(uri)
        if(ext == null) return
        if(stream == null) return
        uploadImage(stream, ext){
            mvc.setProfilePhoto(BitmapFactory.decodeStream(contentResolver.openInputStream(uri)))
        }
    }

    private fun handleImageTaken(bitmap: Bitmap?) {
        if(bitmap == null)return
       val stream =  ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val inputStream = ByteArrayInputStream(stream.toByteArray())
        uploadImage(inputStream, "jpeg"){
            mvc.setProfilePhoto(bitmap)
        }
    }

    private fun updateProfile() {
        model.updateAuthEmail().flowOn(Dispatchers.IO).flatMapConcat {
            return@flatMapConcat when (it) {
                is FirebaseHandler.FirebaseAuthResult.Success -> {
                    model.updateUserDetails().flowOn(Dispatchers.IO)
                }

                is FirebaseHandler.FirebaseAuthResult.Failure -> {
                    flow {
                        emit(it)
                    }
                }
            }
        }.onEach {
            dialogManager.hideProgressBar()
            when(it){
                is FirebaseHandler.FirebaseAuthResult.Failure -> {
                    screenNavigator.showLongToast(it.message)
                }
                is FirebaseHandler.FirebaseAuthResult.Success -> {
                    screenNavigator.showShortToast("Succesfully updated profile.")
                }
            }
        }.onStart {
            dialogManager.showProgressBar("Updating your info", "Please wait")
        }.flowOn(Dispatchers.Main).launchIn(lifecycleScope)

    }

    private fun fetchProfile(){
        lifecycleScope.launch {
            withContext(Dispatchers.IO){
                model.fetchProfile()
                mvc.updateProfile(model.user)
                model.getImage(model.user.image)
                    .flowOn(Dispatchers.IO)
                    .onEach {
                        when(it) {
                            is FirebaseHandler.FirebaseAuthResult.Failure -> {
                                Log.d("FetchProfile", "There was an error while fetching profile photo.")
                            }
                            is FirebaseHandler.FirebaseAuthResult.Success -> {
                                mvc.setProfilePhoto(BitmapFactory.decodeByteArray(it.result,0, it.result.size))
                            }
                        }
                    }.flowOn(Dispatchers.Main)
                    .launchIn(this)
            }
        }
    }

    private fun setupToolbar(){
        setSupportActionBar(mvc.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.navigation_back_arrow)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupNavigationClick()
        supportActionBar?.setTitle(R.string.profile_toolbar_title)
    }
    private fun setupNavigationClick(){
        lifecycleScope.launch {
            withContext(Dispatchers.Main){
                mvc.toolbar.navigationClicks().collect{
                    onBackPressed()
                }
            }
        }

    }
}