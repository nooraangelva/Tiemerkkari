package com.example.android.navigation.screens.imports

import android.widget.EditText
import androidx.databinding.InverseBindingAdapter
// Recyclerview binders for SignImport

@InverseBindingAdapter(attribute = "android:text")
fun EditText.getStringFromBinding(): String {

    return text.toString()

}