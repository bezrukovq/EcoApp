package com.example.ecoapp.feature.news_tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.ecoapp.R
import com.example.ecoapp.feature.news_tabs.newslist.NewsLikedFragment
import com.example.ecoapp.feature.news_tabs.newslist.NewsListFragment
import com.md.nails.presentation.basemvp.BaseMvpFragment
import kotlinx.android.synthetic.main.fragment_news_tabs.*

class NewsTabsFragment: BaseMvpFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_news_tabs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewpager.adapter = NewsPagerAdapter(childFragmentManager)
        sliding_tabs.setupWithViewPager(viewpager)
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                ((viewpager.adapter as NewsPagerAdapter).list[position] as NewsTab).refresh()
            }

        })
    }

}
class NewsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val list = arrayListOf<Fragment>(newsListFragment, newsLikedFragment)

    override fun getCount(): Int  = list.size

    override fun getItem(i: Int): Fragment = list[i]

    override fun getPageTitle(position: Int): CharSequence {
        return (list[position] as NewsTab).getTitle()
    }
    companion object{
        val newsListFragment = NewsListFragment()
        val newsLikedFragment = NewsLikedFragment()
    }
}