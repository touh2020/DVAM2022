package eu.epfc.pocketmovie

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    // Create an array of titles

    var tableTitle = arrayOf("RECENT MOVIE","POCKET")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var pager = findViewById<ViewPager2>(R.id.viewPager2)
        var tabLayout  = findViewById<TabLayout>(R.id.tabLayout)

    //initialisation pagerview with fragements and attach it to activity

        PageAdapter(this,tabLayout,pager)
            .apply {
                addFragement(RecentMoviesFragement(),tableTitle.get(0))
                addFragement(PocketFragement(),tableTitle.get(1))
            }.attach()
    }

}