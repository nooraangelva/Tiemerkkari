package com.example.android.navigation.screens.imports

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.navigation.databinding.ListItemStepsBinding

import com.example.android.navigation.dataForm.Step
import timber.log.Timber

// Adapter to control recyclerview
class StepAdapter(private val stepList : ArrayList<Step>, val clickListener: RadioButtonListener) : RecyclerView.Adapter<StepAdapter.StepViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder =
        StepViewHolder(ListItemStepsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = stepList.size

    // Calls the inner class to bind the new sign to the viewholder
    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {

        holder.bind(stepList[position],position,clickListener)

    }

    inner class StepViewHolder(private val binding : ListItemStepsBinding) : RecyclerView.ViewHolder(binding.root) {

        // binds the sign to viewholder with adapter and adds a clickListener to it
        fun bind(step : Step, position: Int, clickListener: RadioButtonListener){

            step.step = position
            binding.step = step
            binding.stepNumberTextView.text = position.toString()
            binding.clickListener = clickListener
            binding.executePendingBindings()

        }

    }

}

// ClickListener when radiobutton is clicked
// This function is  overridden in the SignOptionsFragment
class RadioButtonListener(val clickListener: (choice:Int,order:Int) -> Unit) {

    fun onClick(choice:Int, order:Int) = clickListener(choice,order)

}