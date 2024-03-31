package com.aatif.tchello.navigation

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.aatif.tchello.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.net.ConnectException

class CustomDialogBuilder {

    private val data = mutableListOf<Content>()

    fun addHeading(text: String) = this.apply {
            data.add(Content.Heading(text))
        }

    fun addLabel(text: String) = this.apply {
        data.add(Content.Label(text))
    }

    fun addEditText(hint: String, errorChecker: (String) -> String?) = this.apply {
        data.add(Content.EditText(hint, errorChecker))
    }

    fun addSingleActionButton(label: String, onClickListener: () -> Unit) = this.apply {
        data.add(Content.SingleActionButton(label, onClickListener))
    }

    fun addTwoActionButton(firstLabel: String, secondLabel: String, firstOnClickListener: ()->Unit, secondOnClickListener: () -> Unit) = this.apply{
        val f = Content.SingleActionButton(firstLabel, firstOnClickListener)
        val s = Content.SingleActionButton(secondLabel, secondOnClickListener)
        data.add(Content.MultiActionButton(f, s))
    }

    fun build(): Adapter<*> {
        return MyAdapter(data)
    }

    companion object {
        fun builder(): CustomDialogBuilder {
           return CustomDialogBuilder()
        }
    }

    private class MyAdapter(private val data: MutableList<Content>): Adapter<CustomDialogViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomDialogViewHolder {
            return LayoutInflater.from(parent.context).inflate(R.layout.dialog_recycler_item, parent, false).let {
                CustomDialogViewHolder(it)
            }
        }

        override fun getItemCount(): Int = data.size

        override fun onBindViewHolder(holder: CustomDialogViewHolder, position: Int) {
           return holder.bind(data[position])
        }

    }

    private class CustomDialogViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        private var textWatcher: TextWatcher? = null
        private val positiveButton by lazy { itemView.findViewById(R.id.positive_btn) as MaterialButton }
        private val negativeButton by lazy{ itemView.findViewById(R.id.negative_btn) as MaterialButton }
        private val labelView by lazy { itemView.findViewById(R.id.label) as TextView }
        private val textView by lazy{ (itemView.findViewById(R.id.heading) as TextView) }
        val editTextView by lazy{ itemView.findViewById(R.id.edit_text) as TextInputEditText }
        val til by lazy{ itemView.findViewById(R.id.til) as TextInputLayout }

        fun bind(content: Content) {
            val g = View.GONE
            positiveButton.visibility = g
            negativeButton.visibility = g
            labelView.visibility = g
            textView.visibility = g
            til.visibility = g
            when(content){
                is Content.Heading -> bindHeading(content)
                is Content.EditText -> bindEditText(content)
                is Content.Label -> bindLabel(content)
                is Content.MultiActionButton -> bindMultiActionButton(content)
                is Content.SingleActionButton -> bindSingleActionButton(content)
            }
        }

        private fun bindMultiActionButton(multiActionButton: Content.MultiActionButton) {
            bindActionButton(positiveButton, multiActionButton.positive)
            bindActionButton(negativeButton, multiActionButton.negative)
        }

        private fun bindSingleActionButton(actionButton: Content.SingleActionButton){
            bindActionButton(positiveButton, actionButton)
        }

        private fun bindActionButton(view: MaterialButton, actionButton: Content.SingleActionButton) {
            view.setText(actionButton.label)
            view.setOnClickListener { actionButton.onClickListener.invoke() }
            view.visibility = View.VISIBLE
        }

        private fun bindLabel(label: Content.Label) {
            labelView.setText(label.text)
            labelView.visibility = View.VISIBLE
        }

        private fun bindEditText(editText: Content.EditText) {
            til.visibility = View.VISIBLE
            val listener = object: TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

                override fun afterTextChanged(s: Editable?) {
                    til.error = editText.errorChecker.invoke(s?.toString().orEmpty())
                }

            }
            textWatcher?.let { editTextView.removeTextChangedListener(it) }
            textWatcher = listener
            editTextView.addTextChangedListener(listener)
            editTextView.hint = editText.text
        }

        private fun bindHeading(heading: Content.Heading) {
            textView.setText(heading.text)
            textView.visibility = View.VISIBLE
        }
    }

    sealed class Content {
        data class Heading(val text: String): Content()

        data class EditText(val text: String, val errorChecker: (String) -> String?): Content()

        data class Label(val text: String): Content()

        data class SingleActionButton(val label: String, val onClickListener: ()-> Unit ): Content()

        data class MultiActionButton(val positive: SingleActionButton, val negative: SingleActionButton): Content()
    }
}