package com.aatif.tchello.common

import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Get view clicks as a flow.
 */
fun View.getClicks(): Flow<Unit> = callbackFlow {
    setOnClickListener {
        trySend(Unit)
    }

    awaitClose {
        setOnClickListener (null)
    }
}

fun Toolbar.navigationClicks() = callbackFlow {
    setNavigationOnClickListener {
        trySend(Unit)
    }
    awaitClose{
        setNavigationOnClickListener(null)
    }
}

fun NavigationView.itemClicks() = callbackFlow {
    setNavigationItemSelectedListener { menuItem ->
        trySend(menuItem).isSuccess
    }
    awaitClose{
        setNavigationItemSelectedListener(null)
    }
}

