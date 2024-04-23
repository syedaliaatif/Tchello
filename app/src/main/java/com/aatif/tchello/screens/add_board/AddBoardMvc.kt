package com.aatif.tchello.screens.add_board

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.aatif.tchello.R
import com.aatif.tchello.common.getClicks
import com.aatif.tchello.common.textChanges
import com.aatif.tchello.screens.common.BaseMvc
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddBoardMvc @Inject constructor(layoutInflater: LayoutInflater, private val context: Context) :BaseMvc(layoutInflater = layoutInflater, resId = R.layout.activity_add_board) {
    private val uploadPhotoButton by lazy { findViewById(R.id.cover_photo_update_button) as MaterialButton }
    private val coverPhotoCiv by lazy { findViewById(R.id.cover_photo_civ) as CircleImageView}
    private val boardNameTil by lazy { findViewById(R.id.board_name_til) as TextInputLayout }
    private val boardNameEditText by lazy {findViewById(R.id.board_name_edit_text) as TextInputEditText }
    val toolbar by lazy { findViewById(R.id.toolbar_add_board) as Toolbar}
    private val addBoardButton by lazy { findViewById(R.id.add_board_btn) as MaterialButton}

    fun getBoardNameTextChanges(): Flow<CharSequence?> = boardNameEditText.textChanges()
        .onEach { if(it?.isBlank() == true || it == null) boardNameTil.error = "Board name can't be empty." else boardNameTil.error = null  }

    fun getAddBoardButtonClicks(): Flow<Unit> = addBoardButton.getClicks()

    fun getUploadPhotoButtonClicks(): Flow<Unit> = uploadPhotoButton.getClicks()

    fun setCoverPhoto(bitmap: Bitmap) {
        coverPhotoCiv.setImageBitmap(bitmap)
    }

    suspend fun showDialog(scope: CoroutineScope): Flow<Int> {
        val flow = MutableSharedFlow<Int>()
        AlertDialog.Builder(context)
            .setItems(arrayOf("Select from Gallery", "Take a picture")){ _, id ->
                scope.launch {
                    flow.emit(id)
                }
            }.show()
        return flow
    }
}
