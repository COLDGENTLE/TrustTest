package com.sharkgulf.soloera.bindcar.fragment

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sharkgulf.soloera.R
import com.trust.demo.basis.trust.utils.TrustLogUtils

/**
 *  Created by user on 2019/10/31
 */
class BindCarRecyclerAdapter(list: List<PagerHolderData>): RecyclerView.Adapter<BindCarRecyclerAdapter.PagerHolder>() {
    private val mList = list
    private val TAG = BindCarRecyclerAdapter::class.java.canonicalName


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PagerHolder {
        val context = p0.context
        val v = LayoutInflater.from(context).inflate(R.layout.viewpager_item,p0,false)
        TrustLogUtils.d(TAG,"p0.context ${p0.context} v $v")
        return PagerHolder(v)
    }

    override fun getItemCount(): Int {
        TrustLogUtils.d(TAG,"getItemCount ${mList.size}")
        return mList.size-1
    }

    override fun onBindViewHolder(hodler: PagerHolder, p1: Int) {
        val pagerHolderData = mList[p1]
        hodler.ic?.setImageResource(pagerHolderData.ic)
        TrustLogUtils.d(TAG,"onBindViewHolder ${pagerHolderData.name}")
        hodler.name?.text = pagerHolderData.name
    }


    class PagerHolder(v:View): RecyclerView.ViewHolder(v){
        var ic:ImageView? = null
        var name:TextView? = null
        init {
            ic = itemView.findViewById(R.id.viewpager_ic)
            name = itemView.findViewById(R.id.viewpager_name)
        }
    }

    class PagerHolderData(var ic:Int,var msg2:Int,var msg3:Int,var name:String)
}