package com.example.news.news_fragments

import News
import android.content.Intent
import com.example.news.ui.HorizontalItemDecoration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.newStringBuilder
import com.example.news.adapter.GeneralAdapter
import com.example.news.adapter.RecommendedAdapter
import com.example.news.viewModel.CacheNewsViewModel
import com.example.news.viewModel.NewsViewModelFactory
import com.example.news.cache.data.BookmarkedNews
import com.example.news.cache.data.CachedNews

import com.example.news.databinding.FragmentHomeBinding
import com.example.news.ui.NewsActivity
import com.example.news.viewModel.NewsViewModel

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private val viewModel : NewsViewModel by activityViewModels()
    private lateinit var generalAdapter: GeneralAdapter
   // private var recommendedNews = mutableListOf<News>()
    private lateinit var recommendedAdapter: RecommendedAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //cacheView model
        val cacheLimit = 10
        val cacheViewModel = ViewModelProvider(this , NewsViewModelFactory(requireActivity().application , cacheLimit))
            .get(CacheNewsViewModel::class.java)

        val recommendedRecycler : RecyclerView = binding.recommendedRecyclerView
        val itemsToShow = 2.5f
        recommendedRecycler.layoutManager = LinearLayoutManager(
            requireContext() ,
            LinearLayoutManager.HORIZONTAL ,
            false
        )
        recommendedAdapter = RecommendedAdapter(requireContext() , emptyList())
        recommendedRecycler.adapter = recommendedAdapter
        recommendedRecycler.addItemDecoration(HorizontalItemDecoration(itemsToShow))
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recommendedRecycler)


        viewModel.recommendedArticles.observe(viewLifecycleOwner){
            articles ->
            val newsItems = articles?.map { article ->
                News(
                    image = article.urlToImage ,
                    title = article.title ,
                    description = article.description ,
                    date = article.publishedAt ,
                    source = article.url ,
                    author = article.author ,
                    category = "General",
                    content = article.content

                )
            }
            newsItems?.let {
                recommendedAdapter.updateNews(it)
            }
        }
        viewModel.fetchRecommendedNews()
        recommendedAdapter.onItemClick = {news ->
            val cachedNews = CachedNews(
                id = news.title.hashCode().toString() ,
                title = news.title ,
                description = news.description,
                publishedAt = news.date ,
                author = news.author ,
                content = news.content ,
                category = "General" ,
                source = news.source,
                image = news.image ,
                timestamp = System.currentTimeMillis()

            )
            cacheViewModel.cacheNews(cachedNews)
            val intent = Intent(requireContext() , NewsActivity::class.java).apply {
                putExtra("news" , news)
            }
            startActivity(intent)
        }

        //////////////////////////////////////

        //set up adapter
        generalAdapter = GeneralAdapter( requireContext() , cacheViewModel.bookmarkedIds.value ?:emptySet()){
                news , shouldBookmark ->
            val bookmark = BookmarkedNews(
                id = news.title.hashCode().toString() ,
                title = news.title ,
                description = news.description ,
                publishedAt = news.date ,
                author = news.author,
                category = news.category,
                content = news.content ,
                image = news.image ,
                source = news.source,
                timestamp = System.currentTimeMillis()
            )
//            val updatedBookmarks = generalAdapter.bookmarkIds.toMutableSet()
            if(shouldBookmark){
                //updatedBookmarks.add(bookmark.id)
                Toast.makeText(requireContext(), "Bookmark Was Added" , Toast.LENGTH_SHORT).show()
                cacheViewModel.bookmark(bookmark)
            }
            else{
                //updatedBookmarks.remove(bookmark.id)
                Toast.makeText(requireContext(), "Bookmark Was Deleted" , Toast.LENGTH_SHORT).show()
                cacheViewModel.deleteBookmark(bookmark)
            }
//            generalAdapter.updateBookmarks(updatedBookmarks)

        }
        cacheViewModel.bookmarkedIds.observe(viewLifecycleOwner){ids ->
            generalAdapter.updateBookmarks(ids )
        }
        val recyclerView : RecyclerView = binding.homeRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = generalAdapter
        recyclerView.isNestedScrollingEnabled = false

        //view model
//        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        //observe data
        viewModel.newArticles.observe(viewLifecycleOwner){
            articles ->
            val newsItems = articles?.map { article ->
                News(image = article.urlToImage ,
                    title = article.title ,
                    description = article.description ,
                    date = article.publishedAt ,
                    source = article.url,
                    author = article.author,
                    category = "General" ,
                    content = article.content
                )
            }
            if (newsItems != null) {
                generalAdapter.submitList(newsItems)
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner){error ->
            Toast.makeText(requireContext() , error , Toast.LENGTH_SHORT).show()
            Log.i("API" , error)

        }
        viewModel.getBreakingNews("us")


        generalAdapter.onItemClick = {news ->

            //add to cache
            val cachedNews = CachedNews(
                id = news.title.hashCode().toString(),
                title = news.title ,
                content = news.content ,
                publishedAt = news.date ,
                source = news.source ,
                description = news.description ,
                image = news.image ,
                category = "General",
                author = news.author ,
                timestamp = System.currentTimeMillis()
            )

            cacheViewModel.cacheNews(cachedNews)
            val intent = Intent(requireContext() , NewsActivity::class.java).apply {
                putExtra("news" , news)
            }
            startActivity(intent)
        }
    }


}