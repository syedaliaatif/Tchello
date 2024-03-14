package com.aatif.tchello.screens.homepage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.aatif.tchello.R
import com.aatif.tchello.common.firebase.FirebaseHandler
import com.aatif.tchello.screens.common.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomePageActivity : BaseActivity<HomePageMvc>() {

    @Inject lateinit var firebaseHandler: FirebaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        setupUI()
    }

    private fun setupUI(){
        setContentView(mvc.root)
        setSupportActionBar(mvc.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.home_action_bar_icon)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.app_name))
        supportActionBar?.setHomeButtonEnabled(true)
        setNavigationClicks()
        setButtonClicks()
        setupProfile()
    }

    private fun setNavigationClicks(){
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                mvc.navigationClicks().collect{ mvc.toggleDrawer() }
            }
        }
    }

    private fun setButtonClicks(){
        lifecycleScope.launch {
            mvc.menuClicks().flowOn(Dispatchers.Main).collect{ menuItem ->
                withContext(Dispatchers.Main){
                    when{
                        menuItem.itemId == R.id.action_profile -> {
                            screenNavigator.showShortToast("Profile clicked.")
                        }
                        menuItem.itemId == R.id.action_sign_out -> {
                            firebaseHandler.signOut()
                            screenNavigator.navigateToIntroPage(true)
                        }
                    }
                }
            }
        }
    }

    private fun setupProfile(){
        lifecycleScope.launch(Dispatchers.IO) {
            val user = firebaseHandler.getUserDetails()?:return@launch
            Log.d("AATIF_DBG", "userDetails : $user")
            withContext(Dispatchers.Main){
                mvc.setupUser(user)
            }
        }
    }

}