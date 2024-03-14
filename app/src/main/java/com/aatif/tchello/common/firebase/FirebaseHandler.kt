package com.aatif.tchello.common.firebase

import com.aatif.tchello.common.toFlow
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class FirebaseHandler @Inject constructor(private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore ) {

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
                    token = snapshot.getString(FIELD_TOKEN).orEmpty()
                )
            }
            else -> null
        }
    }

    private companion object{
        const val USER_COLLECTION_PATH = "User"
        const val FIELD_ID = "id"
        const val FIELD_NAME = "name"
        const val FIELD_EMAIL = "email"
        const val FIELD_TOKEN = "token"
        const val FIELD_IMAGE = "image"
    }

}

