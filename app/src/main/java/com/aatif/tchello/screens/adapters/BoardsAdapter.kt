package com.aatif.tchello.screens.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aatif.tchello.R
import com.aatif.tchello.common.image.ImageLoader
import com.aatif.tchello.common.model.Board

class BoardsAdapter(private val imageLoader: ImageLoader): RecyclerView.Adapter<BoardsAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val imageLoader: ImageLoader): RecyclerView.ViewHolder(view){
        private val iv: ImageView by lazy { view.findViewById(R.id.board_list_item_view_iv) }
        private val title: TextView by lazy { view.findViewById(R.id.board_list_item_view_tv) }

        fun bind(board: Board) {
            if(board.coverPhoto.isNotBlank()) {
                imageLoader.loadImageWithFallback(board.coverPhoto, iv, R.drawable.board_list_fallback_cover)
            } else {
                iv.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.board_list_fallback_cover))
            }
            title.text = board.name
        }
    }

    var data: List<Board> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return LayoutInflater.from(parent.context).inflate(R.layout.board_list_item_view, parent, false).let {
            ViewHolder(it, imageLoader)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun updateData(data: List<Board>) {
        this.data = data
        notifyDataSetChanged()
    }
}