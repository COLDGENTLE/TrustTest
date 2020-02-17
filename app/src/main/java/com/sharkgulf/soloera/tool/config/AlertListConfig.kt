package com.sharkgulf.soloera.tool.config

import android.content.Intent
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.cards.activity.alert.BsFragmentActivity
import com.sharkgulf.soloera.cards.activity.battery.BatteryActivity
import com.sharkgulf.soloera.cards.activity.history.RideTrackActivity
import com.sharkgulf.soloera.cards.activity.map.MapActivity
import com.sharkgulf.soloera.main.MainHomeActivity
import com.trust.demo.basis.trust.utils.TrustAppUtils

/**
 *  Created by user on 2019/9/9
 */
val USER_GET = 2006                                 //用户拉取
val DEV_REGISTRY = 2001                             //设备注册
val DEV_STATUS = 2002                               //车辆状态变更
val ACCON = "accon"                                 //电门开
val ACCOFF = "accoff"                               //电门关
val GPS_DATA = 2003                                 //gps数据上报
val BATT_DATA = 2004                                //电池数据上报
val RIDE_FINISH = 2005                              //骑行结束
val BATT_INSERT = 1006                              //大电池插入
val BATT_REMOVE = 1007                              //大电池非法移除
val BATT1_LOW = 1008                                //大电池1低电量
val BATT2_LOW = 1009                                //大电池2低电量
val ALERT_MOVE = 1010                               //非法位移
val SLIGHT_VIBRATION = 1011                         //轻微震动
val MEDIUM_VIBRATION = 1012                         //中等震动
val SAVERE_VIBRATION = 1013                         //严重等级
val LOGIN_OUT = 1015                                //账户在其它设备登录
val BATTERT2_ADD = 1016 //电池2 插入
val BATTERT2_REMOVE = 1017//电池2 移除
val TEST_ALERT = 2000                               //测试报警
val TURN_FAULT = 1                                  //转把故障
val BRAKE_FAULT = 2                                 //刹车故障
val MOTOR_FAILURE = 3                               //电机故障
val CONTROLL_FAILURE = 4                            //控制器故障
val MOTOR_HALL_FAILURE = 5                          //电机霍尔故障
val MOTOR_PHASE_LOSS_FAILURE = 6                    //电机缺相故障
val CONTROLL_UNDERVOLTAGE_FAILURE = 8               //控制器欠压
val CONTROLL_OVERVOLTAGE_FAILURE = 9                //控制器过压
val BATTERY_PARAMETERS_NOT_SET_FAILURE = 20         //电池参数未设置
val METER_CONTROLL_COMMUNICATION_FAILURE = 21       //智能仪表控制器通讯故障
val METER_CONTROLL_VERIFICATION_FAILURE = 22        //智能仪表控制器验证失败
val MOTOR_CONTROLL_COMMUNICATION_FAILURE = 23       //智能电机控制器通讯故障
val MOTOR_CONTROLL_VERIFICATION_FAILURE = 24        //智能电机控制器验证失败
val BATTERY_CONTROLL_COMMUNICATION_FAILURE = 25     //智能电池管理器通讯故障
val BATTERY_CONTROLL_VERIFICATION_FAILURE = 26      //智能电池管理器验证失败
val VOICE_CONTROLL_COMMUNICATION_FAILURE = 27       //智能音效控制器通讯故障
val VOICE_CONTROLL_VERIFICATION_FAILURE = 28        //智能音效控制器验证失败
val LIGHT_CONTROLL_COMMUNICATION_FAILURE = 29       //智能灯效控制器通讯故障
val LIGHT_CONTROLL_VERIFICATION_FAILURE = 30        //智能灯效控制器验证失败
val CAR_CENTER_CONTROLL_FAILURE = 31                //车辆中心控制器通信故障

