package com.sharkgulf.soloera.dataanalysis

import android.annotation.SuppressLint
import com.sharkgulf.soloera.TrustAppConfig.*
import com.trust.demo.basis.trust.utils.TrustLogUtils
import java.lang.Exception
import kotlin.concurrent.thread

/**
 *  Created by user on 2019/8/27
 */
class DataQueueManger {
    companion object{
        var mManger:DataQueueManger? = null

        fun getInstance():DataQueueManger{
            if (mManger == null) {
                mManger = DataQueueManger()
            }
            return mManger!!
        }


        private val BEAN_STATUS_READ = -1
        private val BEAN_STATUS_START = 0
        private val BEAN_STATUS_STARTING = 1
        private val BEAN_STATUS_END = 2

        private var TIME_OUT = 1000 * 10
    }

    private val instructionMap = hashMapOf<String,InstructionBean?>()
    private var mThread:Thread? = null
    private var mThreadStatus = false
    private var TAG = "DataQueueManger"

    private val REGISTER_STATUS = WEB_SOKECT_RECEIVE+WEB_CAR_STATUS
    init {
        initMap()
    }

    private fun initMap(){
        /**/
        //开电门
        instructionMap[WEB_SOKECT_SEND+WEB_SOKECT_CAR_ACCON] =
                InstructionBean(WEB_SOKECT_SEND+WEB_SOKECT_CAR_ACCON,
                        WEB_SOKECT_SACK+WEB_SOKECT_SEND+WEB_SOKECT_CAR_ACCON,
                        REGISTER_STATUS,
                        rootNode = WEB_SOKECT_SEND+WEB_SOKECT_CAR_ACCON)


        //关电门
        instructionMap[WEB_SOKECT_SEND+WEB_SOKECT_CAR_ACCOFF] =
                InstructionBean(WEB_SOKECT_SEND+WEB_SOKECT_CAR_ACCOFF,
                        WEB_SOKECT_SACK+WEB_SOKECT_SEND+WEB_SOKECT_CAR_ACCOFF,
                        REGISTER_STATUS,
                        rootNode = WEB_SOKECT_SEND+WEB_SOKECT_CAR_ACCOFF)

        //一键启动
        instructionMap[WEB_SOKECT_SEND+WEB_SOKECT_CAR_START] =
                InstructionBean(WEB_SOKECT_SEND+WEB_SOKECT_CAR_START,
                        WEB_SOKECT_SACK+WEB_SOKECT_SEND+WEB_SOKECT_CAR_START,
                        REGISTER_STATUS,
                        rootNode = WEB_SOKECT_SEND+WEB_SOKECT_CAR_START)

        //设防
        instructionMap[WEB_SOKECT_SEND+WEB_SOKECT_CAR_ALERTON] =
                InstructionBean(WEB_SOKECT_SEND+WEB_SOKECT_CAR_ALERTON,
                        WEB_SOKECT_SACK+WEB_SOKECT_SEND+WEB_SOKECT_CAR_ALERTON,
                        REGISTER_STATUS,
                        rootNode = WEB_SOKECT_SEND+WEB_SOKECT_CAR_ALERTON)


        //撤防
        instructionMap[WEB_SOKECT_SEND+WEB_SOKECT_CAR_ALERTOFF] =
                InstructionBean(WEB_SOKECT_SEND+WEB_SOKECT_CAR_ALERTOFF,
                        WEB_SOKECT_SACK+WEB_SOKECT_SEND+WEB_SOKECT_CAR_ALERTOFF,
                        REGISTER_STATUS,
                        rootNode = WEB_SOKECT_SEND+WEB_SOKECT_CAR_ALERTOFF)
        //寻车
        instructionMap[WEB_SOKECT_SEND+WEB_SOKECT_CAR_FIND] =
                InstructionBean(WEB_SOKECT_SEND+WEB_SOKECT_CAR_FIND,
                        WEB_SOKECT_SACK+WEB_SOKECT_SEND+WEB_SOKECT_CAR_FIND,
                        rootNode = WEB_SOKECT_SEND+WEB_SOKECT_CAR_FIND)

        //开坐桶
        instructionMap[WEB_SOKECT_SEND+WEB_SOKECT_CAR_BUCKET_OPEN] =
                InstructionBean(WEB_SOKECT_SEND+WEB_SOKECT_CAR_BUCKET_OPEN,
                        WEB_SOKECT_SACK+WEB_SOKECT_SEND+WEB_SOKECT_CAR_BUCKET_OPEN,
                        REGISTER_STATUS,
                        rootNode = WEB_SOKECT_SEND+WEB_SOKECT_CAR_BUCKET_OPEN)

        //锁车
        instructionMap[WEB_SOKECT_SEND+WEB_SOKECT_CAR_LOCK] =
                InstructionBean(WEB_SOKECT_SEND+WEB_SOKECT_CAR_LOCK,
                        WEB_SOKECT_SACK+WEB_SOKECT_SEND+WEB_SOKECT_CAR_LOCK,
                        REGISTER_STATUS,
                        rootNode = WEB_SOKECT_SEND+WEB_SOKECT_CAR_LOCK)

        //取消锁车
        instructionMap[WEB_SOKECT_SEND+WEB_SOKECT_CAR_LOCK_CANCEL] =
                InstructionBean(WEB_SOKECT_SEND+WEB_SOKECT_CAR_LOCK_CANCEL,
                        WEB_SOKECT_SACK+WEB_SOKECT_SEND+WEB_SOKECT_CAR_LOCK_CANCEL,
                        REGISTER_STATUS,
                        rootNode = WEB_SOKECT_SEND+WEB_SOKECT_CAR_LOCK_CANCEL)

        //解锁
        instructionMap[WEB_SOKECT_SEND+WEB_SOKECT_CAR_UNLOCK] =
                InstructionBean(WEB_SOKECT_SEND+WEB_SOKECT_CAR_UNLOCK,
                        WEB_SOKECT_SACK+WEB_SOKECT_SEND+WEB_SOKECT_CAR_UNLOCK,
                       REGISTER_STATUS,
                        rootNode = WEB_SOKECT_SEND+WEB_SOKECT_CAR_UNLOCK)

        //取消解锁
        instructionMap[WEB_SOKECT_SEND+WEB_SOKECT_CAR_UNLOCK_CANCEL] =
                InstructionBean(WEB_SOKECT_SEND+WEB_SOKECT_CAR_UNLOCK_CANCEL,
                        WEB_SOKECT_SACK+WEB_SOKECT_SEND+WEB_SOKECT_CAR_UNLOCK_CANCEL,
                        REGISTER_STATUS,
                        rootNode = WEB_SOKECT_SEND+WEB_SOKECT_CAR_UNLOCK_CANCEL)
        /* */
        //
    }


