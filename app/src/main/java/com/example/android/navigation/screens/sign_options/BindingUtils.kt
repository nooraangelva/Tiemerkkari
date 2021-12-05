package com.example.android.navigation.screens.sign_options

import android.graphics.BitmapFactory
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.navigation.database.Signs
import java.io.File

// Recyclerview binders for signOptions' list of signs

// Binds image using bitmap
@BindingAdapter("signImage")
fun ImageView.setSignImage(item: Signs) {

    val imgFile = File(item.sourcePicture)

    if (imgFile.exists()) {

        val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
        setImageBitmap(bitmap)

    }
}

// Binds signs name
@BindingAdapter("signName")
fun TextView.setSignName(item: Signs) {

    text = item.signName

}