val OVER_DISCHARGE_FAILURE = 40                     //过放
val UNDERVOLTAGE_FAILURE = 41                       //欠压
val CHARGING_OVERCURRENT_FAILURE = 42               //充电过流
val CHARGING_OVERVOLTAGE_FAILURE = 43               //充电过压
val SHORT_CIRCUIT_FAILURE = 44                      //短路
val CHARGE_LOW_TEMPERATURE_FAILURE = 45             //充低温
val PUT_LOW_TEMPERATURE_FAILURE = 46                //放低温
val CHARGE_HIGH_TEMPERATURE_FAILURE = 47            //充高温
val PUT_HIGH_TEMPERATURE_FAILURE = 48               //放高温
val DAY_TIME_RUNNING_LIGHT_FAILURE = 50             //日行灯故障
val TAILLIGHT_FAILURE = 51                          //尾灯故障
val TURN_LEGT_FAILURE = 52                          //左转向灯故障
val TURN_RIGHT_FAILURE = 53                         //右转向灯故障
val HIGH_BEAM_LIGHTS_FAILURE = 54                   //远光灯故障
val LOW_BEAM_LIGHTS_FAILURE = 55                    //近光灯故障
val GEOMAGNETIC_COMMUNICATION_FAILURE = 60          //地磁模块通信故障
val BATTERY2_CONTROLL_COMMUNICATION_FAILURE = 125   //智能电池管理器2通讯故障
val BATTERY2_CONTROLL_VERIFICATION_FAILURE = 126    //智能电池管理器2验证失败
val COMMUNICATION_FAILURE = 193                     //通信故障Pair
val PAIR_CODE_FAILURE = 194                         //对码故障

val BATTERY2_ERROR_140 = 140
val BATTERY2_ERROR_141 = 141
val BATTERY2_ERROR_142 = 142
val BATTERY2_ERROR_143 = 143
val BATTERY2_ERROR_144 = 144
val BATTERY2_ERROR_145 = 145
val BATTERY2_ERROR_146 = 146
val BATTERY2_ERROR_147 = 147
val BATTERY2_ERROR_148 = 148



//alert Config
val POPU_TYPE_NO = 0            //无弹框
val POPU_TYPE_FULL = 1          //全屏
val POPU_TYPE_POPU = 2          //popu
val POPU_TYPE_TOAST = 3         //toast

//alert type
val ALERT_TYPE_CAR_INFO = 1     //车辆消息
val ALERT_TYPE_SYSTEM = 2     //系统消息

//alert key
val ALERT_KEY = "ALERT_KEY"
val ALERT_NOTIFICATIION_KEY = "ALERT_NOTIFICATION_KEY"


val alertInfoMap = hashMapOf<Int, AlertInfoBean>()

