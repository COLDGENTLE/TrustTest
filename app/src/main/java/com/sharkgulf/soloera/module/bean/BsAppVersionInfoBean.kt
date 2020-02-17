package com.sharkgulf.soloera.module.bean

/**
 *  Created by user on 2019/3/13
 */
class BsAppVersionInfoBean {
    /**
     * state : 02
     * state_info : 失败
     * seq : 0
     * data : {"ver":"","ver_code":0,"url":"","des":"","upgrade_type":0}
     */

    private var state: String? = null
    private var state_info: String? = null
    private var seq: Int = 0
    private var data: DataBean? = null

    fun getState(): String? {
        return state
    }

    fun setState(state: String) {
        this.state = state
    }

    fun getState_info(): String? {
        return state_info
    }

    fun setState_info(state_info: String) {
        this.state_info = state_info
    }

    fun getSeq(): Int {
        return seq
    }

    fun setSeq(seq: Int) {
        this.seq = seq
    }

    fun getData(): DataBean? {
        return data
    }

    fun setData(data: DataBean) {
        this.data = data
    }

    class DataBean {
        /**
         * ver :
         * ver_code : 0
         * url :
         * des :
         * upgrade_type : 0
         */

        var ver: String? = null
        var ver_code: Int = 0
        var url: String? = null
        var des: String? = null
        var upgrade_type: Int = 0
        var is_update:Int = 0
        var md5:String? = null
        var file_size:Int = 0
    }
}