package com.aatif.tchello.common.image

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class ImageLoader @Inject constructor(private val context: Context, private val glideRequestManager: RequestManager) {

    fun loadImageInto(url: String, view: ImageView): Boolean{
        if(url.isBlank() || url.isEmpty()){

            return false
        }
        return try {
            glideRequestManager.load(url).into(view)
            true
        }catch (error: Exception){
            false
        }
    }

    fun loadImageWithFallback(url: String, view: ImageView, @DrawableRes placeholder: Int): Boolean{
        if(url.isEmpty() || url.isBlank()){
            try{
                glideRequestManager.load(url).placeholder(placeholder).into(view)
            }catch (error: Exception){
               Log.d("GlideError", "Blank url sent to glide.")
            }
            return false
        }
        return try {
            glideRequestManager.load(url).placeholder(placeholder).into(view)
            true
        }catch (error: Exception){
            false
        }

    }
}