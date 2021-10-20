package com.example.android.navigation.screens.sign_options

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class SignOptionsAdapter: RecyclerView.Adapter<TextItemViewHolder>(){

    //var data =  listOf<SleepNight>()
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.sleepQuality.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}