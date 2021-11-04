package com.example.android.navigation.screens.imports

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.navigation.database.Instructions
import com.example.android.navigation.databinding.ListItemStepsBinding

class StepAdapter(private val stepList : ArrayList<Instructions>) : RecyclerView.Adapter<StepAdapter.StepViewHolder>() {

    inner class StepViewHolder(private val binding : ListItemStepsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(step : Instructions){
            binding.steps = step
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder =
            StepViewHolder(ListItemStepsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = stepList.size

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.bind(stepList[position])
    }

}