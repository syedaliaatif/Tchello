package com.aatif.tchello.common

import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.core.widget.addTextChangedListener
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
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

fun TextInputEditText.textChanges() = callbackFlow {
    val listener = object :TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { trySend(s) }
        override fun afterTextChanged(s: Editable?) = Unit
    }
    addTextChangedListener(listener)
    awaitClose {
        removeTextChangedListener(listener)
    }
}

