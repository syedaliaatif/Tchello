package com.aatif.tchello.screens.add_board

import com.aatif.tchello.common.firebase.FirebaseHandler
import com.aatif.tchello.common.model.Board
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.UUID
import javax.inject.Inject

class AddBoardModel @Inject constructor(private val firebaseHandler: FirebaseHandler) {

    private var model = Model()
    fun updateName(name: String) {
        model = model.copy(name = name)
    }

    fun addBoard() : Flow<FirebaseHandler.FirebaseResult<DocumentReference, String>> {
        val user = firebaseHandler.getUser()?: return flow { emit(FirebaseHandler.FirebaseResult.Failure("User is not logged in.")) }
        val board = getBoard(user)
        return firebaseHandler.createNewBoard(board).flowOn(Dispatchers.IO)
    }

    private fun getBoard(user: FirebaseUser) : Board {
        return Board(
            id = UUID.randomUUID().toString(),
            name = model.name,
            coverPhoto = "",
            tasks = emptyList(),
            memberIds = listOf(user.uid) ,
            isStarred = false,
            activityIds = emptyList(),
            ownerId = user.uid
        )
    }

    fun isValid(): Boolean {
        return model.name.isNotBlank()
    }

    data class Model(val name: String = "")
}
