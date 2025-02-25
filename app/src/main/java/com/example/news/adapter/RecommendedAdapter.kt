package com.example.news.adapter

import News
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.news.R

class RecommendedAdapter(private val context: Context, private val newsList: Array<News>)
    :RecyclerView.Adapter<RecommendedAdapter.ViewHolder>(){

        inner class ViewHolder(itemView : View):RecyclerView.ViewHolder(itemView){
            val title : TextView = itemView.findViewById(R.id.title_recommended_card)
           // val description : TextView = itemView.findViewById(R.id.description_recommended_card)
            val image : ImageView = itemView.findViewById(R.id.image_recommended_card)
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendedAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recommended_card , parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendedAdapter.ViewHolder, position: Int) {
        val current = newsList[position]
        Log.d("RecommendedAdapter", "Binding item at position $position: ${current.title}")

        holder.title.text = current.title
       // holder.description.text = current.description


        Glide.with(context)
            .load(current.image)
            .placeholder(R.drawable.baseline_check_box_24) // Placeholder image while loading
            .error(R.drawable.baseline_check_box_outline_blank_24) // Error image if loading fails
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

}