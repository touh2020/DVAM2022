package eu.epfc.pocketmovie

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PageAdapter (fm: FragmentActivity
                   ,private val tabLayout: TabLayout? = null ,
                    private val viewPager2: ViewPager2? = null
): FragmentStateAdapter(fm) {
    private val fragementList : ArrayList<Fragment> = arrayListOf()
    private  val  fragmentTitleList : ArrayList<String> = arrayListOf()

    override fun getItemCount(): Int {
            return fragementList.size
    }

    override fun createFragment(position: Int): Fragment {
     return fragementList[position]
    }

    fun addFragement(fragment: Fragment,title:String?="Title")
    {
        fragementList.add(fragment)
        fragmentTitleList.add(title.toString())
    }
    private fun setTabLayoutMediator()
    {
        TabLayoutMediator(tabLayout!!,viewPager2!!){
            tab,position ->
            tab.text = this.fragmentTitleList[position]
        }.attach()
    }
    fun attach()
    {
        viewPager2?.adapter=this
        tabLayout?.let {
            setTabLayoutMediator()
        }
    }
}
