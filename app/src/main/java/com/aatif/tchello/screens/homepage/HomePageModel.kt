package com.aatif.tchello.screens.homepage

import android.util.Log
import com.aatif.tchello.common.firebase.FirebaseHandler
import com.aatif.tchello.common.model.Board
import com.aatif.tchello.common.model.Task
import com.aatif.tchello.common.model.User
import com.google.common.collect.ImmutableList
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.util.ArrayList
import javax.inject.Inject

class HomePageModel @Inject constructor(private val firebaseHandler: FirebaseHandler) {

    suspend fun getUserInformation(): User? {
        return firebaseHandler.getUserDetails()
    }

    fun signOut() {
        firebaseHandler.signOut()
    }

    fun getProfilePhoto(uri: String) = firebaseHandler.getImage(uri)
    fun fetchBoards() : Flow<List<Board>> {
        val userId = firebaseHandler.getUser()?.uid ?: return flow { emit(emptyList()) }
        return firebaseHandler.getBoardsForAUser(userId).flowOn(Dispatchers.IO).map {
            when(it) {
                is FirebaseHandler.FirebaseResult.Failure -> {
                    Log.w("HomePageActivity", "Could not fetch boards.: ${it.message}")
                   emptyList()
                }
                is FirebaseHandler.FirebaseResult.Success -> {
                    val boards = it.result.documents
                    boards.toModelList()
                }
            }
        }
    }

    fun List<DocumentSnapshot>.toModelList(): List<Board> =
        map {
            Board.fromMap(it.data)
        }.filterNotNull()
}