package com.aatif.tchello.screens.common

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

open class BaseMvc(protected val layoutInflater: LayoutInflater, @LayoutRes resId: Int)
{
    val root: View = layoutInflater.inflate(resId, null, false)

    protected fun findViewById(@IdRes id: Int): View{
        return root.findViewById(id)
    }
}