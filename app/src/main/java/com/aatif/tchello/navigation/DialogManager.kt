package com.aatif.tchello.navigation

import android.app.Dialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import com.aatif.tchello.R

/**
 * Manager class that handles showing dialogs.
 * @property activity Activity in whose scope this dialog manager lives.
 */
class DialogManager(private val activity: AppCompatActivity) {

    /**
     * Shows progress bar dialog.
     */
    fun showProgressDialog(): Dialog {
        val dialog = ProgressDialog(activity)
        dialog.setTitle(R.string.progress_bar_title)
        dialog.show()
        return dialog
    }

}