package com.sharkgulf.soloera.dataanalysis

import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


/**
 *  Created by user on 2019/8/7
 */
class DataAnalusis {

    companion object{
        private var mDataAnalusis:DataAnalusis? = null
        fun getInstanse():DataAnalusis{
            if (mDataAnalusis == null) {
                mDataAnalusis = DataAnalusis()
            }
            return mDataAnalusis!!
        }
    }
    private val mDataQueue: Queue<String> =LinkedList<String>()
    private val mThreadPoolExecutor = ThreadPoolExecutor(3, 5, 5, TimeUnit.SECONDS,
            LinkedBlockingQueue<Runnable>(100))

    @Synchronized
    fun updateData(data:String){
        mDataQueue.offer(data)
        mThreadPoolExecutor.execute(DataRunnable(mDataQueue.poll()))
    }


}