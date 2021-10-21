package com.example.android.navigation.screens.sign_options

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.navigation.R
import com.example.android.navigation.database.Signs

abstract class SignOptionsAdapter: RecyclerView.Adapter<TextItemViewHolder>(){

    var data =  listOf<Signs>()
        set(value){
            field = value
            notifyDataSetChanged()
        }
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]
        /*to reset or set properties to existing views
        if(item.signName == "red"){
            holder.textView.setColor(Color.RED)
        }
        else{
            holder.textView.setColor(Color.BLUE)
        }
         */
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
                .inflate(R.layout.text_item_view, parent, false) as TextView
        return TextItemViewHolder(view)
    }
}