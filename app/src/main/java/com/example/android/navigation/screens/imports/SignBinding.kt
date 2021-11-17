package com.example.android.navigation.screens.imports

import android.widget.EditText
import androidx.databinding.InverseBindingAdapter

@InverseBindingAdapter(attribute = "android:text")
fun EditText.getStringFromBinding(): String? {
    val result=text.toString()

    return result

}