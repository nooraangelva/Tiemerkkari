package com.example.android.navigation.screens.sign_options

import android.graphics.BitmapFactory
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.ImageViewBindingAdapter.setImageUri
import com.example.android.navigation.R
import com.example.android.navigation.database.Signs
import java.io.File

@BindingAdapter("signImage")
fun ImageView.setSignImage(item: Signs) {
    val imgFile = File(item.sourcePicture)
    if (imgFile.exists()) {
        var bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
        setImageBitmap(bitmap)
    }
}

@BindingAdapter("signName")
fun TextView.setSignName(item: Signs) {
    text = item.signName
}