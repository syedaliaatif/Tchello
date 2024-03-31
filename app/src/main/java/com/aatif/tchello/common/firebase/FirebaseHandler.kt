package com.aatif.tchello.common.firebase

import android.graphics.Bitmap
import android.util.Log
import com.aatif.tchello.common.toFlow
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import java.io.InputStream
import java.util.UUID
import javax.inject.Inject

class FirebaseHandler @Inject constructor(private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore , private val firebaseStorage: FirebaseStorage) {

    fun storeUserData(user: User): Flow<FirebaseAuthResult<DocumentReference, String>>{
        return firestore.collection(USER_COLLECTION_PATH)
            .add(user)
            .toFlow()
    }

    fun signUp(email: String, password: String ): Flow<FirebaseAuthResult<AuthResult, String>> {
        return firebaseAuth.createUserWithEmailAndPassword(email, password).toFlow()
    }

     fun signIn(email: String, password: String): Flow<FirebaseAuthResult<AuthResult, String>> = firebaseAuth.signInWithEmailAndPassword(email, password).toFlow()

    fun getUser(): FirebaseUser? = firebaseAuth.currentUser

    fun signOut() = firebaseAuth.signOut()

    fun isLoggedIn() = getUser()?.uid != null

    sealed interface FirebaseAuthResult<RESULT, MESSAGE>{
        data class Success<RESULT, MESSAGE>(val result: RESULT): FirebaseAuthResult<RESULT, MESSAGE>

        data class Failure<RESULT, MESSAGE>(val message: MESSAGE): FirebaseAuthResult<RESULT, MESSAGE>
    }

    private fun <RESULT> Task<RESULT>.toFlow(): Flow<FirebaseAuthResult<RESULT, String>> = callbackFlow {
        addOnSuccessListener {
            trySend(FirebaseAuthResult.Success(it))
        }
        addOnFailureListener{
            trySend(FirebaseAuthResult.Failure(it.message.orEmpty()))
        }
        addOnCanceledListener {
            trySend(FirebaseAuthResult.Failure("Couldn't complete"))
        }
        awaitClose()
    }

    suspend fun getUserDetails(): User? {
        val result = firestore.collection(USER_COLLECTION_PATH).whereEqualTo("id", getUser()?.uid ).get().toFlow().firstOrNull()
       return when(result){
            is FirebaseAuthResult.Success -> {

                val snapshot = result.result.documents.get(0)
                User(
                    id = snapshot.getString(FIELD_ID).orEmpty(),
                    name = snapshot.getString(FIELD_NAME).orEmpty(),
                    email = snapshot.getString(FIELD_EMAIL).orEmpty(),
                    image = snapshot.getString(FIELD_IMAGE).orEmpty(),
                    token = snapshot.getString(FIELD_TOKEN).orEmpty(),
                    mobile = snapshot.getString(FIELD_MOBILE).orEmpty()
                )
            }
            else -> null
        }
    }

    private fun updatePassword(password: String): Flow<FirebaseAuthResult<Void, String>>{
        return getUser()?.updatePassword(password)?.toFlow()?.flowOn(Dispatchers.IO)?: flow { emit(FirebaseAuthResult.Failure("No user is signed in currently")) }
    }

     fun updateUserDetails(user: User): Flow<FirebaseAuthResult<Void, String>> {
       return flow {
            val result = firestore.collection(USER_COLLECTION_PATH).whereEqualTo("id", getUser()?.uid ).get().toFlow().firstOrNull()
             when(result){
                is FirebaseAuthResult.Success -> {
                    firestore.collection(USER_COLLECTION_PATH)
                        .document(result.result.documents.get(0).id)
                        .update(FIELD_NAME, user.name, FIELD_EMAIL, user.email, FIELD_IMAGE, user.image, FIELD_TOKEN, user.token, FIELD_MOBILE, user.mobile)
                        .toFlow()
                        .collect {
                            emit(it)
                        }
                }
                is FirebaseAuthResult.Failure -> emit(FirebaseAuthResult.Failure(result.message))
                else -> emit(FirebaseAuthResult.Failure("No user with the given id"))
             }
        }
    }

    fun updateEmail(email:String): Flow<FirebaseAuthResult<Void, String>> {
        val user = getUser()?: return flow { emit(FirebaseAuthResult.Failure<Void, String>("User is not authenticated.")) }
        return user.updateEmail(email).toFlow()
    }

    fun storeImage(stream: InputStream, extension: String): Flow<FirebaseAuthResult<UploadTask.TaskSnapshot, String>>{
        val uuid = UUID.randomUUID()
        return firebaseStorage.reference.child("$uuid.$extension")
            .putStream(stream)
            .toFlow()
    }

    fun getImage(uri: String): Flow<FirebaseAuthResult<ByteArray, String>> {
        return firebaseStorage.reference.child(uri).getBytes(1024L*1024L).toFlow()
    }

    private companion object{
        const val USER_COLLECTION_PATH = "User"
        const val FIELD_ID = "id"
        const val FIELD_NAME = "name"
        const val FIELD_EMAIL = "email"
        const val FIELD_TOKEN = "token"
        const val FIELD_IMAGE = "image"
        const val FIELD_MOBILE = "mobile"
    }

}

