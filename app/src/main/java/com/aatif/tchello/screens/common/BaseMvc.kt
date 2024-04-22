package com.aatif.tchello.screens.common

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnAttachStateChangeListener
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

open class BaseMvc(protected val layoutInflater: LayoutInflater, @LayoutRes resId: Int)
{
    val root: View = layoutInflater.inflate(resId, null, false)

    protected fun findViewById(@IdRes id: Int): View{
        return root.findViewById(id)
    }

    protected fun onAttachedtoWindow(perform: ()-> Unit) {
        if(root.isAttachedToWindow) {
            perform.invoke()
        } else {
            val listener = object : OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {
                    perform.invoke()
                    root.removeOnAttachStateChangeListener(this)
                }

                override fun onViewDetachedFromWindow(v: View) {
                    root.removeOnAttachStateChangeListener(this)
                }

            }
            root.addOnAttachStateChangeListener(listener)
        }
    }
}