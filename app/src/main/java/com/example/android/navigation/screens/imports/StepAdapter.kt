package com.example.android.navigation.screens.imports

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.navigation.databinding.ListItemStepsBinding

import com.example.android.navigation.Step
import timber.log.Timber


class StepAdapter(private val stepList : ArrayList<Step>, private val interaction: Interaction?= null, private val interactionPaint: InteractionPaint? = null) : RecyclerView.Adapter<StepAdapter.StepViewHolder>() {

    inner class StepViewHolder(private val binding : ListItemStepsBinding, private val interaction: Interaction?, private val interactionPaint: InteractionPaint?) : RecyclerView.ViewHolder(binding.root) {
        fun bind(step : Step, position: Int){

            binding.step = step
            binding.stepNumberTextView.text = position.toString()


            Timber.i("Radiobutton Id v: " + binding.radioVertical)

            setRadios(step.order)

            if (interaction == null) {
                binding.radioVertical.setOnClickListener {
                    step.order = 1

                    //binding.step.order = 1
                }
                binding.radioHorizontal.setOnClickListener {
                    interaction?.onItemSelected(position, 2)
                }
                binding.radioDiagonal.setOnClickListener {
                    interaction?.onItemSelected(position, 3)
                }
                binding.radioArc.setOnClickListener {
                    interaction?.onItemSelected(position, 4)
                }
            }

            setRadiosPaint(step.paint)

            if (interactionPaint != null) {
                binding.radioNoPaint.setOnClickListener {
                    interactionPaint.onItemSelected(position, 5)
                }
                binding.radio5WidePaint.setOnClickListener {
                    interactionPaint.onItemSelected(position, 6)
                }
                binding.radio10WidePaint.setOnClickListener {
                    interactionPaint.onItemSelected(position, 7)
                }
                binding.radio50WidePaint.setOnClickListener {
                    interactionPaint.onItemSelected(position, 8)
                }
            }

        }
        fun setRadios(answer: Int) {
            //bug fix: clear RadioGroup selection before setting the values
            // otherwise checked answers sometimes disappear on scroll
            binding.radioGroupOrder.clearCheck()

            if (answer == -1) return //skip setting checked if no answer is selected

            when (answer) {
                1 -> binding.radioVertical.isChecked = true
                2 -> binding.radioHorizontal.isChecked = true
                3 -> binding.radioDiagonal.isChecked = true
                4 -> binding.radioArc.isChecked = true
            }
        }
        fun setRadiosPaint(answer: Int) {
            //bug fix: clear RadioGroup selection before setting the values
            // otherwise checked answers sometimes disappear on scroll
            binding.radioGroupPaint.clearCheck()

            if (answer == -1) return //skip setting checked if no answer is selected

            when (answer) {
                5 -> binding.radioNoPaint.isChecked = true
                6 -> binding.radio5WidePaint.isChecked = true
                7 -> binding.radio10WidePaint.isChecked = true
                8 -> binding.radio50WidePaint.isChecked = true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder =
            StepViewHolder(ListItemStepsBinding.inflate(LayoutInflater.from(parent.context), parent, false),interaction,interactionPaint)

    override fun getItemCount(): Int = stepList.size

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.bind(stepList[position], position)
    }

    interface Interaction {
        fun onItemSelected(position: Int, selection: Int)
    }
    interface InteractionPaint {
        fun onItemSelected(position: Int, selection: Int)
    }
    override public void onClick
}