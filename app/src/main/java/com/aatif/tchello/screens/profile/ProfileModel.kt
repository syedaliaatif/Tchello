package com.aatif.tchello.screens.profile

import android.graphics.Bitmap
import com.aatif.tchello.common.FormUtils
import com.aatif.tchello.common.firebase.FirebaseHandler
import com.aatif.tchello.common.firebase.User
import com.google.firebase.storage.UploadTask.TaskSnapshot
import io.grpc.internal.DnsNameResolver.SrvRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.InputStream
import javax.inject.Inject

class ProfileModel @Inject constructor(private val firebaseHandler: FirebaseHandler) {

    var user = User()

    init {
        user = user.copy(id = firebaseHandler.getUser()?.uid.orEmpty())
    }
    suspend fun fetchProfile()  {
        firebaseHandler.getUserDetails()?.also{
            user = it
        }
    }

     fun updateUserDetails(): Flow<FirebaseHandler.FirebaseAuthResult<Void, String>> {
        return firebaseHandler.updateUserDetails(user)
    }

    fun updateAuthEmail(): Flow<FirebaseHandler.FirebaseAuthResult<Void, String>>{
        return firebaseHandler.updateEmail(email = user.email)
    }

    fun update(name: String? = null, email: String? = null, image: String? = null, mobile: String?=null) {
        synchronized(user){
            name?.let { user = user.copy(name = it) }
            email?.let { user = user.copy(email = it) }
            image?.let{ user = user.copy(image = it)}
            mobile?.let{ user = user.copy(mobile = it)}
        }
    }

    fun uploadImage(stream: InputStream, extension:String): Flow<FirebaseHandler.FirebaseAuthResult<TaskSnapshot,String>>{
        return firebaseHandler.storeImage(stream, extension)
    }

    fun getImage(uri: String) = firebaseHandler.getImage(uri)


}