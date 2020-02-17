package com.sharkgulf.soloera.module.bean

/*
 *Created by Trust on 2019/1/2
 */
class BsGetCapchaBean {

    /**
     * state : 0000
     * state_info : OK
     * cust_info :
     * seq : 1234
     * data : {"captcha":{"type":1,"sessionid":"abcdefg","url":["http://captcha.img.url1","http://captcha.img.url2"],"prompt":"用户提示信息","input":"1234"}}
     */

    private var state: String? = null
    private var state_info: String? = null
    private var cust_info: String? = null
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

    fun getCust_info(): String? {
        return cust_info
    }

    fun setCust_info(cust_info: String) {
        this.cust_info = cust_info
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
         * captcha : {"type":1,"sessionid":"abcdefg","url":["http://captcha.img.url1","http://captcha.img.url2"],"prompt":"用户提示信息","input":"1234"}
         */

        var captcha: CaptchaBean? = null

        class CaptchaBean {
            /**
             * type : 1
             * sessionid : abcdefg
             * url : ["http://captcha.img.url1","http://captcha.img.url2"]
             * prompt : 用户提示信息
             * input : 1234
             */

            var type: Int = 0
            var sessionid: String? = null
            var prompt: String? = null
            var input: String? = null
            var url: List<String>? = null
        }
    }
}