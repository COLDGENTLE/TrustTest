package com.sharkgulf.soloera.tool.config

import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.db.dbmanger.BsDbManger
import com.sharkgulf.soloera.db.bean.DbUserLoginStatusBean
import com.sharkgulf.soloera.module.bean.BsGetCarInfoBean
import com.sharkgulf.soloera.module.bean.BsGetUserInfoBean
import com.trust.demo.basis.trust.utils.TrustLogUtils

/**
 *  Created by user on 2019/7/15
 */
class Authentication {

    companion object {
        private var mAuthentication: Authentication? = null
        fun getAuthentication(): Authentication {
            if (mAuthentication == null) {
                mAuthentication = Authentication()
            }
            return mAuthentication!!
        }
    }
    private val TAG = "Authentication"
    private var mDbUserBean: DbUserLoginStatusBean? = null
    private var mBsDbManger : BsDbManger? = null


    //获取用户登录状态
    fun getUserLoginStatus():Boolean{
        return if (getUserBean()) {
            return mDbUserBean!!.userLoginStatus
        }else{
            false
        }
    }


    //设置用户登录状态
    fun setUserLoginStatus(status:Boolean){
        if (getUserBean()) {
            if (TrustAppConfig.userPhone != null && TrustAppConfig.userPhone != "") {
                mDbUserBean!!.userLoginStatus = status
                setUserCarBean(mDbUserBean!!)
            }else{
                TrustLogUtils.d(TAG,"user is error  : ${TrustAppConfig.userPhone}")
            }
        }
    }

    fun setUserInfoName(name:String){
        if (getUserBean()) {
            mDbUserBean!!.userBean!!.real_name = name
            setUserCarBean(mDbUserBean!!)
        }
    }

    fun setUserPhone(newPhone:String){
        if (getUserBean()) {
            mDbUserBean!!.userPhone
            mDbUserBean!!.userPhone = newPhone
            mDbUserBean!!.userBean!!.phone_num = newPhone
            setUserCarBean(mDbUserBean!!)
        }
    }

    fun setUser(user:DbUserLoginStatusBean){
        if (getUserBean()) {
            mDbUserBean = user
            setUserCarBean(mDbUserBean!!)
        }
    }

    private fun getUserBean():Boolean{ return getUserBean(TrustAppConfig.userPhone) }


    private fun getUserBean(phone:String?):Boolean{
        mBsDbManger = BsApplication.bsDbManger
        mDbUserBean = if (phone != null&& phone != "") {
            mBsDbManger?.checkUserLoginStatus(phone)
        }else{
            mBsDbManger?.checkUserLoginStatus()
        }
        TrustLogUtils.d(TAG,"getUserBean $mDbUserBean")
        return mDbUserBean != null
    }

    private fun setUserCarBean(bean:DbUserLoginStatusBean){
        mBsDbManger!!.setUserLoginStatus(bean)
    }

    //更新车辆信息
    fun setCarInfo(bean: BsGetCarInfoBean.DataBean?){
        if (getUserBean()) {
            mDbUserBean!!.userBikeList = bean
            setUserCarBean(mDbUserBean!!)
        }
    }

    //获取当前用户车辆信息
    fun getUserCarInfo():Boolean{
        return if (getUserBean()) {
            if (mDbUserBean!!.userBikeList?.bikes != null) {
                mDbUserBean!!.userBikeList?.bikes!!.size != 0
            }else{
                false
            }
        }else{
            false
        }
    }

    //获取当前用户车辆信息
    fun getUserCarList():BsGetCarInfoBean.DataBean?{
        return if (getUserBean()) {
            mDbUserBean!!.userBikeList
        }else{
            null
        }
    }

    //获取当前用户签到积分
    fun getUserCheckinNum():Int{
        return  if (getUserBean()) {
            mDbUserBean!!.userCheckInNum
        }else{
            0
        }
    }

    //更新当前用户签到积分
    fun setUserCheckinNum(num:Int){
        if (getUserBean()) {
            mDbUserBean?.userCheckInNum = num
            setUserCarBean(mDbUserBean!!)
        }
    }


    //获取当前登录用户bean
    fun getUser():DbUserLoginStatusBean?{
        getUserBean()
        return mDbUserBean
    }


    //获取当前登录用户bean
    fun getUser(phone:String):DbUserLoginStatusBean?{
        getUserBean(phone)
        return mDbUserBean
    }

    //异常退出 全部用户登录状态更新
    fun allUserExt(){
        mBsDbManger?.allUserExt()
    }

    //获取最后一个登录的用户信息
    fun getRecentLoginUser():DbUserLoginStatusBean?{
        return mBsDbManger?.getRecentLoginUser()
    }


    fun getToken():String?{
        val user = getUser()
        return user?.userToken
    }


    fun getChooseBidPos(isFiler:Boolean,list:List<BsGetCarInfoBean.DataBean.BikesBean>?):Int?{
        getUserBean()
        var i = if(isFiler) 0 else DEFULT
        list?.forEachIndexed { index, bikesBean ->
            if (bikesBean.bike_id == mDbUserBean?.bidPos) {
                DataAnalysisCenter.getInstance().setBikeId(bikesBean.bike_id)
                i = index
            }
        }
        val notEmpty = list?.isNotEmpty()
        if(notEmpty != null && notEmpty){
            DataAnalysisCenter.getInstance().setBikeId(list[0].bike_id)
        }
        return i
    }


    fun getChooseBid():Int?{
        getUserBean()
        mDbUserBean?.bidPos
        return  mDbUserBean?.bidPos
    }

    fun setChooseBid(userPhone:String = "",bidPos:Int){
        if (getUserBean()) {
            mDbUserBean?.bidPos = bidPos
            setUserCarBean(mDbUserBean!!)
        }
    }


    fun findDemoUser():DbUserLoginStatusBean?{
        return mBsDbManger?.findDemoUser()
    }

    fun setDemoUser(bean: BsGetUserInfoBean){
        mBsDbManger?.setDemoUser(bean)
    }

    fun setDemoUserBikes(bean:BsGetCarInfoBean){
        mBsDbManger?.setDemoUserBikes(bean)
    }
}