    class InstructionBean(var startNode:String,var nextNode:String?,var endNode:String? = null,var status:Int = BEAN_STATUS_READ,var ts:Long = 0 ,var rootNode:String)

    @Synchronized
    fun changeStatus(topic: String){
        val instructionBean = instructionMap[topic]
        if (instructionBean != null ) {
            val node = when (instructionBean.status) {
                BEAN_STATUS_READ -> {
                    instructionBean.status = BEAN_STATUS_START
                    instructionBean.startNode
                }
                BEAN_STATUS_START -> {
                    instructionBean.status = BEAN_STATUS_STARTING
                    instructionBean.nextNode
                }
                BEAN_STATUS_STARTING -> {
                    instructionBean.status = BEAN_STATUS_END
                    instructionBean.endNode
                }
                BEAN_STATUS_END -> {
                    instructionBean.status = BEAN_STATUS_READ
                    ""
                }
                else -> {
                    null
                }
            }
            if (node != null) {
                instructionBean.rootNode = node
            }
            instructionBean.ts = System.currentTimeMillis()
            instructionMap[topic] = instructionBean

            if (!mThreadStatus) {
                mThreadStatus = true
                loopMap()
            }
        }
    }


    @SuppressLint("NewApi")
    private fun loopMap(){
        if (mThread == null) {
        mThread = thread {
            while (true){
                var mMapIsIdle = true
                try {
                    instructionMap.forEach { k, v ->
                        if (v != null) {
                            TrustLogUtils.d(TAG,"检查是否有指令超时 ")
                            if (v.status != BEAN_STATUS_READ) {
                                mMapIsIdle = false
                                if(System.currentTimeMillis() - v.ts > TIME_OUT){
                                    val nextNode = v.nextNode
                                    val endNode = v.endNode
                                    val node = if (endNode!= null) { endNode }else if(nextNode != null){ nextNode }else{ null }
                                    if (node != null) {
                                        TrustLogUtils.d(TAG,"有指令超时 发送topic $node")
                                        DataAnalysisCenter.getInstance().notice(node,isTimeOut = true,callTopic = v.startNode)
                                        v.status = BEAN_STATUS_READ
                                    }
                                }
                            }
                        }
                    }
                    if (mMapIsIdle) {
                        mThreadStatus = false
                        mThread?.interrupt()
                    }
                    Thread.sleep(2000)
                }catch (e:Exception){
                    e.printStackTrace()
                    mThread = null
                    break//捕获到异常之后，执行break跳出循环。
                }
            }

        }

        }

    }

    fun initInstructionMap(topic: String){
        val instructionBean = instructionMap[topic]
        if (instructionBean != null) {
            instructionBean.status = BEAN_STATUS_READ
            instructionMap[topic] = instructionBean
        }
    }
}

