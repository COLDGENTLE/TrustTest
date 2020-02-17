package com.sharkgulf.soloera.bindcar.fragment

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sharkgulf.soloera.R


/**
 *  Created by user on 2019/10/31
 */
class ViewPagerAdapter(context: Context,arrayList: ArrayList<PagerHolder>): PagerAdapter() {
    //上下文
    private val mContext: Context = context
    //数据
    private val mData: List<PagerHolder> = arrayList

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val holder = mData[position]
        val view = View.inflate(mContext, R.layout.viewpager_item, container)
        view.findViewById<ImageView>(R.id.viewpager_ic).setImageResource(holder.ic)
        view.findViewById<TextView>(R.id.viewpager_name).text = holder.name
        return view
    }


    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    override fun getCount(): Int {
        return mData.size
    }


    class PagerHolder(var ic:Int,var msg2:Int,var msg3:Int,var name:String)
}