fun initAlertInfoMap(){
    //电池
    alertInfoMap[BATT_INSERT] = getAlertInfoBean(BATT_INSERT, R.drawable.alert_battry_add_big_ic)
    alertInfoMap[BATT_REMOVE] = getAlertInfoBean(BATT_REMOVE, R.drawable.alert_battry_remove_big_ic)
    alertInfoMap[BATT1_LOW] = getAlertInfoBean(BATT1_LOW, R.drawable.alert_battry_low_big_ic)
    alertInfoMap[BATT2_LOW] = getAlertInfoBean(BATT2_LOW, R.drawable.alert_battry_low_big_ic)
    alertInfoMap[BATTERY_PARAMETERS_NOT_SET_FAILURE] = getAlertInfoBean(BATTERY_PARAMETERS_NOT_SET_FAILURE, R.drawable.alert_battry_controll_communication_big_ic)
    alertInfoMap[BATTERY_CONTROLL_COMMUNICATION_FAILURE] = getAlertInfoBean(BATTERY_CONTROLL_COMMUNICATION_FAILURE, R.drawable.alert_battry_controll_communication_big_ic)
    alertInfoMap[BATTERY_CONTROLL_VERIFICATION_FAILURE] = getAlertInfoBean(BATTERY_CONTROLL_VERIFICATION_FAILURE, R.drawable.alert_battry_controll_communication_big_ic)
    alertInfoMap[CHARGING_OVERCURRENT_FAILURE] = getAlertInfoBean(CHARGING_OVERCURRENT_FAILURE, R.drawable.alert_battry_controll_communication_big_ic)
    alertInfoMap[CHARGING_OVERVOLTAGE_FAILURE] = getAlertInfoBean(CHARGING_OVERVOLTAGE_FAILURE, R.drawable.alert_battry_controll_communication_big_ic)
    alertInfoMap[CHARGE_LOW_TEMPERATURE_FAILURE] = getAlertInfoBean(CHARGE_LOW_TEMPERATURE_FAILURE, R.drawable.alert_battry_controll_communication_big_ic)
    alertInfoMap[PUT_LOW_TEMPERATURE_FAILURE] = getAlertInfoBean(PUT_LOW_TEMPERATURE_FAILURE, R.drawable.alert_battry_controll_communication_big_ic)
    alertInfoMap[CHARGE_HIGH_TEMPERATURE_FAILURE] = getAlertInfoBean(CHARGE_HIGH_TEMPERATURE_FAILURE, R.drawable.alert_battry_controll_communication_big_ic)
    alertInfoMap[PUT_HIGH_TEMPERATURE_FAILURE] = getAlertInfoBean(PUT_HIGH_TEMPERATURE_FAILURE, R.drawable.alert_battry_controll_communication_big_ic)
    alertInfoMap[OVER_DISCHARGE_FAILURE] = getAlertInfoBean(OVER_DISCHARGE_FAILURE, R.drawable.alert_battry_controll_communication_big_ic)
    alertInfoMap[SHORT_CIRCUIT_FAILURE] = getAlertInfoBean(SHORT_CIRCUIT_FAILURE, R.drawable.alert_battry_controll_communication_big_ic)
    alertInfoMap[BATTERT2_ADD] = getAlertInfoBean(BATTERT2_ADD, R.drawable.alert_battry_controll_communication_big_ic)
    alertInfoMap[BATTERT2_REMOVE] = getAlertInfoBean(BATTERT2_REMOVE, R.drawable.alert_battry_controll_communication_big_ic)

    alertInfoMap[BATTERY2_CONTROLL_COMMUNICATION_FAILURE] = getAlertInfoBean(BATTERY2_CONTROLL_COMMUNICATION_FAILURE, R.drawable.alert_battry_controll_communication_big_ic)
    alertInfoMap[BATTERY2_ERROR_140] = getAlertInfoBean(BATTERY2_ERROR_140, R.drawable.alert_battry_controll_communication_big_ic)
    alertInfoMap[BATTERY2_ERROR_141] = getAlertInfoBean(BATTERY2_ERROR_141, R.drawable.alert_battry_controll_communication_big_ic)
    alertInfoMap[BATTERY2_ERROR_142] = getAlertInfoBean(BATTERY2_ERROR_142, R.drawable.alert_battry_controll_communication_big_ic)
    alertInfoMap[BATTERY2_ERROR_143] = getAlertInfoBean(BATTERY2_ERROR_143, R.drawable.alert_battry_controll_communication_big_ic)
    alertInfoMap[BATTERY2_ERROR_144] = getAlertInfoBean(BATTERY2_ERROR_144, R.drawable.alert_battry_controll_communication_big_ic)
    alertInfoMap[BATTERY2_ERROR_145] = getAlertInfoBean(BATTERY2_ERROR_145, R.drawable.alert_battry_controll_communication_big_ic)
    alertInfoMap[BATTERY2_ERROR_146] = getAlertInfoBean(BATTERY2_ERROR_146, R.drawable.alert_battry_controll_communication_big_ic)
    alertInfoMap[BATTERY2_ERROR_147] = getAlertInfoBean(BATTERY2_ERROR_147, R.drawable.alert_battry_controll_communication_big_ic)
    alertInfoMap[BATTERY2_ERROR_148] = getAlertInfoBean(BATTERY2_ERROR_148, R.drawable.alert_battry_controll_communication_big_ic)



    //非法位移
    alertInfoMap[ALERT_MOVE] = getAlertInfoBean(ALERT_MOVE, R.drawable.alert_move_big_ic)
    //震动
    alertInfoMap[SLIGHT_VIBRATION] = getAlertInfoBean(SLIGHT_VIBRATION, R.drawable.alert_slight_vibration_big_ic)
    alertInfoMap[MEDIUM_VIBRATION] = getAlertInfoBean(MEDIUM_VIBRATION, R.drawable.alert_medium_vibration_big_ic)
    alertInfoMap[SAVERE_VIBRATION] = getAlertInfoBean(SAVERE_VIBRATION, R.drawable.alert_savere_vibration_big_ic)
    //测试
    alertInfoMap[TEST_ALERT] = getAlertInfoBean(TEST_ALERT, R.drawable.alert_test_big_ic)
    //转把
    alertInfoMap[TURN_FAULT] = getAlertInfoBean(TURN_FAULT, R.drawable.alert_turn_big_ic)
    //刹车
    alertInfoMap[BRAKE_FAULT] = getAlertInfoBean(BRAKE_FAULT, R.drawable.alert_brake_big_ic)

    //电机
    alertInfoMap[MOTOR_FAILURE] = getAlertInfoBean(MOTOR_FAILURE, R.drawable.alert_motor_big_ic)
    alertInfoMap[MOTOR_HALL_FAILURE] = getAlertInfoBean(MOTOR_HALL_FAILURE, R.drawable.alert_motor_big_ic)
    alertInfoMap[MOTOR_PHASE_LOSS_FAILURE] = getAlertInfoBean(MOTOR_PHASE_LOSS_FAILURE, R.drawable.alert_motor_big_ic)

    //控制器
    alertInfoMap[CONTROLL_FAILURE] = getAlertInfoBean(CONTROLL_FAILURE, R.drawable.alert_controll_big_ic)
    alertInfoMap[CONTROLL_UNDERVOLTAGE_FAILURE] = getAlertInfoBean(CONTROLL_UNDERVOLTAGE_FAILURE, R.drawable.alert_controll_big_ic)
    alertInfoMap[CONTROLL_OVERVOLTAGE_FAILURE] = getAlertInfoBean(CONTROLL_OVERVOLTAGE_FAILURE, R.drawable.alert_controll_big_ic)
    alertInfoMap[METER_CONTROLL_COMMUNICATION_FAILURE] = getAlertInfoBean(CONTROLL_OVERVOLTAGE_FAILURE, R.drawable.alert_controll_big_ic)
    alertInfoMap[METER_CONTROLL_VERIFICATION_FAILURE] = getAlertInfoBean(METER_CONTROLL_VERIFICATION_FAILURE, R.drawable.alert_controll_big_ic)
    alertInfoMap[MOTOR_CONTROLL_COMMUNICATION_FAILURE] = getAlertInfoBean(MOTOR_CONTROLL_COMMUNICATION_FAILURE, R.drawable.alert_controll_big_ic)
    alertInfoMap[MOTOR_CONTROLL_VERIFICATION_FAILURE] = getAlertInfoBean(MOTOR_CONTROLL_VERIFICATION_FAILURE, R.drawable.alert_controll_big_ic)
    alertInfoMap[CAR_CENTER_CONTROLL_FAILURE] = getAlertInfoBean(CAR_CENTER_CONTROLL_FAILURE, R.drawable.alert_controll_big_ic)

    //音效
    alertInfoMap[VOICE_CONTROLL_COMMUNICATION_FAILURE] = getAlertInfoBean(VOICE_CONTROLL_COMMUNICATION_FAILURE, R.drawable.alert_voice_controll_communication_big_ic)
    alertInfoMap[VOICE_CONTROLL_VERIFICATION_FAILURE] = getAlertInfoBean(VOICE_CONTROLL_VERIFICATION_FAILURE, R.drawable.alert_voice_controll_communication_big_ic)
    alertInfoMap[VOICE_CONTROLL_VERIFICATION_FAILURE] = getAlertInfoBean(VOICE_CONTROLL_VERIFICATION_FAILURE, R.drawable.alert_voice_controll_communication_big_ic)


    //灯光
    alertInfoMap[DAY_TIME_RUNNING_LIGHT_FAILURE] = getAlertInfoBean(DAY_TIME_RUNNING_LIGHT_FAILURE, R.drawable.alert_light_big_ic)
    alertInfoMap[TAILLIGHT_FAILURE] = getAlertInfoBean(TAILLIGHT_FAILURE, R.drawable.alert_light_big_ic)
    alertInfoMap[TURN_LEGT_FAILURE] = getAlertInfoBean(TURN_LEGT_FAILURE, R.drawable.alert_light_big_ic)
    alertInfoMap[TURN_RIGHT_FAILURE] = getAlertInfoBean(TURN_RIGHT_FAILURE, R.drawable.alert_light_big_ic)
    alertInfoMap[HIGH_BEAM_LIGHTS_FAILURE] = getAlertInfoBean(HIGH_BEAM_LIGHTS_FAILURE, R.drawable.alert_light_big_ic)
    alertInfoMap[LOW_BEAM_LIGHTS_FAILURE] = getAlertInfoBean(LOW_BEAM_LIGHTS_FAILURE, R.drawable.alert_light_big_ic)

}


