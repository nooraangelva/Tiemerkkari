package com.example.android.navigation.screens.sign_options

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.navigation.R
import com.example.android.navigation.database.Signs

@BindingAdapter("signImage")
fun ImageView.setSignImage(item: Signs) {
    setImageResource(item.sourcePicture)
}

@BindingAdapter("signName")
fun TextView.setSignName(item: Signs) {
    setText( item.signName)
}