package com.aatif.tchello.navigation

import android.app.Dialog
import android.app.ProgressDialog
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aatif.tchello.R
import javax.inject.Inject

/**
 * Manager class that handles showing dialogs.
 * @property activity Activity in whose scope this dialog manager lives.
 */
class DialogManager @Inject constructor(private val activity: AppCompatActivity) {

    private var progressBar: ProgressDialog? = null
    private var alertDialog: AlertDialog? = null
    fun showProgressBar(title: String, message: String) {
        progressBar = ProgressDialog.show(activity, title, message,true,false)
    }

    fun hideProgressBar(){
        progressBar?.cancel()
        progressBar = null
    }

    fun showDialog(builder: CustomDialogBuilder) {
        val recyclerView = RecyclerView(activity)
        recyclerView.adapter = builder.build()
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        AlertDialog.Builder(activity)
            .setView(recyclerView)
            .show().also {
                alertDialog = it
            }
    }

    fun hideDialog() {
        alertDialog?.hide()?.also {
            alertDialog = null
        }
    }

}