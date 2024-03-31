package com.aatif.tchello.composition.application

import android.app.Application
import com.aatif.tchello.TchelloApplication
import com.aatif.tchello.common.firebase.FirebaseHandler
import com.aatif.tchello.composition.activity.ActivityComponent
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(subcomponents = [ActivityComponent::class])
class AppModule {

    @Singleton
    @Provides
    fun firebaseAuth(): FirebaseAuth {
       return Firebase.auth
    }

    @Singleton
    @Provides
    fun firestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Singleton
    @Provides
    fun firebaseHandler(firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore, firebaseStorage: FirebaseStorage): FirebaseHandler{
        return FirebaseHandler(firebaseAuth, firestore, firebaseStorage)
    }

    @Singleton
    @Provides
    fun firebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

}