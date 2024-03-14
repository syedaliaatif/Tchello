package com.aatif.tchello.common

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

fun Task<AuthResult>.toFlow() = callbackFlow {
    addOnSuccessListener {
        trySend(Pair(it, null))
    }
    addOnFailureListener {
        trySend(Pair(null, it))
    }
    awaitClose { }
}