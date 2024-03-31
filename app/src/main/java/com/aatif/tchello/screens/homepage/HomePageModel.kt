package com.aatif.tchello.screens.homepage

import com.aatif.tchello.common.firebase.FirebaseHandler
import com.aatif.tchello.common.firebase.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomePageModel @Inject constructor(private val firebaseHandler: FirebaseHandler) {

    suspend fun getUserInformation(): User? {
        return firebaseHandler.getUserDetails()
    }

    fun signOut() {
        firebaseHandler.signOut()
    }

    fun getProfilePhoto(uri: String) = firebaseHandler.getImage(uri)
}