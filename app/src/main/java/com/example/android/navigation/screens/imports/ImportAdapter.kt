package com.example.android.navigation.screens.imports

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.navigation.R
import com.example.android.navigation.database.Instructions
import com.example.android.navigation.databinding.ListItemStepsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

// class ImportAdapter(private val dataSet: Array<String>) :
// RecyclerView.Adapter<ImportAdapter.ViewHolder>() {
//
//
//
// /**
// * Provide a reference to the type of views that you are using
// * (custom ViewHolder).
// */
// class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
// val textView: TextView
//
// init {
// // Define click listener for the ViewHolder's View.
// textView = view.findViewById(R.id.textView)
// }
// }
//
// // Create new views (invoked by the layout manager)
// override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
// // Create a new view, which defines the UI of the list item
// val view = LayoutInflater.from(viewGroup.context)
// .inflate(R.layout.list_item_steps, viewGroup, false)
//
// return ViewHolder(view)
// }
//
// // Replace the contents of a view (invoked by the layout manager)
// override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
//
// // Get element from your dataset at this position and replace the
// // contents of the view with that element
// viewHolder.textView.text = dataSet[position]
// }
//
// // Return the size of your dataset (invoked by the layout manager)
// override fun getItemCount() = dataSet.size
//
// }
//
//
//
// class ImportAdapter (private val dataSet: Array<String> ) :
// RecyclerView.Adapter<ImportAdapter.ViewHolder>(StepsDiffCallback()) {
//
// override fun onBindViewHolder(holder: ViewHolder, position: Int) {
// val item = getItem(position)
//
// holder.bind(item)
// }
//
// override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
// return ViewHolder.from(parent)
// }
//
// class ViewHolder private constructor(val binding: ListItemStepsBinding)
// : RecyclerView.ViewHolder(binding.root) {
//
// fun bind(item: Instructions) {
// binding.steps = item
// binding.executePendingBindings()
// }
//
// companion object {
// fun from(parent: ViewGroup): ViewHolder {
// val layoutInflater = LayoutInflater.from(parent.context)
// val binding = ListItemStepsBinding.inflate(layoutInflater, parent, false)
//
// return ViewHolder(binding)
// }
// }
// }
// }
//
// /**
// * Callback for calculating the diff between two non-null items in a list.
// *
// * Used by ListAdapter to calculate the minumum number of changes between and old list and a new
// * list that's been passed to `submitList`.
// */
// class StepsDiffCallback : DiffUtil.ItemCallback<Instructions>() {
// override fun areItemsTheSame(oldItem: Instructions, newItem: Instructions): Boolean {
// return oldItem.stepId == newItem.stepId
// }
//
// override fun areContentsTheSame(oldItem: Instructions, newItem: Instructions): Boolean {
// return oldItem == newItem
// }
// }