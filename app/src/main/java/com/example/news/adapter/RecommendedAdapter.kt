package com.example.news.adapter

import News
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.news.R

class RecommendedAdapter(private val context: Context,
                         private var newsList: List<News> )
    :RecyclerView.Adapter<RecommendedAdapter.ViewHolder>(){


        var onItemClick : ((News) -> Unit)? = null
        inner class ViewHolder(itemView : View):RecyclerView.ViewHolder(itemView){
            val title : TextView = itemView.findViewById(R.id.title_recommended_card)
            val image : ImageView = itemView.findViewById(R.id.image_recommended_card)
            val category : TextView = itemView.findViewById(R.id.category_recommended_card)
            val card : CardView = itemView.findViewById(R.id.card_recommended)
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

        holder.title.text = current.title
        holder.category.text = current.category
        current.image?.let {url ->
            Glide.with(context)
                .load(url)
                .placeholder(R.drawable.newspapers) // Placeholder image while loading
                .error(R.drawable.newspapers) // Error image if loading fails
                .into(holder.image)
        }?: holder.image.setImageResource(R.drawable.newspapers)


        holder.card.setOnClickListener{
            onItemClick?.invoke(current)
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateNews(newNews : List<News>){
        newsList = newNews
        notifyDataSetChanged()
    }

}