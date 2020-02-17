package com.sharkgulf.soloera.tool.view.layout.weight.secondfloor

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View

/*
 *Created by Trust on 2019/1/8
 */
/**
 * 判断子控件类型 处理对应的结果
 */
class ViewTypeProcessing {
    private var mRecyclerView: RecyclerView? = null
    private var mView:View? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    fun checkView(v:View){
        mView = v
        if(mView is RecyclerView){
            mRecyclerView = mView as RecyclerView
        }
    }



    /*
     * 解决 控件是recyclerView的时候加载item 以后显示HeadView的bug
     */
     fun typeRecyclerView():Int{
        if (mRecyclerView != null) {
            val layoutManager = mRecyclerView!!.layoutManager!!
            if(layoutManager is LinearLayoutManager){
                val manager  = layoutManager
                return manager.findFirstCompletelyVisibleItemPosition()
            }else if(layoutManager is GridLayoutManager){
                val manager  = layoutManager
                return manager.findFirstCompletelyVisibleItemPosition()
            }
        }
        return  -1

    }





}