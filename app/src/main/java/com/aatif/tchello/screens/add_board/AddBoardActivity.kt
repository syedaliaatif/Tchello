package com.aatif.tchello.screens.add_board

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.aatif.tchello.R
import com.aatif.tchello.common.navigationClicks
import com.aatif.tchello.screens.common.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddBoardActivity : BaseActivity<AddBoardMvc>() {

    private val galleryPicker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if (it == null) {
            return@registerForActivityResult
        }
       val stream = contentResolver.openInputStream(it)
        lifecycleScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                val bitmap = BitmapFactory.decodeStream(stream)
                withContext(Dispatchers.Main) {
                    mvc.setCoverPhoto(bitmap = bitmap)
                }
            }
        }
    }

    private val takePicturePreview = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        if (it == null) {
            return@registerForActivityResult
        }
        mvc.setCoverPhoto(bitmap = it)
    }

    @Inject lateinit var model : AddBoardModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        setupUI()
    }

    private fun setupUI() {
        setContentView(mvc.root)
        setupToolbar()
        collectEvents()
    }

    private fun setupToolbar() {
        setSupportActionBar(mvc.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mvc.toolbar.navigationClicks().onEach { onBackPressed() }.flowOn(Dispatchers.Main).launchIn(lifecycleScope)
        supportActionBar?.title = getString(R.string.add_board_toolbar_title)
    }

    private fun collectEvents() {
        mvc.getBoardNameTextChanges()
            .onEach { model.updateName(it?.toString().orEmpty()) }
            .flowOn(Dispatchers.Main)
            .launchIn(lifecycleScope)

        mvc.getAddBoardButtonClicks()
            .onEach {
                if(model.isValid().not()){
                    screenNavigator.showShortToast(EMPTY_NAME_ERROR_TEXT)
                } else {
                    model.addBoard().onEach {
                        android.util.Log.d("AATIF_DBG", "Added board succesfully.")
                        setResult(RESULT_OK)
                        finish()
                    }.launchIn(lifecycleScope)
                }
            }
            .flowOn(Dispatchers.Main)
            .launchIn(lifecycleScope)

        mvc.getUploadPhotoButtonClicks()
            .flatMapMerge {
                mvc.showDialog(lifecycleScope)
            }.onEach {
                android.util.Log.d("AATIF_DBG", "Selected $it index value")
                when {
                    it == 0 -> pickFromGallery()
                    it == 1 -> takeAPicture()
                }
            }
            .flowOn(Dispatchers.Main)
            .launchIn(lifecycleScope)
    }

    private fun pickFromGallery() {
        galleryPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun takeAPicture() {
        takePicturePreview.launch(null)
    }

    companion object {
        private const val EMPTY_NAME_ERROR_TEXT = "Board can not have empty name"
    }
}