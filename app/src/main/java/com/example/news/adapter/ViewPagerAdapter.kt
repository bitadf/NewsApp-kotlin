package com.example.news.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.news.news_fragments.CategoryFragment
import com.example.news.news_fragments.HomeFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity ) :
    FragmentStateAdapter(fragmentActivity){

        private val categoryName = listOf(
            "general" , "business" , "entertainment" ,
            "health" , "science" , "sports" , "technology" , "Recent" , "Bookmarks"
        )
    override fun getItemCount(): Int {
        return categoryName.size
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> HomeFragment()
            else -> CategoryFragment.newInstance(categoryName[position])
        }

    }

}