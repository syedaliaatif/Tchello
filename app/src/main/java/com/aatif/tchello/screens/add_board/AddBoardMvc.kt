package com.aatif.tchello.screens.add_board

import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import com.aatif.tchello.R
import com.aatif.tchello.common.getClicks
import com.aatif.tchello.common.textChanges
import com.aatif.tchello.screens.common.BaseMvc
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class AddBoardMvc @Inject constructor(layoutInflater: LayoutInflater) :BaseMvc(layoutInflater = layoutInflater, resId = R.layout.activity_add_board){
    private val boardNameTil by lazy { findViewById(R.id.board_name_til) as TextInputLayout }
    private val boardNameEditText by lazy {findViewById(R.id.board_name_edit_text) as TextInputEditText }
    val toolbar by lazy { findViewById(R.id.toolbar_add_board) as Toolbar}
    private val addBoardButton by lazy { findViewById(R.id.add_board_btn) as MaterialButton}

    fun getBoardNameTextChanges(): Flow<CharSequence?> = boardNameEditText.textChanges()
        .onEach { if(it?.isBlank() == true || it == null) boardNameTil.error = "Board name can't be empty." else boardNameTil.error = null  }

    fun getAddBoardButtonClicks(): Flow<Unit> = addBoardButton.getClicks()
}
