package com.example.android.navigation.screens.sign_options

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.navigation.database.Signs
import com.example.android.navigation.databinding.ListItemSignsBinding
import com.example.android.navigation.generated.callback.OnClickListener


class SignOptionsAdapter ( val clickListener: SignListener): ListAdapter<Signs, SignOptionsAdapter.ViewHolder>(SignDiffCallback()){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemSignsBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Signs, clickListener: SignListener) {

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