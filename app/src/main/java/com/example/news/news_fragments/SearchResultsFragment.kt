package com.example.news.news_fragments

import News
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.adapter.GeneralAdapter
import com.example.news.viewModel.CacheNewsViewModel
import com.example.news.viewModel.NewsViewModelFactory
import com.example.news.cache.data.BookmarkedNews
import com.example.news.cache.data.CachedNews
import com.example.news.databinding.FragmentSearchResultsBinding
import com.example.news.ui.NewsActivity
import com.example.news.viewModel.NewsViewModel

import java.util.Calendar
import java.util.Locale

class SearchResultsFragment : Fragment() {
    private lateinit var binding : FragmentSearchResultsBinding
    private lateinit var viewModel : NewsViewModel
    private lateinit var generalAdapter : GeneralAdapter
    private var query : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        query = arguments?.getString("query")
        binding = FragmentSearchResultsBinding.inflate(layoutInflater , container , false)
        return binding.root
    }

    companion object{
        private const val ARG_QUERY = "query"
        fun newInstance(query: String):SearchResultsFragment {
            val fragment = SearchResultsFragment()
            val args = Bundle()
            args.putString("query" , query)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emptyText = binding.emptySearchTextView
        //view model
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        val cacheViewModel = ViewModelProvider(
            this ,
            NewsViewModelFactory(requireActivity().application , 10)
        ).get(CacheNewsViewModel::class.java)
        ///////////////////////
        //general adapter
        generalAdapter = GeneralAdapter(requireContext() , cacheViewModel.bookmarkedIds.value?: emptySet()){
            news , shouldBookmark ->
            val bookmark = BookmarkedNews(
                id = news.title.hashCode().toString() ,
                title = news.title ,
                description = news.description ,
                publishedAt =  news.date ,
                author = news.author ,
                category = news.category ,
                content = news.content ,
                image = news.image ,
                source = news.source ,
                timestamp = System.currentTimeMillis()
            )
            if(shouldBookmark){
                cacheViewModel.bookmark(bookmark)
                Toast.makeText(requireContext(),"Bookmark Was Added" , Toast.LENGTH_SHORT).show()
            }
            else{
                cacheViewModel.deleteBookmark(bookmark)
                Toast.makeText(requireContext() , "Bookmark Was Deleted" , Toast.LENGTH_SHORT).show()
            }
            cacheViewModel.bookmarkedIds.observe(viewLifecycleOwner){ids ->
                generalAdapter.updateBookmarks(ids)
            }

        }
        ////////////////////////////////////
        //click on card
        generalAdapter.onItemClick = {
                news ->
            val cacheNews = CachedNews(
                id = news.title.hashCode().toString(),
                title = news.title ,
                content = news.content ,
                publishedAt = news.date ,
                source = news.source ,
                description = news.description,
                image = news.image ,
                category = "General",
                author = news.author ,
                timestamp = System.currentTimeMillis()
            )
            cacheViewModel.cacheNews(cacheNews)
            val intent = Intent(requireContext() , NewsActivity::class.java).apply {
                putExtra("news" , news)
            }
            startActivity(intent)
        }
        viewModel.getNews(query!!, getTodayDate())
        //viewModel.getByDomainNews(query!!)


            val recyclerView : RecyclerView = binding.searchRecyclerView
            generalAdapter.submitList(emptyList())
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = generalAdapter
            }
        viewModel.newArticles.observe(viewLifecycleOwner){
            articles->
            val newsItem = articles?.map {
                article ->
                News(
                    image = article.urlToImage ,
                    title = article.title ,
                    description = article.description ,
                    date = article.publishedAt ,
                    source = article.url ,
                    author = article.author ,
                    category = "General" ,
                    content = article.content
                )

            }
            if(!newsItem.isNullOrEmpty()){
                emptyText.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                generalAdapter.submitList(newsItem)
            }
            else{
                recyclerView.visibility = View.GONE
                emptyText.visibility = View.VISIBLE
                generalAdapter.submitList(emptyList())
            }

        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            Log.i("API", error)
        }

    }

    private fun getTodayDate():String{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH , - 1)
        return dateFormat.format(calendar.time)
    }


}