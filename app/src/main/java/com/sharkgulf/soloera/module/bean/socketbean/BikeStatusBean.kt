package com.sharkgulf.soloera.module.bean.socketbean

import com.sharkgulf.soloera.TrustAppConfig.BIKE_OL
import com.sharkgulf.soloera.tool.config.CONTROLL_CARS_STATUS_ACC_ON
import com.sharkgulf.soloera.tool.config.CONTROLL_CAR_BUCKET_OPEN
import com.sharkgulf.soloera.tool.config.CONTROLL_CAR_LOCK

/**
 *  Created by user on 2019/8/30
 */
class BikeStatusBean{
    /**
     * path : /push/bike/bikestatus
     * header : {"to":"4","uuid":"882c17c8-1d59-4032-8052-50e79ad891f9","ts":1567159619,"ack":1}
     * body : {"acc":1,"bike_id":5,"defence":1,"lstatus":0,"rstatus":0,"sstatus":0,"ts":1567159618000000000}
     */

    private var path: String? = "/push/bike/bikestatus"
    private var header: HeaderBean? = null
    private var body: BodyBean? = null

    fun getPath(): String? {
        return path
    }

    fun setPath(path: String) {
        this.path = path
    }

    fun getHeader(): HeaderBean? {
        return header
    }

    fun setHeader(header: HeaderBean) {
        this.header = header
    }

    fun getBody(): BodyBean? {
        return body
    }

    fun setBody(body: BodyBean) {
        this.body = body
    }

    class HeaderBean {
        /**
         * to : 4
         * uuid : 882c17c8-1d59-4032-8052-50e79ad891f9
         * ts : 1567159619
         * ack : 1
         */

        var to: String? = "4"
        var uuid: String? = "----"
        var ts: Int = 0
        var ack: Int = 0
    }

    class BodyBean {
        /**
         * acc : 1
         * bike_id : 5
         * defence : 1
         * lstatus : 0
         * rstatus : 0
         * sstatus : 0
         * ts : 1567159618000000000
         */

        var acc: Int = 0
        var bike_id: Int = 0
        var defence: Int = 0
        var lstatus: Int = 0
        var rstatus: Int = 0
        var sstatus: Int = 0
        var ts: Long = 0
        var ol_status:Int = 0
        private var isAccOn :Boolean = false
        private var isLock :Boolean = false
        private var isSstaus:Boolean = false
        private var isOL:Boolean = false
        fun getIsAccOn():Boolean{
            isAccOn = acc == CONTROLL_CARS_STATUS_ACC_ON
            return isAccOn
        }

        fun getIsLock():Boolean{
            isLock = defence == CONTROLL_CAR_LOCK
            return isLock
        }

        fun getIsOl():Boolean{
            isOL = ol_status == BIKE_OL
            return isOL
        }

        fun getIsSstatus():Boolean{
            isSstaus = sstatus == CONTROLL_CAR_BUCKET_OPEN
            return isSstaus
        }

    }
}