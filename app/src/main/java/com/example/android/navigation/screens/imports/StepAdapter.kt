package com.example.android.navigation.screens.imports

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.navigation.databinding.ListItemStepsBinding

import com.example.android.navigation.Step
import timber.log.Timber


class StepAdapter(private val stepList : ArrayList<Step>, val clickListener: RadioButtonListener) : RecyclerView.Adapter<StepAdapter.StepViewHolder>() {

    inner class StepViewHolder(private val binding : ListItemStepsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(step : Step, position: Int,clickListener: RadioButtonListener){

            binding.step = step
            binding.stepNumberTextView.text = position.toString()
            binding.clickListener = clickListener
            binding.executePendingBindings()

            Timber.i("Radiobutton Id v: " + binding.radioVertical)


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder =
            StepViewHolder(ListItemStepsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = stepList.size

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        //holder.bind(stepList[position], position)
        holder.bind(stepList[position],position,clickListener)
    }



}
class RadioButtonListener(val clickListener: (choice:Int,order:Int) -> Unit) {
    fun onClick(choice:Int, order:Int) = clickListener(choice,order)
}