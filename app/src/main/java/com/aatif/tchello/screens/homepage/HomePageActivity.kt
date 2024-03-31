package com.aatif.tchello.screens.homepage

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.aatif.tchello.R
import com.aatif.tchello.common.firebase.FirebaseHandler
import com.aatif.tchello.screens.common.BaseActivity
import com.aatif.tchello.screens.profile.ProfileActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomePageActivity : BaseActivity<HomePageMvc>() {

    @Inject lateinit var model: HomePageModel

    private val activityForResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        setupProfile()
    }

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
                            activityForResultLauncher.launch(Intent(this@HomePageActivity, ProfileActivity::class.java))
                            //screenNavigator.navigateToProfilePage(false)
                        }
                        menuItem.itemId == R.id.action_sign_out -> {
                            model.signOut()
                            screenNavigator.navigateToIntroPage(true)
                        }
                    }
                }
            }
        }
    }

    private fun setupProfile(){
        lifecycleScope.launch(Dispatchers.IO) {
            val user = model.getUserInformation()?:return@launch
            withContext(Dispatchers.Main){
                mvc.setupUser(user)
            }
            model.getProfilePhoto(user.image)
                .flowOn(Dispatchers.IO)
                .onEach {
                    when(it){
                        is FirebaseHandler.FirebaseAuthResult.Success -> {
                            mvc.setProfilePhoto(BitmapFactory.decodeByteArray(it.result,0, it.result.size))
                        }
                        is FirebaseHandler.FirebaseAuthResult.Failure -> {
                            Log.d("SetupProfile", "Error while getting profile photo from the backend.")
                        }
                    }

                }.flowOn(Dispatchers.Main)
                .launchIn(this)
        }
    }

}