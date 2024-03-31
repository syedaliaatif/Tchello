package com.aatif.tchello.screens.homepage

import android.graphics.Bitmap
import android.graphics.ColorFilter
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.aatif.tchello.R
import com.aatif.tchello.common.firebase.User
import com.aatif.tchello.common.image.ImageLoader
import com.aatif.tchello.common.itemClicks
import com.aatif.tchello.common.navigationClicks
import com.aatif.tchello.screens.common.BaseMvc
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomePageMvc @Inject constructor(layoutInflater: LayoutInflater, private val imageLoader: ImageLoader): BaseMvc(layoutInflater, R.layout.activity_home_page) {

    val toolbar by lazy{findViewById(R.id.toolbar_home_page) as Toolbar }
    private val navigation by lazy { findViewById(R.id.navigation_view) as NavigationView }
    private val profilePicture by lazy { findViewById(R.id.nav_header_profile_photo_civ) as ImageView }
    private val profileName by lazy { findViewById(R.id.nav_header_profile_name_tv) as TextView}
    private val profileUsername by lazy { findViewById(R.id.nav_header_profile_username_tv) as TextView }
    /**
     * Returns flow for navigation clicks.
     */
    fun navigationClicks(): Flow<Unit> = toolbar.navigationClicks()

    fun menuClicks(): Flow<MenuItem> = navigation.itemClicks()

    fun setupUser(user: User){
        val wasSuccess = imageLoader.loadImageInto(user.image, profilePicture)
        if(wasSuccess == false){
            profilePicture.setImageDrawable(ContextCompat.getDrawable(root.context, R.drawable.menu_item_profile).apply {})
        }
        profileName.text = user.name
        profileUsername.text = user.email
    }

    /**
     * Opens drawer.
     */
    fun toggleDrawer(){
        val isOpen = (root as DrawerLayout).isDrawerOpen(DRAWER_GRAVITY)
        if(isOpen){
            root.closeDrawer(DRAWER_GRAVITY)
        } else {
            root.openDrawer(DRAWER_GRAVITY)
        }
    }

    fun setProfilePhoto(bitmap: Bitmap?) {
        profilePicture.setImageBitmap(bitmap)
    }

    companion object{
        private const val DRAWER_GRAVITY = GravityCompat.START
    }
}