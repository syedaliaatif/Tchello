package com.aatif.tchello.common.image

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class ImageLoader @Inject constructor(private val glideRequestManager: RequestManager) {

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
}