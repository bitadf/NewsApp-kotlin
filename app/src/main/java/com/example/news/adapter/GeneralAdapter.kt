package com.example.news.adapter

import News
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.news.R
import com.example.news.data.NewsResponse
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class GeneralAdapter(
    private val context: Context ,
     var bookmarkIds : Set<String> = emptySet(),
    val onBookmarkClick: (News, Boolean) -> Unit
) : RecyclerView.Adapter<GeneralAdapter.ViewHolder>() {
    private val newsList = mutableListOf<News>()
    private val bookmarkedItems = mutableSetOf<String>()

    //click listener interface
    var onItemClick: ((News) -> Unit)? = null
//    var onBookmarkClick: (News, Boolean) -> Unit

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById<TextView>(R.id.news_card_title)
        var description: TextView = view.findViewById<TextView>(R.id.news_card_description)
        var category: TextView = view.findViewById<TextView>(R.id.news_card_category)
        var date: TextView = view.findViewById<TextView>(R.id.news_card_date)
        var image: ImageView = view.findViewById<ImageView>(R.id.news_card_image)
        var bookmarkIcon: ImageView = view.findViewById(R.id.bookmark)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<News>) {
        newsList.clear()
        newsList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = newsList[position]
        holder.title.text = item.title
        //holder.date.text = item.date
        val convertedDate = ZonedDateTime.parse(item.date)
        val format = DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm:ss z")
        holder.date.text = convertedDate.format(format)


        holder.description.text = item.description
        holder.category.text = item.category

        //place image
        item.image?.let { url ->
            Glide.with(context)
                .load(url)
                .placeholder(R.drawable.newspapers)
                .error(R.drawable.newspapers)
                .centerCrop()
                .into(holder.image)
        } ?: holder.image.setImageResource(R.drawable.newspapers)




        // Get unique key for bookmark tracking
        val uniqueKey = getUniqueKey(item)
        val isBookmarked = bookmarkIds.contains(uniqueKey)

        // Set correct icon
        updateBookmarkIcon(holder, isBookmarked)

        // Handle bookmark click
        holder.bookmarkIcon.setOnClickListener {
            val newBookmarkState = !isBookmarked

            // Update the ViewModel immediately
            onBookmarkClick(item, newBookmarkState)
        }


        holder.itemView.setOnClickListener{
            onItemClick?.invoke(item)

        }
    }
    private fun updateBookmarkIcon(holder:ViewHolder , isBookmarked : Boolean){
        holder.bookmarkIcon.setImageResource(
            if(isBookmarked)R.drawable.baseline_turned_in_24
            else R.drawable.baseline_turned_in_not_24
        )
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateBookmarks(newBookmarkIds : Set<String> ){
        bookmarkIds = newBookmarkIds
        notifyDataSetChanged()
    }
    private fun getUniqueKey(news:News):String{
        return news.title.hashCode().toString()
    }

}