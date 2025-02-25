package com.example.news.news_fragments

import News
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.adapter.GeneralAdapter
import com.example.news.viewModel.CacheNewsViewModel
import com.example.news.viewModel.NewsViewModelFactory
import com.example.news.cache.data.BookmarkedNews
import com.example.news.cache.data.CachedNews
import com.example.news.databinding.FragmentCategoryBinding
import com.example.news.ui.NewsActivity
import com.example.news.viewModel.NewsViewModel


class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private var categoryName: String? = null


    private lateinit var viewModel: NewsViewModel
    private lateinit var generalAdapter: GeneralAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categoryName = arguments?.getString("category_name")
        binding = FragmentCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(categoryName: String): CategoryFragment {
            val fragment = CategoryFragment()
            val args = Bundle()
            args.putString("category_name", categoryName)
            fragment.arguments = args
            return fragment
        }
    }

    @SuppressLint("CommitTransaction")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emptyText = binding.emptyTextView



        //view model
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        val cacheLimit = 10
        val cacheViewModel = ViewModelProvider(
            this,
            NewsViewModelFactory(requireActivity().application, cacheLimit)
        ).get(CacheNewsViewModel::class.java)
        ///////////////////////////////////////////////////////////////////

        //set up adapter
        generalAdapter = GeneralAdapter(requireContext() , cacheViewModel.bookmarkedIds.value?:emptySet()){
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
            if(shouldBookmark){
                cacheViewModel.bookmark(bookmark)
                Toast.makeText(requireContext(), "Bookmark Was Added" , Toast.LENGTH_SHORT).show()
            }
            else{
                cacheViewModel.deleteBookmark(bookmark)
                Toast.makeText(requireContext(), "Bookmark Was Deleted" , Toast.LENGTH_SHORT).show()
            }

        }
        cacheViewModel.bookmarkedIds.observe(viewLifecycleOwner){ids ->
            generalAdapter.updateBookmarks(ids)
        }

        val recyclerView: RecyclerView = binding.categoryRecyclerView
        generalAdapter.submitList(emptyList())
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = generalAdapter
        }

        //set click listener
        generalAdapter.onItemClick = { news ->
            //add to cache
            val cachedNews = CachedNews(
                id = news.title.hashCode().toString(),
                title = news.title,
                content = news.content,
                publishedAt = news.date,
                source = news.source,
                description = news.description,
                image = news.image,
                category = categoryName.toString(),
                author = news.author,
                timestamp = System.currentTimeMillis()
            )
            cacheViewModel.cacheNews(cachedNews)


            val intent = Intent(requireContext(), NewsActivity::class.java).apply {
                putExtra("news", news)
            }
            startActivity(intent)
        }


        //observe
        if (categoryName.equals("recent", ignoreCase = true) ) {
            cacheViewModel.cachedNews.observe(viewLifecycleOwner) { cache ->

                    val newItem = cache?.map { item ->
                        News(
                            image = item.image,
                            title = item.title,
                            description = item.description,
                            content = item.content,
                            category = item.category,
                            source = item.source,
                            author = item.author,
                            date = item.publishedAt
                        )
                    }
                    if (!newItem.isNullOrEmpty()) {
                        recyclerView.visibility = View.VISIBLE
                        emptyText.visibility = View.GONE
                        generalAdapter.submitList(newItem)
                    }
                    else{
                        recyclerView.visibility = View.GONE
                        emptyText.visibility = View.VISIBLE
                        generalAdapter.submitList(emptyList())
                    }


            }

        } else if (categoryName.equals("bookmarks" ,ignoreCase = true)){
            cacheViewModel.bookmarkNews.observe(viewLifecycleOwner){
                bookmarks ->

                    val newItem = bookmarks?.map { bookmark ->
                        News(
                        image = bookmark.image,
                        title = bookmark.title,
                        description = bookmark.description,
                        content = bookmark.content,
                        category = bookmark.category,
                        source = bookmark.source,
                        author = bookmark.author,
                        date = bookmark.publishedAt
                        )
                    }
                if (!newItem.isNullOrEmpty()) {
                    recyclerView.visibility = View.VISIBLE
                    emptyText.visibility = View.GONE
                    generalAdapter.submitList(newItem)
                }
                else{
                    recyclerView.visibility = View.GONE
                    emptyText.visibility = View.VISIBLE
                    generalAdapter.submitList(emptyList())
                }


            }

        }
        else {
            viewModel.newArticles.observe(viewLifecycleOwner) { articles ->
                val newsItem = articles?.map { article ->
                    News(
                        image = article.urlToImage,
                        title = article.title,
                        description = article.description,
                        date = article.publishedAt,
                        source = article.url,
                        author = article.author,
                        category = categoryName.toString(),
                        content = article.content
                    )
                }
                if (!newsItem.isNullOrEmpty()) {
                    recyclerView.visibility = View.VISIBLE
                    emptyText.visibility = View.GONE
                    generalAdapter.submitList(newsItem)
                }
                else{
                    recyclerView.visibility = View.GONE
                    emptyText.visibility = View.VISIBLE
                    generalAdapter.submitList(emptyList())
                }
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            Log.i("API", error)
        }

        viewModel.getCategoryNews("us", categoryName.toString())


    }

}