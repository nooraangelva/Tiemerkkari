package com.example.android.navigation.screens.imports

import android.widget.CheckedTextView
import com.google.android.material.textfield.TextInputEditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.navigation.R
import com.example.android.navigation.database.Instructions

@BindingAdapter("stepNumberTextView")
fun TextView.setStepNumberTextView(item: Instructions?) {
    item?.let {
        text = item?.step.toString()
    }

}

@BindingAdapter("stepOrderTextView")
fun TextInputEditText.setStepOrderTextView(item: Instructions?) {
    item?.let {
        text = item.order
    }

}

@BindingAdapter("xMovementInput")
fun TextInputEditText.setXMovementInput(item: Instructions) {
    item?.let {
        text = item.parX.toString()
    }
}

@BindingAdapter("yMovementInput")
fun TextInputEditText.setYMovementInput(item: Instructions) {
    item?.let {
        text = item.parY.toString()
    }
}

@BindingAdapter("xDirectionChecked")
fun CheckedTextView.setXDirectionChecked(item: Instructions) {
        isChecked = item.directionX

}

@BindingAdapter("yDirectionChecked")
fun CheckedTextView.setYDirectionChecked(item: Instructions) {
    isChecked = item.directionY
}

@BindingAdapter("paintChecked")
fun CheckedTextView.setPaintChecked(item: Instructions) {
    isChecked = item.paint
}

