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
    text = item?.step.toString()
}

@BindingAdapter("xMovementInput")
fun TextInputEditText.setXMovementInput(item: Instructions) {
    text = item.parX.toString()
}

@BindingAdapter("yMovementInput")
fun TextInputEditText.setSignName(item: Instructions) {
    text = item.parY.toString()
}

@BindingAdapter("xDirectionChecked")
fun CheckedTextView.setSignName(item: Instructions) {
    isChecked = item.directionX
}

@BindingAdapter("yDirectionChecked")
fun CheckedTextView.setSignName(item: Instructions) {
    isChecked = item.directionY
}

@BindingAdapter("paintChecked")
fun CheckedTextView.setSignName(item: Instructions) {
    isChecked = item.paint
}

