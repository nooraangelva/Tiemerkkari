package com.example.android.navigation.screens.imports

import android.R
import android.app.PendingIntent.getActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.navigation.database.Instructions
import com.example.android.navigation.databinding.ListItemStepsBinding
import android.widget.ArrayAdapter

import android.widget.Spinner
import kotlin.coroutines.EmptyCoroutineContext.plus


class StepAdapter(private val stepList : ArrayList<Instructions>) : RecyclerView.Adapter<StepAdapter.StepViewHolder>() {

    inner class StepViewHolder(private val binding : ListItemStepsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(step : Instructions, position: Int){
            //binding.steps = step
            binding.paintChecked.isChecked = step.paint
            binding.radioArc.isChecked = false
            binding.radioDiagonal.isChecked = false
            //binding.radioHorizontal
            //binding.radioVertical
            binding.stepNumberTextView.text = position.toString()
            //binding.xDirectionChecked
            //binding.xMovementInput
            //binding.yDirectionChecked
            //binding.yMovementInput



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder =
            StepViewHolder(ListItemStepsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = stepList.size

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.bind(stepList[position], position)
    }


}