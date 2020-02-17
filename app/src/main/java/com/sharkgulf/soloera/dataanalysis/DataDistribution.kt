package com.sharkgulf.soloera.dataanalysis

/**
 *  Created by user on 2019/8/5
 */
class DataDistribution {
    companion object{
        private var mDataDistribution:DataDistribution? = null
        fun getInstance():DataDistribution{
            if (mDataDistribution == null) {
                mDataDistribution = DataDistribution()
            }
            return mDataDistribution!!
        }
    }

    @Synchronized
    fun updateData(topic:String,data:String?){
        dataMap[topic] = DataDistributionBean(topic,data)
        DataAnalysisBean.getInstanse().updateData(topic,data)
        DataQueueManger.getInstance().changeStatus(topic)
        DataAnalysisCenter.getInstance().notice(topic,data)
    }

    fun getData(topic: String):DataDistributionBean?{
        return dataMap[topic]
    }

    private val dataMap = hashMapOf<String,DataDistributionBean>()
    class DataDistributionBean(var topic:String,var data:String?)

}