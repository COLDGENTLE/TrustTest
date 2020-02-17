package com.sharkgulf.soloera

import android.app.Activity
import android.content.pm.PackageManager
import com.google.gson.Gson
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.db.bean.DbUserLoginStatusBean
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.db.bean.DbBleBean
import com.sharkgulf.soloera.jpushlibrary.getRegistrationId
import com.sharkgulf.soloera.module.DbModel
import com.sharkgulf.soloera.module.bean.BsGetBindInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.tool.config.ACTION_CANCEL_LOSE_BIKE
import com.sharkgulf.soloera.tool.config.ACTION_LOSE_BIKE
import com.sharkgulf.soloera.tool.config.getAgent
import com.sharkgulf.soloera.tool.config.getDevId
import com.trust.demo.basis.base.ModuleResultInterface
import com.trust.demo.basis.trust.utils.TrustAppUtils
import com.trust.demo.basis.trust.utils.TrustLogUtils
import com.trust.demo.basis.trust.utils.TrustMD5Utils
import org.json.JSONObject

/**
 *  Created by user on 2019/3/13
 */
class RequestConfig {

    companion object {
        init {
            checkSalt()
        }

        private var mSalt:String? = null
        private val DID:String = "BC5DEC61C7355A3CC29BAA4C4F0D382C"
        private val TAG = "RequestConfig"
        private fun encryptionPwd(pwd: String?):String{
            if (mSalt == null) {
                checkSalt()
            }
            return TrustMD5Utils.getMD5(mSalt, TrustMD5Utils.getMD5(pwd), "BS")
        }

        private fun checkSalt(){
            val userBean = BsApplication.bsDbManger!!.checkUserLoginStatus(userPhone)
            mSalt = userBean?.userSalt
        }
        fun requestAppVersion():HashMap<String,Any>{
            val map = hashMapOf<String,Any>()
            map["platform"] = "A"
            map["channel"] = TrustAppUtils.getChannel("UMENG_CHANNEL")
            map["pkg_name"] = TrustAppUtils.getContext().packageName
            return map
        }

        fun requestEditUserInfo(bean: DbUserLoginStatusBean):HashMap<String,Any>{
            TrustLogUtils.d(TAG,"bean.userBean!! ${Gson().toJson(bean.userBean!!)}")
            val map = hashMapOf<String,Any>()
            map["user"] = JSONObject(Gson().toJson(bean.userBean!!))
            return map
        }


        fun requestUserKey(phone:String,isUseTemporaryToken: Boolean):HashMap<String,Any>{
            val map = HashMap<String, Any>()
            map["phone_num"] = phone
            map["isUseTemporaryToken"] = isUseTemporaryToken
            return map
        }

        fun setEditPwd(newPwd:String,oldPwd:String? = null):HashMap<String,Any>{
            val map = hashMapOf<String,Any>()
            if (oldPwd != null) {
                map["curr_password"] = encryptionPwd(oldPwd)
            }
                map["new_password"] = encryptionPwd(newPwd)
            return map
        }

        fun requestUserOrCarInfo():HashMap<String,Any>{
            val mapUser = hashMapOf<String,Any>()
            mapUser["user_id"] = TrustAppConfig.userId
            return mapUser
        }

        fun updateBindStatus(bindStatus:Int,bean:DbBleBean):HashMap<String,Any>{
            val bindStatusMap = hashMapOf<String, Any>()
            bindStatusMap["bike_id"] = TrustAppConfig.bikeId
            bindStatusMap["status"] = bindStatus
                val btsign = hashMapOf<String,Any?>()
                btsign["did"] = DID
                btsign["validation"] = bean.validation
                btsign["sign"] = bean.sign
                btsign["salt"] = bean.salt
                btsign["bt_mac"] = BsApplication.removeString(bean.mac)

                bindStatusMap["btsign"] = btsign
            return bindStatusMap
        }


        fun requestBarreryInfo():HashMap<String,Any>{
            val barreryMap = hashMapOf<String, Any>()
            barreryMap["bike_id"] = TrustAppConfig.bikeId
            return barreryMap
        }


        fun requestTimeLevel(startTime:String,endTime:String):HashMap<String,Any>{
            val timeLevelMap = hashMapOf<String, Any>()
            timeLevelMap["time_level"] = DATA_TYPE
            timeLevelMap["begin_date"] = startTime
            timeLevelMap["end_date"] = endTime
            return timeLevelMap
        }

        fun requestRideSummary(startTime:String,endTime:String):HashMap<String,Any>{
            val rideSummaryMap = hashMapOf<String, Any>()
            rideSummaryMap["bike_id"] = bikeId
            rideSummaryMap["time_level"] = DATA_TYPE
            rideSummaryMap["begin"] = startTime
            rideSummaryMap["end"] = endTime
            return rideSummaryMap
        }

        fun requestRideReport(day:String):HashMap<String,Any>{
            val rideReportMap = hashMapOf<String, Any>()
            rideReportMap["bike_id"] = bikeId
            rideReportMap["day"] = day
            return rideReportMap
        }

        fun requestRideTrack(bike_id:Int,track_id:Int):HashMap<String,Any>{
            val rideTrackMap = hashMapOf<String, Any>()
            rideTrackMap["bike_id"] = bike_id
            rideTrackMap["track_id"] = track_id
            return rideTrackMap
        }

        fun requestCheckUserThree(phone:String,data: MutableMap<String, String>):HashMap<String,Any>{
            val checkUserThree = hashMapOf<String,Any>()
            checkUserThree["type"] = 3
            val partner = hashMapOf<String,Any>()
            partner["partner"] = data["partner"]!!
            partner["user_openid"] = if (data["openid"] != null)  data["openid"]!! else  data["uid"]!!
            partner["user_uid"] = data["uid"]!!
            partner["nick_name"] = data["name"]!!
            partner["access_token"] = data["access_token"]!!
            checkUserThree["partner"] = partner
            checkUserThree["phone_num"] = phone
            return checkUserThree
        }

        fun requestCheckUserRegister(type:Int,data: MutableMap<String, String>):HashMap<String,Any>{
            val checkUserRegister = hashMapOf<String,Any>()
            checkUserRegister["type"] = type
            val partner = hashMapOf<String,Any?>()
            partner["partner"] = data["partner"]!!
            partner["user_openid"] = if (data["openid"] != null)  data["openid"] else  data["uid"]
            partner["user_uid"] = data["uid"]
            partner["nick_name"] = data["name"]!!
            partner["access_token"] = data["access_token"]
            checkUserRegister["partner"] = partner
            return checkUserRegister
        }

        fun requestUserRegister(type:Int,phone:String,data: HashMap<String, String>):HashMap<String,Any>{
            val userRegister= hashMapOf<String,Any>()
            userRegister["type"] =  type
            userRegister["phone_num"] =  phone
            val partner = hashMapOf<String,Any>()
            partner["partner"] = data["partner"]!!
            partner["user_openid"] = if (data["openid"] != null)  data["openid"]!! else  data["uid"]!!
            partner["user_uid"] = data["uid"]!!
            partner["nick_name"] = data["name"]!!
            partner["access_token"] = data["access_token"]!!
            userRegister["partner"] = partner
            return userRegister
        }



        fun userThreeLogin(activity:Activity,data: HashMap<String, String>):HashMap<String,Any>{
            val login = hashMapOf<String,Any>()
            login["type"] = 4
            login["channel"] = appChannel
            login["trigger"] = TYPE_USER
            login["dev_id"] = getDevId()
            val partner = hashMapOf<String,Any>()
            partner["partner"] = data["partner"]!!
            partner["user_openid"] = if (data["openid"] != null)  data["openid"]!! else  data["uid"]!!
            partner["user_uid"] = data["uid"]!!
            partner["nick_name"] = data["name"]!!
            partner["access_token"] = data["access_token"]!!
            login["partner"] = partner
            return login
        }

        fun getTicket():HashMap<String,Any>{
            val ticketMap = hashMapOf<String,Any>()
            ticketMap["type"] = 1
            ticketMap["channel"] = 1
            return ticketMap
        }

        fun getOrderInfo(channel:String,amount:Int,order_no:String):HashMap<String,Any>{
            val orderInfoMap = hashMapOf<String,Any>()
            try {
                val pingMap = hashMapOf<String,Any>()
                pingMap["channel"] = channel
                pingMap["amount"] = amount * 100
                pingMap["order_no"] = order_no
                orderInfoMap["pingpp"] = pingMap
            }catch (e:Exception){
                TrustLogUtils.e(e.toString())
            }

            return orderInfoMap
        }



        fun getPhoneLogin(phone:String,type:Int,isUseTemporaryToken:Boolean):HashMap<String,Any>{
            val phoneLoginMap = hashMapOf<String,Any>()
            phoneLoginMap["phone_num"] = phone
            phoneLoginMap["type"] = type
            phoneLoginMap["isUseTemporaryToken"] = isUseTemporaryToken
            return phoneLoginMap
        }

        fun getCapcha(type:Int):HashMap<String,Any>{
            val phoneLoginMap = hashMapOf<String,Any>()
            phoneLoginMap["token"] = TrustMD5Utils.getMD5("BLUE+ts+SHARK")
            phoneLoginMap["type"] = type
            return phoneLoginMap
        }

        fun getSubmintVerificationCode(type:Int,phone:String?,verofocatopmCodeStr:String,data:HashMap<String,String>?):HashMap<String,Any>{
            val map = HashMap<String, Any>()
            map["phone_num"] = phone!!
            map["sms_vc"] = verofocatopmCodeStr
            map["type"] = type
            map["channel"] = "CHL"
            map["trigger"] = TYPE_USER
            map["dev_id"] = getDevId()
            TrustLogUtils.d(TAG,"getSubmintVerificationCode ${data.toString()} ")
            if (type == LOGIN_TYPE_THERE) {
                val partner = hashMapOf<String,String>()
                partner["partner"] = data!!["partner"]!!
                partner["user_openid"] = data["openid"] ?: data["uid"]!!
                partner["user_uid"] = data["uid"]!!
                partner["nick_name"] = data["name"]!!
                partner["access_token"] = data["access_token"]!!
                map["partner"] = partner
            }




            return map
        }



        fun getCheckSmsvc(phone:String?,verofocatopmCodeStr:String):HashMap<String,Any>{
            val map = hashMapOf<String,Any>()
            map["phone_num"] = phone!!
            map["sms_vc"] = verofocatopmCodeStr
            return map
        }

        fun requestGetBindInfo(mac:String):HashMap<String,Any>{
            val bindCar = hashMapOf<String, Any>()
            bindCar["bt_macs"] = arrayListOf(mac)
            bindCar["did"] = "BC5DEC61C7355A3CC29BAA4C4F0D382C"
            return bindCar
        }



        fun changeSecurity(mode:Int ,map: HashMap<Int,Boolean>):HashMap<String,Any>{
            val changeSecurityMap = hashMapOf<String,Any>()
            changeSecurityMap["bike_id"] = bikeId
            val security = hashMapOf<String,Any>()
            security["mode"] = mode
            val custom = hashMapOf<String,Boolean?>()
            custom["move"] = map[R.id.alert_move]
            custom["batt_out"] = map[R.id.alert_batt_out]
//            custom["batt_in"] = map[R.id.alert_batt_in]
            custom["vibr_severe"] = map[R.id.alert_serious_shock_sw]
            custom["vibr_moderate"] = map[R.id.alert_medium_shock_sw]
//            custom["vibr_mild"] = map[R.id.alert_slight_shock_sw]
            custom["power_low"] = map[R.id.alert_power_low_sw]
            security["custom"] = custom
            changeSecurityMap["security"] = security
            return changeSecurityMap
        }


        fun testNotify():HashMap<String,Any>{
            val testNotifyMap = hashMapOf<String,Any>()
            testNotifyMap["type"] = 1
            testNotifyMap["bike_id"] = bikeId
            return testNotifyMap
        }


        fun updatePushId():HashMap<String,Any>{
            val context = TrustAppUtils.getContext()
            val pushIdMap = hashMapOf<String,Any>()
            pushIdMap["channel_id"] = getRegistrationId(context)!!
            pushIdMap["dev_id"] = getDevId()
            pushIdMap["channel"] = context.packageManager
                    .getApplicationInfo(context.packageName,
                            PackageManager.GET_META_DATA).metaData.getString("UMENG_CHANNEL")!!
            pushIdMap["client"] = "android"
            return pushIdMap
        }

        fun getBleInfo(bikeId:Int):HashMap<String,Any>{
            val bleMap = hashMapOf<String,Any>()
            bleMap["bike_id"] = bikeId
            bleMap["did"] = "BC5DEC61C7355A3CC29BAA4C4F0D382C"
            return bleMap
        }


        fun lostModu(bikeId:Int,isLostModu:Boolean):HashMap<String,Any>{
            val lostModu = hashMapOf<String,Any>()
            lostModu["bike_id"] = bikeId
            lostModu["k"] = if(isLostModu) ACTION_LOSE_BIKE else ACTION_CANCEL_LOSE_BIKE
            return lostModu
        }

        fun alertList(bikeId: IntArray,day:String):HashMap<String,Any>{
            val alertList = hashMapOf<String,Any>()
//            alertList["bike_id"] = bikeId
            alertList["bids"] = bikeId
            alertList["date"] = day
            alertList["t"] = arrayOf(1)

            var time = 0
            DbModel().getAlertNewestTime(DEFUTE, day,object : ModuleResultInterface<Int> {
                override fun resultData(bean: Int?,pos:Int?) {
                    if (bean != null) {
                        time = bean
                    }
                }

                override fun resultError(msg: String) {}
            })
            alertList["sta"] = time
            return alertList
        }



        fun getAlertList(day:String,bikeId: Int,is_read:Boolean):HashMap<String,Any>{
            val alertList = hashMapOf<String,Any>()
            alertList["bike_id"] = bikeId
            alertList["date"] = day
            alertList["is_read"] = is_read
            return alertList
        }




        fun pwdLogin(phone:String,pwd:String,isUseTemporaryToken: Boolean):HashMap<String,Any>{
            checkSalt()
            TrustLogUtils.d(TAG,"mSalt:$mSalt")
            val map = HashMap<String, Any>()
            map["phone_num"] = phone
            map["password"] = TrustMD5Utils.getMD5(mSalt, TrustMD5Utils.getMD5(pwd), "BS")
            map["type"] = LOGIN_TYPE_PWD
            map["channel"] = appChannel
            map["trigger"] = TYPE_USER
            map["dev_id"] = getAgent()
            map["isUseTemporaryToken"] = isUseTemporaryToken
            return map
        }

        fun updateProfile(bean: BsGetBindInfoBean.DataBean.BindInfoBean,bikeName:String):HashMap<String,Any>{
            bean.bike_info!!.setBike_name(bikeName)
            val toJson = Gson().toJson(bean)
            val map = HashMap<String, Any>()
            map["bike_info"] = JSONObject(toJson).getJSONObject("bike_info")
            return map
        }

        fun updateCarProfile(bean: CarInfoBean):HashMap<String,Any>{
            val toJson = Gson().toJson(bean)
            val map = HashMap<String, Any>()
            map["bike_info"] = JSONObject(toJson)
            return map
        }


        fun updatePhone(oldPhone:String,newPhone:String,vc:String):HashMap<String,Any>{
            val map = HashMap<String, Any>()
            map["pn_old"] = oldPhone
            map["pn_new"] = newPhone
            map["vc"] = vc
            return map
        }


        fun checkUserIsRegister(phone:String):HashMap<String,Any>{
            val map = HashMap<String, Any>()
            map["type"] = USER_REGISTER_PHONE
            map["phone_num"] = phone
            return map
        }




    }





}