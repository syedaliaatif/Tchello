package com.aatif.tchello.screens.add_board

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.aatif.tchello.R
import com.aatif.tchello.common.navigationClicks
import com.aatif.tchello.screens.add_board.AddBoardMvc
import com.aatif.tchello.screens.common.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class AddBoardActivity : BaseActivity<AddBoardMvc>() {

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
    }

    companion object {
        private const val EMPTY_NAME_ERROR_TEXT = "Board can not have empty name"
    }
}