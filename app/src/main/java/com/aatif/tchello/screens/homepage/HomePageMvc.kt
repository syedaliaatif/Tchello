package com.aatif.tchello.screens.homepage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.widget.ContentLoadingProgressBar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aatif.tchello.R
import com.aatif.tchello.common.getClicks
import com.aatif.tchello.common.model.User
import com.aatif.tchello.common.image.ImageLoader
import com.aatif.tchello.common.itemClicks
import com.aatif.tchello.common.model.Board
import com.aatif.tchello.common.navigationClicks
import com.aatif.tchello.screens.adapters.BoardsAdapter
import com.aatif.tchello.screens.common.BaseMvc
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomePageMvc @Inject constructor(layoutInflater: LayoutInflater, private val imageLoader: ImageLoader, private val context: Context): BaseMvc(layoutInflater, R.layout.activity_home_page) {

    val toolbar by lazy{findViewById(R.id.toolbar_home_page) as Toolbar }
    private val navigation by lazy { findViewById(R.id.navigation_view) as NavigationView }
    private val profilePicture by lazy { findViewById(R.id.nav_header_profile_photo_civ) as ImageView }
    private val profileName by lazy { findViewById(R.id.nav_header_profile_name_tv) as TextView}
    private val profileUsername by lazy { findViewById(R.id.nav_header_profile_username_tv) as TextView }
    private val boardRecyclerView by lazy { findViewById(R.id.board_rv) as RecyclerView}
    private val boardsAdapter = BoardsAdapter(imageLoader)
    private val fab by lazy {findViewById(R.id.add_board_fab_button) as FloatingActionButton }
    private val viewFlipper by lazy { findViewById(R.id.home_page_view_flipper) as ViewFlipper }
    private val loader by lazy { findViewById(R.id.home_page_loader) as ContentLoadingProgressBar }
    init {
        onAttachedtoWindow {
            boardRecyclerView.adapter = boardsAdapter
        }
    }
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

    fun setBoards(boards: List<Board>?) {
        if(boards == null) return
        boardsAdapter.updateData(boards)
    }

    fun getFabClicks() : Flow<Unit> = fab.getClicks()

    fun showLoading() {
        viewFlipper.displayedChild = LOADER_ID
        loader.show()
    }

    fun showContent() {
        viewFlipper.displayedChild = CONTENT_ID
        loader.hide()
    }

    fun setFabIcon() {
        fab.setImageResource(R.drawable.ic_add)
    }

    companion object{
        private const val DRAWER_GRAVITY = GravityCompat.START
        private const val LOADER_ID = 1
        private const val CONTENT_ID = 0
    }
}