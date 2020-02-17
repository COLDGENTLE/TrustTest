package com.sharkgulf.soloera.module.bean

/*
 *Created by Trust on 2019/1/2
 */
class BsGetUserInfoBean {

    /**
     * state : 00
     * state_info : OK
     * seq : 0
     * data : {"user":{"user_id":4,"phone_num":"13892929789","account":"","email":"","nick_name":"蓝鲨","icon":"","real_name":"","gender":0,"birthday":"0001-01-01","id_no":"","location":"","red_time":"2019-01-24","pwd_status":1,"per_sign":"","completion":30,"home":{},"mile_remind":0}}
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
         * user : {"user_id":4,"phone_num":"13892929789","account":"","email":"","nick_name":"蓝鲨","icon":"","real_name":"","gender":0,"birthday":"0001-01-01","id_no":"","location":"","red_time":"2019-01-24","pwd_status":1,"per_sign":"","completion":30,"home":{},"mile_remind":0}
         */

        var user: UserBean? = null

        class UserBean {
            /**
             * user_id : 4
             * phone_num : 13892929789
             * account :
             * email :
             * nick_name : 蓝鲨
             * icon :
             * real_name :
             * gender : 0
             * birthday : 0001-01-01
             * id_no :
             * location :
             * red_time : 2019-01-24
             * pwd_status : 1
             * per_sign :
             * completion : 30
             * home : {}
             * mile_remind : 0
             */

            var user_id: Int = 0
            var phone_num: String? = null
            var account: String? = null
            var email: String? = null
            var nick_name: String? = null
            var icon: String? = null
            var real_name: String? = null
            var gender: Int = 0
            var birthday: String? = null
            var id_no: String? = null
            var location: String? = null
            var red_time: String? = null
            var pwd_status: Int = 0
            var per_sign: String? = null
            var completion: Int = 0
            var home: HomeBean? = null
            var mile_remind: Int = 0

            class HomeBean
        }
    }
}