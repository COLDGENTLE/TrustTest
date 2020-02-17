package com.sharkgulf.soloera.tool.config

import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.module.bean.socketbean.BikeStatusBean

/**
 *  Created by user on 2019/8/30
 */
var mBikeStatusBean: BikeStatusBean? = null

val controllResultStr:HashMap<String, ResultStr> = hashMapOf(

        //开电门
        Pair(TrustAppConfig.WEB_SOKECT_SEND + TrustAppConfig.WEB_SOKECT_CAR_ACCON,
                ResultStr(setTextStrings(R.string.controll_car_open_acc_ready_txt),
                        setTextStrings(R.string.controll_car_open_acc_success_txt),
                        setTextStrings(R.string.controll_car_open_acc_error_txt),
                        setTextStrings(R.string.controll_car_open_acc_time_out_txt))),
        //关电门
        Pair(TrustAppConfig.WEB_SOKECT_SEND + TrustAppConfig.WEB_SOKECT_CAR_ACCOFF,
                ResultStr(setTextStrings(R.string.controll_car_close_acc_ready_txt),
                        setTextStrings(R.string.controll_car_close_acc_success_txt),
                        setTextStrings(R.string.controll_car_close_acc_error_txt),
                        setTextStrings(R.string.controll_car_close_acc_time_out_txt))),
        //一键启动
        Pair(TrustAppConfig.WEB_SOKECT_SEND + TrustAppConfig.WEB_SOKECT_CAR_START,
                ResultStr(setTextStrings(R.string.controll_car_start_ready_txt),
                        setTextStrings(R.string.controll_car_start_success_txt),
                        setTextStrings(R.string.controll_car_start_error_txt),
                        setTextStrings(R.string.controll_car_start_time_out_txt))),
        //设防
        Pair(TrustAppConfig.WEB_SOKECT_SEND + TrustAppConfig.WEB_SOKECT_CAR_ALERTON,
                ResultStr(setTextStrings(R.string.controll_car_lock_ready_txt),
                        setTextStrings(R.string.controll_car_lock_success_txt),
                        setTextStrings(R.string.controll_car_lock_error_txt),
                        setTextStrings(R.string.controll_car_lock_time_out_txt))),
        //撤防
        Pair(TrustAppConfig.WEB_SOKECT_SEND + TrustAppConfig.WEB_SOKECT_CAR_ALERTOFF,
                ResultStr(setTextStrings(R.string.controll_car_unlock_ready_txt),
                        setTextStrings(R.string.controll_car_unlock_success_txt),
                        setTextStrings(R.string.controll_car_unlock_error_txt),
                        setTextStrings(R.string.controll_car_unlock_time_out_txt))),
        //寻车
        Pair(TrustAppConfig.WEB_SOKECT_SEND + TrustAppConfig.WEB_SOKECT_CAR_FIND,
                ResultStr(setTextStrings(R.string.controll_car_find_car_ready_txt),
                        setTextStrings(R.string.controll_car_find_car_success_txt),
                        setTextStrings(R.string.controll_car_find_car_error_txt),
                        setTextStrings(R.string.controll_car_find_car_time_out_txt))),
        //开坐桶
        Pair(TrustAppConfig.WEB_SOKECT_SEND + TrustAppConfig.WEB_SOKECT_CAR_BUCKET_OPEN,
                ResultStr(setTextStrings(R.string.controll_car_open_bucket_ready_txt),
                        setTextStrings(R.string.controll_car_open_bucket_success_txt),
                        setTextStrings(R.string.controll_car_open_bucket_error_txt),
                        setTextStrings(R.string.controll_car_open_bucket_time_out_txt))),
        //锁车
        Pair(TrustAppConfig.WEB_SOKECT_SEND + TrustAppConfig.WEB_SOKECT_CAR_LOCK,
                ResultStr(setTextStrings(R.string.controll_car_open_acc_ready_txt),
                        setTextStrings(R.string.controll_car_open_acc_ready_txt),
                        setTextStrings(R.string.controll_car_open_acc_ready_txt),
                        setTextStrings(R.string.controll_car_open_acc_ready_txt))),
        //取消锁车
        Pair(TrustAppConfig.WEB_SOKECT_SEND + TrustAppConfig.WEB_SOKECT_CAR_LOCK_CANCEL,
                ResultStr(setTextStrings(R.string.controll_car_open_acc_ready_txt),
                        setTextStrings(R.string.controll_car_open_acc_ready_txt),
                        setTextStrings(R.string.controll_car_open_acc_ready_txt),
                        setTextStrings(R.string.controll_car_open_acc_ready_txt))),
        //解锁
        Pair(TrustAppConfig.WEB_SOKECT_SEND + TrustAppConfig.WEB_SOKECT_CAR_UNLOCK,
                ResultStr(setTextStrings(R.string.controll_car_open_acc_ready_txt),
                        setTextStrings(R.string.controll_car_open_acc_ready_txt),
                        setTextStrings(R.string.controll_car_open_acc_ready_txt),
                        setTextStrings(R.string.controll_car_open_acc_ready_txt))),

        //解锁
        Pair(TrustAppConfig.WEB_SOKECT_SEND + TrustAppConfig.WEB_SOKECT_CAR_UNLOCK_CANCEL,
                ResultStr(setTextStrings(R.string.controll_car_open_acc_ready_txt),
                        setTextStrings(R.string.controll_car_open_acc_ready_txt),
                        setTextStrings(R.string.controll_car_open_acc_ready_txt),
                        setTextStrings(R.string.controll_car_open_acc_ready_txt)))
)


class ResultStr(var reading:String,var successStr:String ,var errorStr:String ,var timeOut:String )
//电门
val CONTROLL_CARS_STATUS_ACC_ON = 1
val CONTROLL_CARS_STATUS_ACC_OFF = 2

//车辆设防状态

val CONTROLL_CAR_LOCK = 1 //设防
val CONTROLL_CAR_UNLOCK = 3 //撤防
val CONTROLL_CAR_NO_MUSIC_LOCK = 2 //静音设防

//坐桶
val CONTROLL_CAR_BUCKET_OPEN = 1
val CONTROLL_CAR_BUCKET_CLOSE = 2

//车锁状态
val CONTRILL_CAR_LOCK_STATUS_LOCK = 1 //已锁定
val CONTRILL_CAR_LOCK_STATUS_UNLOCK = 2 //未锁定
val CONTRILL_CAR_LOCK_STATUS_LOCKING = 3//锁定中
val CONTRILL_CAR_LOCK_STATUS_UNLOCKING = 4//解锁中

//默认状态
val CONTROLL_START_OPEN = 1


//丢车ui 模式
val TYPE_LOSE_BIKE = 1 //丢车模式初始化
val TYPE_LOSE_BIKEING = 2 //丢车模式启动中
val TYPE_LOSE_BIKE_SUCCESS = 9 //丢车模式启动成功
val TYPE_LOCK_BIKE = 4 //锁车模式
val TYPE_BIKE_KEY = "TYPE_BIKE_KEY"

//安防状态
val SECUTITY_ALERT = 1 //警戒模式
val SECUTITY_DO_NOT_DISTURB = 2 //勿扰模式
val SECUTITY_LOSE_CAR = 9 //丢车模式

val ACTION_LOSE_BIKE = 1 //设置丢车模式
val ACTION_CANCEL_LOSE_BIKE = -1 //取消丢车模式

val DEFULT = -1 //默认

