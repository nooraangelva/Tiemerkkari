package com.example.android.navigation.screens.sign_options

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.navigation.R
import com.example.android.navigation.database.Signs
import com.example.android.navigation.databinding.ListItemSignsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class SignOptionsAdapter ( val clickListener: SignListener): ListAdapter<DataItem, RecyclerView.ViewHolder>(SignDiffCallback()){

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<Signs>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.SignsItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val signItem = getItem(position) as DataItem.SignsItem
                holder.bind(clickListener, signItem.sign)
            }
        }
    }

    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.sign_options_header, parent, false)
                return TextViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SignsItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class ViewHolder private constructor(val binding: ListItemSignsBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: SignListener, item: Signs) {
            //TODO tsekkaa että path oikein kuvaan
            item.also { binding.sign = it }
            binding.sign?.sourcePicture = "sign_images/${item.sourcePicture}"

            //imagen sidonta
            val imgFile = File(binding.sign?.sourcePicture)
            if (imgFile.exists()) {
                var bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                binding.imageViewList.setImageBitmap(bitmap)
            }

            binding.clickListener = clickListener
            binding.executePendingBindings()

        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSignsBinding
                        .inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minumum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class SignDiffCallback : DiffUtil.ItemCallback<DataItem>(){
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}

class SignListener(val clickListener: (signId: Long) -> Unit){
    fun onClick(sign: Signs) = clickListener(sign.signId)

}

sealed class DataItem {
    data class SignsItem(val sign: Signs): DataItem() {
        override val id = sign.signId
    }

    object Header: DataItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long
}

