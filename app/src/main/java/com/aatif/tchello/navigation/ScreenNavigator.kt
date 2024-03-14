package com.aatif.tchello.navigation

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aatif.tchello.screens.homepage.HomePageActivity
import com.aatif.tchello.screens.intro.IntroActivity
import com.aatif.tchello.screens.signin.SignInActivity
import com.aatif.tchello.screens.signup.SignUpActivity
import javax.inject.Inject

class ScreenNavigator @Inject constructor(
    private val activity: AppCompatActivity
) {
    private var progressBar : Dialog?=null
    fun navigateToSignInPage(clearTask: Boolean = false){
        val flag = getFlags(clearTask)
        activity.startActivity(Intent(activity, SignInActivity::class.java).apply { this.flags = flag })
    }

    fun navigateToSignUpPage(clearTask: Boolean = false){
        val flag = getFlags(clearTask)
        activity.startActivity(Intent(activity, SignUpActivity::class.java).apply { this.flags = flag })
    }

    fun navigateToIntroPage(clearTask: Boolean = false){
        val flag = getFlags(clearTask)
        activity.startActivity(Intent(activity, IntroActivity::class.java).apply { this.flags = flag })
    }

    fun navigateToHomePage(clearTask: Boolean = false){
        val flag = getFlags(clearTask)
        activity.startActivity(Intent(activity, HomePageActivity::class.java).apply {
            this.flags = flag 
        })
    }
    
    private fun getFlags(clearTask: Boolean): Int{
        return if(clearTask)(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) else 0
    }

    fun showLongToast(message: String){
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    fun showShortToast(message: String){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    fun showProgressBar(title: String, message: String) {
        progressBar = ProgressDialog.show(activity, title, message,true,false)
    }

    fun hideProgressBar(){
        progressBar?.cancel()
        progressBar = null
    }
}