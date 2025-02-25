package com.example.news.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.example.news.R
import com.example.news.adapter.ViewPagerAdapter
import com.example.news.databinding.ActivityMainBinding
import com.example.news.news_fragments.SearchResultsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private lateinit var bottomNavigationView : BottomNavigationView

    private lateinit var toolBar: androidx.appcompat.widget.Toolbar

    private lateinit var tablayout : TabLayout

    private lateinit var viewPager2: ViewPager2

    private lateinit var adapter: ViewPagerAdapter
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tablayout = binding.tabLayout
        viewPager2 = binding.viewPager2

        adapter = ViewPagerAdapter(this)
        viewPager2.adapter = adapter

        TabLayoutMediator(tablayout , viewPager2){
            tab , position ->
            when(position){
                0 -> tab.text = "General"
                1 -> tab.text = "Business"
                2 -> tab.text = "Entertainment"
                3 -> tab.text = "Health"
                4 -> tab.text = "Science"
                5 -> tab.text = "Sports"
                6 -> tab.text = "Technology"
                7 -> tab.text = "Recent"
                8 -> tab.text = "Bookmarks"
            }
        }.attach()



        bottomNavigationView = binding.bottomNavigation
        toolBar = binding.toolbar
        setSupportActionBar(toolBar)



        bottomNavigationView.setOnItemSelectedListener {
            menuItem ->
            when(menuItem.itemId){
                R.id.bottom_home ->{
                    SetSearchFrame(binding.viewPager2 , binding.searchFrameContainer ,
                        binding.tabLayout , binding.bottomNavigation , false)
                    viewPager2.setCurrentItem(0,true)
                    true
                }
                R.id.bottom_recent ->{
                    SetSearchFrame(binding.viewPager2 , binding.searchFrameContainer ,
                        binding.tabLayout , binding.bottomNavigation , false)
                    viewPager2.setCurrentItem(7 , true)
                    true
                }
                R.id.bottom_archive -> {
                    SetSearchFrame(binding.viewPager2 , binding.searchFrameContainer ,
                        binding.tabLayout , binding.bottomNavigation , false)
                    viewPager2.setCurrentItem(8 , true)
                    true
                }
                R.id.bottom_profile ->{
                    SetSearchFrame(binding.viewPager2 , binding.searchFrameContainer ,
                        binding.tabLayout , binding.bottomNavigation , false)
                    Toast.makeText(this , R.string.profile, Toast.LENGTH_SHORT).show()
                    true
                }
                else -> true
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.toolbar_menu , menu)

        val  searchItem = menu?.findItem(R.id.toolbar_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.apply {
            queryHint = "Search news..."
            val searchEditText = findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            searchEditText.setTextColor(ContextCompat.getColor(this@MainActivity , R.color.white))
            searchEditText.setHintTextColor(ContextCompat.getColor(this@MainActivity , R.color.grey))
        }
        ///////
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    // Handle the search query here
                    showSearchResults(query)
                    Toast.makeText(this@MainActivity, "Searching for: $query", Toast.LENGTH_SHORT).show()
                    searchView.clearFocus()
                    // Add your search implementation here
                }
                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true

            }
        })
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                // Called when search view collapses (back arrow clicked)
                SetSearchFrame(binding.viewPager2, binding.searchFrameContainer, binding.tabLayout, binding.bottomNavigation, false)
                binding.viewPager2.setCurrentItem(0, true)
                return true
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toolbar_notification -> {

                Toast.makeText(this, R.string.notification, Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun showSearchResults(query : String){
        SetSearchFrame(binding.viewPager2 , binding.searchFrameContainer ,
            binding.tabLayout , binding.bottomNavigation , true)
        //replaceFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.search_frame_container , SearchResultsFragment.newInstance(query))
            .addToBackStack(null)
            .commit()
    }
    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        if(binding.searchFrameContainer.visibility == View.VISIBLE){
            SetSearchFrame(binding.viewPager2 , binding.searchFrameContainer ,
                binding.tabLayout , binding.bottomNavigation , false)
        }
        else {
            viewPager2.setCurrentItem(0,true)
            super.onBackPressed()
        }


    }
    private fun SetSearchFrame(
        viewPager2: ViewPager2, searchFragment:FrameLayout,
        tabLayout: TabLayout, bottomNavigationView: BottomNavigationView , mode :Boolean
    ) {
        //if false gone
        if (!mode) {
            viewPager2.visibility = View.VISIBLE
            tabLayout.visibility = View.VISIBLE
            bottomNavigationView.visibility = View.VISIBLE
            searchFragment.visibility = View.GONE
        } else {
            //show
            viewPager2.visibility = View.GONE
            tabLayout.visibility = View.GONE
            bottomNavigationView.visibility = View.VISIBLE
            searchFragment.visibility = View.VISIBLE
        }


    }

}