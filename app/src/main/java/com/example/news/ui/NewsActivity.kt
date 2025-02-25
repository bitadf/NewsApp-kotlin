package com.example.news.ui

import News
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.databinding.ActivityMainBinding
import com.example.news.databinding.ActivityNewsBinding
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class NewsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val news : News? = intent.getParcelableExtra("news")
        if(news == null){
            finish()
            return
        }
        // Populate UI with news details
        binding.newsTitle.text = news.title
        binding.newsDescription.text = if(news.description != null) news.description else "No Description"
        binding.newsCategory.text = news.category
//        binding.newsDate.text = news.date
        val convertedDate = ZonedDateTime.parse(news.date)
        val format = DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm:ss z")
        binding.newsDate.text= convertedDate.format(format)

        binding.authorName.text = if(news.author != null) news.author else "Author not found"
        binding.newsContent.text = if(news.content != null)news.content else "No Content"


        // Load image with Glide
        binding.newsImage.scaleType = ImageView.ScaleType.CENTER_CROP
        if (news.image != null) {
            Glide.with(this)
                .load(news.image)
                .placeholder(R.drawable.newspapers)
                .error(R.drawable.newspapers)
                .centerCrop()
                .into(binding.newsImage)

        }

        // Open source URL in browser
//        binding.sourceUrl.text = news.source
        binding.sourceUrl.setOnClickListener {
            val url = news.source
            Log.i("API" , url)
            if (url.startsWith("http")) {  // Basic URL validation
                try {
                    val uri = Uri.parse(url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, "No browser found to open the URL", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {

        startActivity(Intent(this,MainActivity::class.java))
        super.onBackPressed()
    }




}