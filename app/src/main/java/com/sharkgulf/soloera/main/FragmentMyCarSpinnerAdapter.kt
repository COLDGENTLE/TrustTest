package com.sharkgulf.soloera.main

import android.database.DataSetObserver
import android.view.View
import android.view.ViewGroup
import android.widget.SpinnerAdapter
import com.sharkgulf.soloera.module.bean.BsGetCarInfoBean

/**
 *  Created by user on 2019/7/1
 */
class FragmentMyCarSpinnerAdapter: SpinnerAdapter {
    override fun getItemViewType(position: Int): Int {
        return position
    }

    private val ml: BsGetCarInfoBean.DataBean? = null

    override fun isEmpty(): Boolean {
        return ml==null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return View(convertView?.context)
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {
    }


    override fun getItem(position: Int): Any {
        return 1
    }

    override fun getViewTypeCount(): Int {
        return ml?.bikes!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return View(convertView?.context)
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
    }

    override fun getCount(): Int {
        return ml?.bikes?.size!!
    }
}