private fun getAlertInfoBean(event:Int,ic:Int): AlertInfoBean {
    val intent = when (event) {
          SLIGHT_VIBRATION,
          TURN_FAULT, BRAKE_FAULT, MOTOR_FAILURE, CONTROLL_FAILURE,
        PUT_HIGH_TEMPERATURE_FAILURE ,MOTOR_HALL_FAILURE,MOTOR_PHASE_LOSS_FAILURE,CONTROLL_UNDERVOLTAGE_FAILURE,CONTROLL_OVERVOLTAGE_FAILURE
            ,BATTERY_PARAMETERS_NOT_SET_FAILURE,METER_CONTROLL_COMMUNICATION_FAILURE,METER_CONTROLL_VERIFICATION_FAILURE
            ,MOTOR_CONTROLL_COMMUNICATION_FAILURE,MOTOR_CONTROLL_VERIFICATION_FAILURE,BATTERY_CONTROLL_COMMUNICATION_FAILURE
            ,BATTERY_CONTROLL_VERIFICATION_FAILURE,VOICE_CONTROLL_COMMUNICATION_FAILURE,VOICE_CONTROLL_VERIFICATION_FAILURE,
        LIGHT_CONTROLL_COMMUNICATION_FAILURE,LIGHT_CONTROLL_VERIFICATION_FAILURE,OVER_DISCHARGE_FAILURE,UNDERVOLTAGE_FAILURE,DAY_TIME_RUNNING_LIGHT_FAILURE
            ,TAILLIGHT_FAILURE,TURN_LEGT_FAILURE,TURN_RIGHT_FAILURE,HIGH_BEAM_LIGHTS_FAILURE,LOW_BEAM_LIGHTS_FAILURE,CHARGE_HIGH_TEMPERATURE_FAILURE
              ,PUT_LOW_TEMPERATURE_FAILURE,CHARGE_LOW_TEMPERATURE_FAILURE,CHARGING_OVERVOLTAGE_FAILURE
              ,SHORT_CIRCUIT_FAILURE,CHARGING_OVERCURRENT_FAILURE
        -> {
            Intent(TrustAppUtils.getContext(), BsFragmentActivity::class.java)
        }
        BATT1_LOW,BATT2_LOW,BATT_INSERT -> {
            Intent(TrustAppUtils.getContext(), BatteryActivity::class.java)
        }
        ALERT_MOVE -> {
            Intent(TrustAppUtils.getContext(),RideTrackActivity::class.java)
        }
        BATT_REMOVE -> {
            Intent(TrustAppUtils.getContext(),MainHomeActivity::class.java)
        }
        MEDIUM_VIBRATION,SAVERE_VIBRATION -> {
            Intent(TrustAppUtils.getContext(),MapActivity::class.java)
        }
        else -> {
            null
        }
    }
    return AlertInfoBean(event, ic, intent)
}

class AlertInfoBean(var event:Int,var ic:Int,var intent:Intent? = null)
