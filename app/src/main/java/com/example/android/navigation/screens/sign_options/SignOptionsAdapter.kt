package com.example.android.navigation.screens.sign_options

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.android.navigation.R
import com.example.android.navigation.database.Signs



class SignOptionsAdapter: RecyclerView.Adapter<SignOptionsAdapter.ViewHolder>(){

    var data =  listOf<Signs>()
        set(value){
            field = value
            notifyDataSetChanged()
        }
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val res = holder.itemView.context.resources
        holder.signName.text = item.signName

        holder.image.setImageResource(item.sourcePicture)


        /*to reset or set properties to existing views
        if(item.signName == "red"){
            holder.textView.setColor(Color.RED)
        }
        else{
            holder.textView.setColor(Color.BLUE)
        }
         */
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
                .inflate(R.layout.list_item_signs, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val signName: TextView = itemView.findViewById(R.id.signNameListText)
        val image: ImageView = itemView.findViewById(R.id.imageViewList)
    }
}