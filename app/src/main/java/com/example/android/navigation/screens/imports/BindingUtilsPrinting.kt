package com.example.android.navigation.screens.imports

import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.navigation.database.Instructions

@BindingAdapter("step")
fun TextView.setStep(item: Instructions?) {
    text = item?.step.toString()
}

@BindingAdapter("order")
fun TextView.setOrder(item: Instructions?) {

    text = item?.order

}

@BindingAdapter("parameterX")
fun TextView.setParameterX(item: Instructions?) {

    text = item?.parX.toString()

}

@BindingAdapter("parameterY")
fun TextView.setParameterY(item: Instructions?) {

    text = item?.parY.toString()

}

@BindingAdapter("paint")
fun CheckBox.setPaint(item: Instructions) {

    //isChecked = item.paint

}
