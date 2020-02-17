package com.sharkgulf.soloera.cards.activity.battery

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 *  Created by user on 2019/2/22
 */
class BatteryAdapter( fm : FragmentManager): FragmentPagerAdapter(fm) {
    private var titles:ArrayList<String> = arrayListOf()
    private var fragments:ArrayList<Fragment> = arrayListOf()

    fun setTitlesFragments(titleList:ArrayList<String> ,list:ArrayList<Fragment>){
        titles = titleList
        fragments = list
    }

    override fun getItem(p0: Int): Fragment {
        return fragments[p0]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun getCount(): Int {
        return titles.size
    }
}