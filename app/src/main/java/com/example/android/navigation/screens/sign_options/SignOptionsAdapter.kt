package com.example.android.navigation.screens.sign_options

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.navigation.database.Signs
import com.example.android.navigation.databinding.ListItemSignsBinding
import timber.log.Timber

// Adapter to control recyclerview
class SignOptionsAdapter (private val clickListener: SignListener): ListAdapter<Signs,
        SignOptionsAdapter.ViewHolder>(SignDiffCallback()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent)

    }

    // Calls the inner class to bind the new sign to the viewholder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

                val signItem = getItem(position)
                holder.bind(clickListener, signItem)

    }

    class ViewHolder private constructor(val binding: ListItemSignsBinding) : RecyclerView.ViewHolder(binding.root){

        // binds the sign to viewholder with adapter and adds a clickListener to it
        fun bind(clickListener: SignListener, item: Signs) {

            Timber.tag("SignOptionsAdapter").v("bind()")

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

// Callback for calculating the diff between two non-null items in a list
// Used by ListAdapter to calculate the minimum number of changes between and old list and a new
// list that's been passed to `submitList`.
class SignDiffCallback : DiffUtil.ItemCallback<Signs>(){

    override fun areItemsTheSame(oldItem: Signs, newItem: Signs): Boolean {

        return oldItem.signId == newItem.signId

    }

    override fun areContentsTheSame(oldItem: Signs, newItem: Signs): Boolean {

        return oldItem == newItem

    }

}

// ClickListener when sign is clicked it navigates to printing fragment
// This function is  overridden in the SignOptionsFragment
class SignListener(val clickListener: (signId: Long) -> Unit){

    fun onClick(sign: Signs) = clickListener(sign.signId)

}
