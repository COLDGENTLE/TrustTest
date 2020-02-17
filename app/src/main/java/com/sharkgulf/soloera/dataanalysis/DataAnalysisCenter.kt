package com.sharkgulf.soloera.dataanalysis

import com.google.gson.Gson
import com.sharkgulf.soloera.TrustAppConfig.WEB_SOKECT_HEART
import com.sharkgulf.soloera.TrustAppConfig.WEB_SOKECT_SEND
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.tool.config.controllResultStr
import com.sharkgulf.soloera.module.bean.socketbean.SocketBean
import com.sharkgulf.soloera.module.bean.socketbean.SocketSendBean
import com.trust.demo.basis.trust.utils.TrustLogUtils
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 *  Created by user on 2019/8/5
 */
class DataAnalysisCenter {
    companion object{
        private var mDataAnalysisCenter:DataAnalysisCenter? = null
        fun getInstance():DataAnalysisCenter{
            if (mDataAnalysisCenter == null) {
                mDataAnalysisCenter = DataAnalysisCenter()
            }
            return mDataAnalysisCenter!!
        }
    }


    private val mGson = Gson()
    private var mBikeId:Int = -1
    private val TAG = "DataAnalysisCenter"
    private val topicMap:HashMap<String,ArrayList<DataAnalysBean>> = hashMapOf()

    interface onDataAnalysisCallBack{
        fun onNoticeCallBack(msg:String?)
        fun onErrorCallBack(msg :String,timeOutTopic: String?)
    }
    class DataAnalysBean(var topic: String,var callBack:onDataAnalysisCallBack,var tag :String)

    fun setBikeId(bikeId: Int){
        mBikeId = bikeId
    }

    fun registerCallBack(topic:String,callBack:onDataAnalysisCallBack,tag:String){
        if (topicMap[topic] == null) {
            topicMap[topic] = arrayListOf(DataAnalysBean(topic,callBack,tag))
        }else{
            if (topicMap[topic]!!.indexOf(DataAnalysBean(topic,callBack,tag)) == -1) {
                topicMap[topic]!!.add(DataAnalysBean(topic,callBack,tag))
            }
        }
    }

    fun unRegisterCallBack(topic:String,tag:String){
        val bean = topicMap[topic]?.find { it.tag == tag && it.topic == topic }
        topicMap[topic]?.remove(bean)
    }


    fun notice(topic: String,data:String? = null,isTimeOut:Boolean = false,callTopic:String? = null){
        TrustLogUtils.d(TAG,"notice $topic")
        topicMap[topic]?.forEach {
            if (isTimeOut) {
                it.callBack.onErrorCallBack(controllResultStr[callTopic!!]!!.timeOut,callTopic)
            }else{
                it.callBack.onNoticeCallBack(data)
            }
        }
    }

    fun<T> getData(topic: String,bikeId: Int? = null):T?{
        return DataAnalysisBean.getInstanse().getData<T>(topic, if (bikeId != null) bikeId else mBikeId)
    }

    fun<T> setData(topic:String,data:T){
        DataAnalysisBean.getInstanse().setDbData(topic,data)
    }

    fun sendHeartData(){
        send<Any>("$WEB_SOKECT_SEND$WEB_SOKECT_HEART",1)
    }

    fun sendData(path: String,bikeId:Int? = null,ack: Int = 1){
        TrustLogUtils.d(TAG,"sendData  $path")
        val bean = SocketSendBean()
        bean.bike_id = if (bikeId != null) bikeId else mBikeId
        send(path,ack,bean)
        DataQueueManger.getInstance().changeStatus(path)
    }

    fun  sendAnswer(path: String,uuid:String,ack: Int){
        send<Any>(path,ack,uuid = uuid)
    }

    fun sendLocalData(topic: String,msg:String?){
        DataDistribution.getInstance().updateData(topic,msg)
    }

    private  fun<T> send(path:String,ack:Int,bean : T? = null ,uuid:String = UUID.randomUUID().toString()){
        val socketBean = SocketBean<T>()
        socketBean.path = path
        socketBean.header = SocketBean.HeaderBean()
        socketBean.header?.uuid =uuid
        socketBean.header?.ack = ack
        socketBean.header?.ts = (System.currentTimeMillis() / 1000).toInt()
        socketBean.body = bean
        TrustLogUtils.d(TAG,"send  ${mGson.toJson(socketBean)}")
        BsApplication.mTrustWebSocket?.sendMessage(mGson.toJson(socketBean))
    }

    fun sendLocalData(topic:String){
        DataDistribution.getInstance().updateData(topic,null)
    }
}