package com.example.android.navigation.screens.sign_options

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.adapters.ImageViewBindingAdapter
import androidx.databinding.adapters.ImageViewBindingAdapter.setImageUri
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
import timber.log.Timber
import java.io.File


class SignOptionsAdapter ( val clickListener: SignListener): ListAdapter<Signs,
        SignOptionsAdapter.ViewHolder>(SignDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

                val signItem = getItem(position)
                holder.bind(clickListener, signItem)

    }

    class ViewHolder private constructor(val binding: ListItemSignsBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: SignListener, item: Signs) {
            //TODO tsekkaa että path oikein kuvaan
            /*
            item.also { binding.sign = it }
            binding.sign?.sourcePicture = "sign_images/${item.sourcePicture}"

            //imagen sidonta
            val imgFile = File(binding.sign?.sourcePicture)
            if (imgFile.exists()) {
                var bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                binding.imageViewList.setImageBitmap(bitmap)
            }*/
            Timber.i("source : "+ item.sourcePicture)
            //binding.imageViewList.setImageURI(item.sourcePicture.toU)
            binding.sign = item
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
class SignDiffCallback : DiffUtil.ItemCallback<Signs>(){
    override fun areItemsTheSame(oldItem: Signs, newItem: Signs): Boolean {
        return oldItem.signId == newItem.signId
    }

    override fun areContentsTheSame(oldItem: Signs, newItem: Signs): Boolean {
        return oldItem == newItem
    }

}

class SignListener(val clickListener: (signId: Long) -> Unit){
    fun onClick(sign: Signs) = clickListener(sign.signId)

}
