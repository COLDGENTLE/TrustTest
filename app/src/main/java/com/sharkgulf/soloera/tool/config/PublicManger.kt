package com.sharkgulf.soloera.tool.config

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.fragment.app.Fragment
import android.text.Html
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.appliction.BsApplication.Companion.bsDbManger
import com.sharkgulf.soloera.db.bean.DbBleBean
import com.sharkgulf.soloera.loging.ChangePwdActivity
import com.sharkgulf.soloera.loging.LogingActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.bumptech.glide.request.transition.Transition
import com.sharkgulf.soloera.BuildConfig
import com.sharkgulf.soloera.appliction.BsApplication.Companion.applicationContext
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.UserProtocolActivity
import com.sharkgulf.soloera.appliction.BsActivityLifecycleCallbacks
import com.sharkgulf.soloera.appliction.BsApplication.Companion.mBsApplication
import com.sharkgulf.soloera.appliction.BsApplication.Companion.mTrustWebSocket
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.db.bean.DbUserLoginStatusBean
import com.sharkgulf.soloera.db.dbmanger.BsAlertBeanDbManger
import com.sharkgulf.soloera.db.dbmanger.BsBleDbManger
import com.sharkgulf.soloera.db.dbmanger.BsCarInfoDbManger
import com.sharkgulf.soloera.db.dbmanger.BsDbManger
import com.sharkgulf.soloera.home.fragment.FragmentHomeControlCard
import com.sharkgulf.soloera.home.fragment.FragmentHomeMapCard
import com.sharkgulf.soloera.home.fragment.history.FragmentHomeHistoryCard
import com.sharkgulf.soloera.jpushlibrary.getRegistrationId
import com.sharkgulf.soloera.main.FragmentMyCar
import com.sharkgulf.soloera.main.MainHomeActivity
import com.sharkgulf.soloera.module.bean.BsGetBindInfoBean
import com.sharkgulf.soloera.module.bean.BsGetCarInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.*
import com.sharkgulf.soloera.tool.SizeLabel
import com.sharkgulf.soloera.tool.alert.AlertTool
import com.sharkgulf.soloera.tool.arouter.ROUTE_PATH_LOGING
import com.sharkgulf.soloera.tool.arouter.arouterStartActivity
import com.sharkgulf.soloera.tool.config.Authentication.Companion.getAuthentication
import com.sharkgulf.soloera.tool.view.ControllUtils
import com.sharkgulf.soloera.tool.view.TextDrawable
import com.sharkgulf.soloera.tool.view.dialog.TrustGeneralPurposePopupwindow
import com.sharkgulf.soloera.tool.view.layout.weight.BatteryBgView
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.trust.utils.*
import com.trust.demo.basis.trust.utils.appcrash.TrustAndroidDeviceInfo
import com.trust.retrofit.config.ProjectInit
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 *  Created by user on 2019/3/18
 */

@SuppressLint("CheckResult")
fun ImageView.glide(url:String?, isCircleCrop:Boolean = false, placeholderIc:Int = R.drawable.car_ic, isAnimation:Boolean = false){
    val build = DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build()
    if (url != null && url != "") {
        val glide = Glide.with(this).load(url)
                .placeholder(placeholderIc).error(placeholderIc)
        if (isCircleCrop) {
            glide.apply(RequestOptions().circleCrop())
        }
        if (isAnimation) {
            glide.transition(DrawableTransitionOptions.with(build))
        }
        glide.into(this)
    }else{
        val load = Glide.with(this).load(placeholderIc)
        if (isCircleCrop) {
            load.apply(RequestOptions().circleCrop())
        }

        if (isAnimation) {
            load.transition(DrawableTransitionOptions.with(build))
        }
        load.into(this)
    }
}


fun ImageView.glide(url:Int?, isCircleCrop:Boolean = false, placeholderIc:Int = R.drawable.car_ic, isAnimation:Boolean = false){
    val build = DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build()
    if (url != null ) {
        val glide = Glide.with(this).load(url)
                .placeholder(placeholderIc).error(placeholderIc)
        if (isCircleCrop) {
            glide.apply(RequestOptions().circleCrop())
        }
        if (isAnimation) {
            glide.transition(DrawableTransitionOptions.with(build))
        }
        glide.into(this)
    }else{
        val load = Glide.with(this).load(placeholderIc)
        if (isCircleCrop) {
            load.apply(RequestOptions().circleCrop())
        }

        if (isAnimation) {
            load.transition(DrawableTransitionOptions.with(build))
        }
        load.into(this)
    }
}

fun ImageView.glideUserIc(url:String?,isCircleCrop:Boolean = false,isAnimation: Boolean = false){
    glide(url,isCircleCrop,R.drawable.user_defulte_ic,isAnimation)
}

fun ImageView.glideUserIc(url:Int,isCircleCrop:Boolean = false,isAnimation: Boolean = false){
    glide(url,isCircleCrop,R.drawable.user_defulte_ic,isAnimation)
}

@SuppressLint("CheckResult")
fun glideBitmap(context: Context, url:String?, listener: onGetGlideBitmapListener, isCircleCrop:Boolean = false, isCropSize:Boolean = false){
    try {
        val load = Glide.with(context.applicationContext)
                .asBitmap()
                .load(url)

        if (isCircleCrop) {
            load.apply(RequestOptions().circleCrop())
            load.into(object :SimpleTarget<Bitmap>(150,150){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    listener.getGlideBitmapListener(resource)
                }
            })
        }else{
            load.into(object :SimpleTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    listener.getGlideBitmapListener(resource)
                }
            })
        }
    }catch (e:Exception){

    }

}


@SuppressLint("CheckResult")
fun glideBitmap(context: Context, url:Uri?, listener: onGetGlideBitmapListener, isCircleCrop:Boolean = false, isCropSize:Boolean = false){
    try {
        val load = Glide.with(context.applicationContext)
                .asBitmap()

                .load(url)

        if (isCircleCrop) {
            load.apply(RequestOptions().circleCrop())
            load.into(object :SimpleTarget<Bitmap>(150,150){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    listener.getGlideBitmapListener(resource)
                }
            })
        }else{
            load.into(object :SimpleTarget<Bitmap>(300,300){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    listener.getGlideBitmapListener(resource)
                }
            })
        }
    }catch (e:Exception){

    }

}


fun setTextStrings(stringsId:Int,msg:String = ""):String{
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(applicationContext?.getString(stringsId, msg), 0, null, SizeLabel(30)).toString()
    }else{
        return Html.fromHtml(applicationContext?.getString(stringsId, msg),  null, SizeLabel(30)).toString()
    }
}

fun setTextStrings(stringsId:Int,msg:String,msg1:String):String{
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(applicationContext?.getString(stringsId, msg,msg1), 0, null, SizeLabel(30)).toString()
    }else{
        return Html.fromHtml(applicationContext?.getString(stringsId, msg,msg1),  null, SizeLabel(30)).toString()
    }
}

fun setTextSpanneds(stringsId:Int, msg:String = "",size :Int = 30):Spanned?{
   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(applicationContext?.getString(stringsId, msg), 0, null, SizeLabel(size))
    }else{
        return Html.fromHtml(applicationContext?.getString(stringsId, msg),  null, SizeLabel(size))
    }
}

fun setTextSpanneds(stringsId:Int, msg1:String = "",msg2:String = "",size :Int = 30):Spanned?{
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(applicationContext?.getString(stringsId, msg1 ,msg2), 0, null, SizeLabel(size))
    }else{
        return Html.fromHtml(applicationContext?.getString(stringsId, msg1 ,msg2),  null, SizeLabel(size))
    }
}

fun setHtmlSpanneds(stringsId: Int,msg:String = "",size: Int = 30,callBack: HtmlSpannedsCallBack):Spanned{
    val listener = Html.ImageGetter { source ->
        val drawable: Drawable? =
                applicationContext?.resources?.getDrawable(callBack.callBack(source),null)
        // Important
        drawable!!.setBounds(0, 0, drawable.intrinsicWidth, drawable
                .intrinsicHeight )
        drawable
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(applicationContext?.getString(stringsId, msg), 0, listener, SizeLabel(size))
    }else{
        return Html.fromHtml(applicationContext?.getString(stringsId, msg),  listener, SizeLabel(size))
    }

}

fun setHtmlSpanneds(stringsId: Int,msg:String = "",color: Int = R.color.colorBlack,callBack: SizeLabel.HtmlOnClickListener):Spanned{
    val context = TrustAppUtils.getContext()
    val textColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.resources.getColor(color,null)
    }else{
        context.resources.getColor(color)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(applicationContext?.getString(stringsId, msg), 0, null, SizeLabel(textColor,callBack))
    }else{
        return Html.fromHtml(applicationContext?.getString(stringsId, msg),  null, SizeLabel(textColor,callBack))
    }

}


fun setHtmlSpanneds(v:TextView,stringsId: Int,msg:String = "",color: Int = R.color.colorBlack,callBack: SizeLabel.HtmlOnClickListener){
    val context = TrustAppUtils.getContext()
    val textColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.resources.getColor(color,null)
    }else{
        context.resources.getColor(color)
    }

    val spanneds = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
         Html.fromHtml(applicationContext?.getString(stringsId, msg), 0, null, SizeLabel(textColor,callBack))
    }else{
         Html.fromHtml(applicationContext?.getString(stringsId, msg),  null, SizeLabel(textColor,callBack))
    }
    v.text = spanneds
    v.movementMethod = LinkMovementMethod.getInstance()
    v.highlightColor = Color.parseColor("#00000000")
}

interface HtmlSpannedsCallBack{
    fun callBack(source:String):Int
}

fun setTextStrings(v:TextView?,stringsId:Int,msg:String = "",color:Int=Color.parseColor("#ffffff"),listener: SizeLabel.HtmlOnClickListener? = null) {
    val s  =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(applicationContext?.getString(stringsId,msg)
                        ,0,null, SizeLabel(color, listener))
            } else {
                Html.fromHtml(applicationContext?.getString(stringsId,msg)
                        ,null, SizeLabel(color, listener))
            }
    v?.text = s
    v?.movementMethod = LinkMovementMethod.getInstance()
    v?.highlightColor = color
}

interface onGetGlideBitmapListener{
    fun getGlideBitmapListener(bitmap: Bitmap?)
}




fun Activity.finishTransition(){
    finishAfterTransition()
}


fun Fragment.finshActivity(){
    TrustMVPActivtiy.activityList.forEach {
        when (it) {
            is LogingActivity,
            is ChangePwdActivity -> {
                it.finish()
            }
            else -> {
            }
        }

    }
}



fun Fragment.getBikeInfo(bikeId:Int): BsGetCarInfoBean.DataBean.BikesBean?{
    return bsDbManger?.getBikeInfo(bikeId)
}


 fun Activity.checkPhone(phone:String?,errorMsg:String):Boolean{
    return if (TrustStringUtils.isPhoneNumberValid(phone)) {
        true
    }else{
      Toast.makeText(this,errorMsg, Toast.LENGTH_LONG).show()
        false
    }
}

private var mCarIc:Bitmap? = null
fun getCarIc():Bitmap?{
    return mCarIc
}

fun setCarIc(bitmap: Bitmap?){
    mCarIc = bitmap
}


private var mUserIc:Bitmap? = null
fun getUserIc():Bitmap?{
    return mUserIc
}

fun setUserIc(bitmap: Bitmap?){
    mUserIc = bitmap
}



fun ImageView.setColor(color: String){
    setColorFilter(Color.parseColor(color))
}
fun ImageView.setColor(color: Int){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        setColorFilter(getBsColor(color))
    }
}

fun ImageView.setIc(trueIc:Int,falseIc:Int,status:Boolean){
    setImageResource(if(status) trueIc else falseIc)
}

fun dataAnalyCenter():DataAnalysisCenter{
    return DataAnalysisCenter.getInstance()
}


fun getWebSocketStatus():Boolean{
    return mTrustWebSocket!!.getWebSocketStatus()
}

fun showToast(msg:String?){
    Observable.create(ObservableOnSubscribe<String> { emitter ->
        if (msg != null) {
            emitter.onNext(msg)
        }
    }).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {
                override fun onComplete() {}

                override fun onSubscribe(d: Disposable) {}

                override fun onNext(t: String) {
                    showToastFree(TrustAppUtils.getContext(),t)
//                    Toast.makeText(TrustAppUtils.getContext(),t,Toast.LENGTH_LONG).show()
//                    ToastUtils.getiInstance().showShortToast(t)
                }

                override fun onError(e: Throwable) {}
            })
}

fun showBsToast(msg:String?){
    Observable.create(ObservableOnSubscribe<String> { emitter ->
        if (msg != null) {
            emitter.onNext(msg)
        }
    }).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {
                override fun onComplete() {}

                override fun onSubscribe(d: Disposable) {}

                override fun onNext(t: String) {
                    showToastFree(TrustAppUtils.getContext(),t)
//                    Toast.makeText(TrustAppUtils.getContext(),t,Toast.LENGTH_LONG).show()
//                    ToastUtils.getiInstance().showShortToast(t)
                }

                override fun onError(e: Throwable) {}
            })
}

fun showDebugToast(msg:String){
    if (BuildConfig.DEBUG || getChannel("UMENG_CHANNEL") == "BS_DEV") {
        Observable.create(ObservableOnSubscribe<String> { emitter ->
            emitter.onNext(msg)
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onComplete() {}

                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(t: String) {
                        showToastFree(TrustAppUtils.getContext(),t)
                    }

                    override fun onError(e: Throwable) {}
                })
    }
}


/**
 * 初始化当前登录用户信息
 */
fun checkUser():Boolean{
    val user = getAuthentication().getUser()
    if (user != null && user.userLoginStatus) {
        TrustAppConfig.token = user.userToken
        TrustAppConfig.userId = user.userId
        TrustAppConfig.userPhone = user.userPhone
        ProjectInit.addToken(TrustAppConfig.token)
    }
    return user != null && user?.userLoginStatus
}


fun getUser(): DbUserLoginStatusBean?{
    val user = BsApplication.mAuthentication.getUser()
    if (user != null&& user.userLoginStatus) {
        TrustAppConfig.token = user.userToken
        TrustAppConfig.userId = user.userId
        TrustAppConfig.userPhone = user.userPhone
        ProjectInit.addToken(TrustAppConfig.token)
    }
    return user
}



fun getDbManger(): BsDbManger {
    return bsDbManger!!
}

fun getDbBleManger():BsBleDbManger{
    return bsDbManger!!.getDbBleMagner()
}

fun getDbCarInfoManger():BsCarInfoDbManger{
    return bsDbManger!!.getDbCarInfoManger()
}

fun getDbAlertDbManger(): BsAlertBeanDbManger {
    return bsDbManger!!.getDbAlertDbManger()
}

fun getContext():Context{
    return TrustAppUtils.getContext()
}

fun getAlerTool():AlertTool{
    return AlertTool.getInstance()
}

fun getAppConfig():AppConfig{
    return AppConfig.getInstance()
}

fun getAppPrivacyPolicyStatus():Boolean{
    return getAppConfig().getAppPrivacyPolicyStatus()
}

fun setAppPrivacyPilicyStatus(status:Boolean){
    getAppConfig().setAppPrivacyPilicyStatus(status)
}

fun getBsActivity():Activity{
    return getBsActivityLifecycleCallbacks().getActivity()
}

fun getBsActivityLifecycleCallbacks():BsActivityLifecycleCallbacks{
    return BsActivityLifecycleCallbacks.getInstance()
}

fun getAuthentication():Authentication{
    return Authentication.getAuthentication()
}

fun getDbBleBean(bindInfoBean: BsGetBindInfoBean.DataBean.BindInfoBean):DbBleBean{
    val btsign = bindInfoBean.btsign
    val  bleBean = DbBleBean()
    bleBean.bikeId = TrustAppConfig.bikeId
    bleBean.mac = btsign?.bt_mac!!
    bleBean.salt = btsign.salt!!
    bleBean.userId = TrustAppConfig.userId
    bleBean.sign = btsign.sign!!
    bleBean.validation = btsign.validation!!
    bleBean.did = btsign.did!!
    bleBean.bleId = bindInfoBean.bike_info?.bleId
    TrustLogUtils.d(TAG,"btsign?.bt_mac!!:${btsign?.bt_mac!!} bindInfoBean.bike_info?.bleId:${bindInfoBean.bike_info?.bleId} ")
    return bleBean

}

fun setBtnIsclick(v:View?,isClick:Boolean,trueBackGroundResource:Int = R.drawable.white_bg_white_line_bg,falseBackGroundResource:Int = R.drawable.no_click_btn_bg){
    v?.isClickable = isClick
    v?.setBackgroundResource(if (isClick)  trueBackGroundResource else falseBackGroundResource)
}

fun getGPPopup():TrustGeneralPurposePopupwindow{
    return TrustGeneralPurposePopupwindow.getInstance()
}

fun getChannel(channelKey:String):String{
    val context = TrustAppUtils.getContext()
    return context.packageManager
            .getApplicationInfo(context.packageName,
                    PackageManager.GET_META_DATA).metaData.getString(channelKey)!!
}

fun getDevId():String{
    return "ANDROID|${TrustAndroidDeviceInfo.getPhoneModel()}|${TrustAndroidDeviceInfo.getSystemVersion()}" +
            "|${TrustAndroidDeviceInfo.getAppVersionName()}|${getDeviceId()}|${TrustAppUtils.getContext().packageName}"
}


fun getAgent():String{
    return "ANDROID|${TrustAndroidDeviceInfo.getPhoneModel()}|${TrustAndroidDeviceInfo.getSystemVersion()}" +
            "|${TrustAndroidDeviceInfo.getAppVersionName()}|${getDeviceId()}|${TrustAppUtils.getContext().packageName}"
}

fun setViewHide(v:View,isHide:Boolean){
        v.visibility = if (isHide) { View.GONE }else{ View.VISIBLE }
}



//
private  fun  <T> getDataCenterData(topic:String,bikeId: Int? = null):T?{
    return dataAnalyCenter().getData<T>(topic,bikeId)
}

//获取车辆信息
fun getCarInfoData(bikeId: Int? = null):CarInfoBean?{
    return getDataCenterData(WEB_SOKECT_RECEIVE + WEB_SOKECT_CAR_INFO,bikeId)
}

fun setCarInfoData(bean:CarInfoBean){
    TrustLogUtils.d(TAG,"set car info namg :${bean.bike_name}   id ${bean.bike_id}")
    return dataAnalyCenter().setData(WEB_SOKECT_RECEIVE+WEB_SOKECT_CAR_INFO,bean)
}

fun sendHirstort(bikeId: Int? = null){
    dataAnalyCenter().sendData(WEB_SOKECT_SEND+WEB_SOKECT_CAR_RIDE_COUNT, bikeId)
}

fun getBattryInfoData(bikeId: Int? = null):BattInfoBean.BodyBean?{
   return  dataAnalyCenter().getData<BattInfoBean>(WEB_SOKECT_RECEIVE+WEB_SOKECT_CAR_POWER,bikeId)?.body
}

private fun sendLocalData(topic:String,msg:String? = null){
    if (msg != null) {
        dataAnalyCenter().sendLocalData(topic,msg)
    }else{
        dataAnalyCenter().sendLocalData(topic)
    }
}

fun registerBleCheckPassWordSuccess(listener:DataAnalysisCenter.onDataAnalysisCallBack,tag:String){
    dataAnalyCenter().unRegisterCallBack(BLE_CHECK_PASS_WORD_SUCCESS,tag)
    dataAnalyCenter().registerCallBack(BLE_CHECK_PASS_WORD_SUCCESS, listener, tag)
}

fun registerBleDissConnection(listener:DataAnalysisCenter.onDataAnalysisCallBack,tag:String){
    dataAnalyCenter().unRegisterCallBack(BLE_CONNECT_CLOSE,tag)
    dataAnalyCenter().registerCallBack(BLE_CONNECT_CLOSE, listener, tag)
}

fun registerBattryInfo(listener:DataAnalysisCenter.onDataAnalysisCallBack,tag:String){
    dataAnalyCenter().unRegisterCallBack(WEB_SOKECT_RECEIVE+WEB_SOKECT_CAR_POWER,tag)
    dataAnalyCenter().registerCallBack(WEB_SOKECT_RECEIVE+WEB_SOKECT_CAR_POWER, listener, tag)
}

fun sendBattryInfo(bikeId: Int = TrustAppConfig.bikeId){
    dataAnalyCenter().sendData(WEB_SOKECT_SEND+WEB_SOKECT_CAR_POWER,bikeId)
}

fun sendUpdateCarInfo(bikeId:Int? = null){
    sendLocalData(APP_UPDATE_CAR_INFO,bikeId?.toString())
}

fun sendUpdateBikeList(){
    sendLocalData(APP_UPDATE_BIKE_LIST)
}

fun registerUpdateBikeList(TAG:String,callBack: DataAnalysisCenter.onDataAnalysisCallBack){
    dataAnalyCenter().unRegisterCallBack(APP_UPDATE_BIKE_LIST,TAG)
    dataAnalyCenter().registerCallBack(APP_UPDATE_BIKE_LIST,callBack,TAG)
}


fun registerUpdateCarInfo(TAG:String,callBack: DataAnalysisCenter.onDataAnalysisCallBack){
    dataAnalyCenter().unRegisterCallBack(APP_UPDATE_CAR_INFO,TAG)
    dataAnalyCenter().registerCallBack(APP_UPDATE_CAR_INFO,callBack,TAG)
}

fun regusterLocation(TAG:String,callBack: DataAnalysisCenter.onDataAnalysisCallBack){
    dataAnalyCenter().unRegisterCallBack(WEB_SOKECT_RECEIVE+WEB_SOKECT_CAR_LOCTION,TAG)
    dataAnalyCenter().registerCallBack(WEB_SOKECT_RECEIVE+WEB_SOKECT_CAR_LOCTION,callBack,TAG)
}


fun registerBikeInfo(TAG:String,callBack: DataAnalysisCenter.onDataAnalysisCallBack){
    dataAnalyCenter().unRegisterCallBack(WEB_SOKECT_SEND+WEB_SOKECT_CAR_INFO,TAG)
    dataAnalyCenter().registerCallBack(WEB_SOKECT_RECEIVE+WEB_SOKECT_CAR_INFO,callBack,TAG)
}

fun registerBikeStatus(TAG:String,callBack: DataAnalysisCenter.onDataAnalysisCallBack){
    dataAnalyCenter().registerCallBack(WEB_SOKECT_RECEIVE+WEB_CAR_STATUS,callBack,TAG)
}

fun registerHirstoryInfo(TAG:String,callBack: DataAnalysisCenter.onDataAnalysisCallBack){
    dataAnalyCenter().unRegisterCallBack(WEB_SOKECT_RECEIVE+WEB_SOKECT_CAR_RIDE_COUNT,TAG)
    dataAnalyCenter().registerCallBack(WEB_SOKECT_RECEIVE+WEB_SOKECT_CAR_RIDE_COUNT,callBack,TAG)
}

fun getBikeLocation(bikeId:Int? = null):CarLoctionBean?{
    return getDataCenterData(WEB_SOKECT_RECEIVE + WEB_SOKECT_CAR_LOCTION,bikeId)
}

fun getBikeStatus(bikeId:Int? = null):BikeStatusBean.BodyBean?{
    return getDataCenterData(WEB_SOKECT_RECEIVE + WEB_CAR_STATUS,bikeId)
}


private fun sendData(bid:Int?,topic: String){
    dataAnalyCenter().sendData(path = topic,bikeId = bid)
}



fun sendBikeLocation(bikeId: Int? = null){
    sendData(bikeId,WEB_SOKECT_SEND+WEB_SOKECT_CAR_LOCTION)
}


 fun sendBikeInfo(bikeId: Int? = null){
    sendData(bikeId,WEB_SOKECT_SEND+WEB_SOKECT_CAR_INFO)
}
fun sendBikeStatus(){
    sendData(bikeId,WEB_SOKECT_SEND + WEB_CAR_STATUS)
}


fun getDeviceId(): String {

    val dbDeviceId1 = getAppConfig().getDbDeviceId()
    if (dbDeviceId1 != null) {
        getAppConfig().setDbDeviceId(dbDeviceId1)
    }
    val registrationId = getRegistrationId(TrustAppUtils.getContext())
    if (registrationId != null) {
        getAppConfig().setDbDeviceId(registrationId)
    }

    return  getAppConfig().getDbDeviceId()!!

    val sn = TrustAndroidDeviceInfo.getSn()

    return if (sn != null && sn != "unknown") {
        getAppConfig().setDbDeviceId(sn)
        sn
    } else {
        val dbDeviceId = getAppConfig().getDbDeviceId()
        if (dbDeviceId != null) {
            return dbDeviceId
        }else{
//            val readFile = readFile()
            val readFile:String? = null
            if (readFile != null) {
                TrustLogUtils.d(TAG,"readFile () ${readFile}")
                return readFile
            }else{

//                createFile(deviceId)
                TrustLogUtils.d(TAG,"getDbDeviceId () ${getAppConfig().getDbDeviceId()}")
                return  getAppConfig().getDbDeviceId()!!
            }
        }
    }
}



fun chengTextDrawableView(v: TextDrawable,action:String){
    when (action) {
        WEB_SOKECT_CAR_BUCKET_OPEN -> {
            chang(v,R.drawable.home_controll_bucket_ic,R.drawable.home_controll_bucket_ic,
                    R.drawable.home_controll_loading_ic,"后座","后座","成功",
                    "失败","超时")
        }
        WEB_SOKECT_CAR_FIND ->{
            chang(v,R.drawable.home_controll_find_car_ic,R.drawable.home_controll_find_car_ic,
                    R.drawable.home_controll_loading_ic,"寻车","寻车","成功",
                    "失败","超时")
        }
        WEB_SOKECT_CAR_START -> {
            chang(v,R.drawable.home_controll_start_ic,R.drawable.home_controll_start_ic,
                    R.drawable.home_controll_loading_ic,"一键启动","一键启动","成功",
                    "失败","超时")
        }
        WEB_SOKECT_CAR_ACCON -> {
            chang(v,R.drawable.home_controll_acc_on_ic,R.drawable.home_controll_acc_on_ic,
                    R.drawable.home_controll_loading_ic,"开电门","开电门","成功",
                    "失败","超时")
        }
        WEB_SOKECT_CAR_ACCOFF -> {
            chang(v,R.drawable.home_controll_acc_off_ic,R.drawable.home_controll_acc_on_ic,
                    R.drawable.home_controll_loading_ic,"关电门","关电门","成功",
                    "失败","超时")
        }
        WEB_SOKECT_CAR_ALERTON -> {
            chang(v,R.drawable.home_controll_lock_close_ic,R.drawable.home_controll_lock_close_ic,
                    R.drawable.home_controll_loading_ic,"设防","设防","成功",
                    "失败","超时")
        }
        WEB_SOKECT_CAR_ALERTOFF -> {
            chang(v,R.drawable.home_controll_lock_open_ic,R.drawable.home_controll_lock_open_ic,
                    R.drawable.home_controll_loading_ic,"撤防","撤防","成功",
                    "失败","超时")
        }
        else -> {
        }
    }
}


fun chengImageView(v: ImageView){
    v.visibility = View.VISIBLE
            chang(v,R.drawable.home_controll_loading_ic,R.drawable.home_controll_loading_ic,
                    R.drawable.home_controll_loading_ic,"后座","后座","成功",
                    "失败","超时")
}



private fun chang(v: TextDrawable, oldIc:Int, newIc:Int, loadIc:Int, oldStr:String, newStr:String,
                  successStr:String, errorStr:String, timeOutStr:String){
    ControllUtils.getInstance().changeViewConfing(v, oldIc,newIc, loadIc,oldStr,newStr,
            successStr,errorStr,timeOutStr).start()
}

private fun chang(v: ImageView, oldIc:Int, newIc:Int, loadIc:Int, oldStr:String, newStr:String,
                  successStr:String, errorStr:String, timeOutStr:String){
    ControllUtils.getInstance().changeViewConfing(v, oldIc,newIc, loadIc,oldStr,newStr,
            successStr,errorStr,timeOutStr).start()
}

fun finshChangeTextDrawable(isSuccess:Boolean){
    ControllUtils.getInstance().end(isSuccess)
}

fun getIsRead():Boolean{
    return ControllUtils.getInstance().getIsRead()
}


fun changeBattryStatus(v:TextView? = null,mileage:Int? =null,txV:TextView? = null,strTv:TextView? = null,
                       bean:BattInfoBean.BodyBean.BattBean?, statusIm:ImageView? = null,
                       vStr:String? = null ,numStr:String? = null,addPowerTv:TextView? = null,
                       nothingIc:Int = R.drawable.battery_nothing_bg,
                       errorIc:Int = R.drawable.battery_error_bg,
                       addPower:Int = R.drawable.battery_add_power_bg,
                       nomorl:Int = R.drawable.battery_nomorl_bg,pos:Int = 1,
                       battinfoStr:TextView? = null,isShowTxInfo:Boolean):Int{
    var addPowerStr:String? = null
    val context = TrustAppUtils.getContext()
    var battertStatusStr = ""
    var m = if(mileage == null) 0 else mileage
    val battertBean = bean?.status
    val capacity = battertBean?.capacity?.toDouble()
    v?.text = strDoubleNum(if(capacity != null) capacity else 0.0) + "%"
    m+= if(bean != null) battertBean!!.mile_es else 0
    addPowerTv?.visibility = View.INVISIBLE
    var textColor:Int = 0
    var textTitleColor:Int = 0
    var textStr:String = ""
    var statisIc:Int = 0

    val batterInfoTxBean = if (isShowTxInfo) {
        batterInfoTx
    }else{
        homeBatterInfoTx
    }


    val status = if (bean == null) {
        statisIc = nothingIc
        BATTRY_START_NOTHING
    }else{
        val statusBean = bean.status
        if(battertBean?.faults != null && battertBean.faults.isNotEmpty()){
            statisIc = errorIc
            BATTRY_STATUS_ERROR
        }else{
            if (statusBean.charge == BATTERY_CHARGE_STATUS_OPEN) {
                addPowerTv?.visibility = View.VISIBLE
                statisIc = addPower
                BATTRY_START_ADD_POWER
            }else{
                statisIc = nomorl
                BATTRY_START_NOMORL
            }
        }
    }
    val position = bean?.info?.position
    val cycle = bean?.info?.cycle
    when (status) {
        BATTRY_START_NOMORL -> {
            changeViewAlpha(v, txV,isShowTxInfo)
            textStr = getBsString(batterInfoTxBean.nomorl,position.toString())
            battertStatusStr = setTextStrings(R.string.batteryinfo_nomorl_tx,position.toString(),cycle.toString())
            if(capacity != null && capacity <= 20){
                textColor = getBsColor(R.color.colorRead)
            }else if(capacity != null && capacity <= 20){
                textColor = getBsColor(R.color.colorWhiteGay)
            }else{
                textColor = getBsColor(R.color.colorWhiteGay)
            }
            textTitleColor = textColor
        }
        BATTRY_START_ADD_POWER -> {
            changeViewAlpha(v, txV,isShowTxInfo)
            addPowerStr = setTextStrings(R.string.add_power_msg_tx,bean!!.info.position.toString(),battertBean!!.charge_es.toString())
            textStr = getBsString(batterInfoTxBean.addPower)
            battertStatusStr = setTextStrings(R.string.batteryinfo_add_power_tx,position.toString())
            textColor = getBsColor(R.color.greenf55)
            textTitleColor = textColor
        }
        BATTRY_START_NOTHING -> {
            changeViewAlpha(v, txV,isShowTxInfo,true)
            textStr = getBsString(batterInfoTxBean.nothing)
            battertStatusStr = setTextStrings(R.string.batteryinfo_nothing_tx,pos.toString())
            textColor = getBsColor(R.color.color_ffea00)
            textTitleColor = textColor
        }
        BATTRY_STATUS_ERROR -> {
            changeViewAlpha(v, txV,isShowTxInfo)
            textStr = getBsString(batterInfoTxBean.error)
            battertStatusStr = setTextStrings(R.string.batteryinfo_error_tx,position.toString())
            textColor = getBsColor(R.color.orange700)
            textTitleColor = textColor
        }
    }


        v?.setTextColor(textColor)

    if (vStr != null) {
        val toString = v?.text.toString()
        v?.text = toString + vStr
    }
    txV?.setTextColor(textTitleColor)
    txV?.text = textStr
    strTv?.text = numStr
    if (pos == 2) {
        battertStatusStr = battinfoStr?.text.toString() + "\n" + battertStatusStr
    }
    battinfoStr?.text = battertStatusStr
    statusIm?.setImageResource(statisIc)
    addPowerTv?.text = addPowerStr
    return m
}


fun changeBatteryInfoUi(powerNumTv:TextView,battery1_tem_text_tv:TextView
                        ,battery1_add_power_tv:TextDrawable,bean:BattInfoBean.BodyBean.BattBean?,
                        batteryStatisIc:ImageView,bg:BatteryBgView,titleLayout:View?,battery_status_tv:TextView):Int{
    val battertBean = bean?.status
    var statusIc:Int
    var viewBgColor:Int = Color.RED

    val status = if (bean == null) {
        statusIc = R.drawable.battery_ic_nothing
        battery1_add_power_tv.visibility = View.INVISIBLE
        BATTRY_START_NOTHING
    }else{
        val statusBean = bean.status
        if(battertBean?.faults != null && battertBean.faults.isNotEmpty()){
            statusIc = R.drawable.battery_ic_error
            battery1_add_power_tv.visibility = View.INVISIBLE
            BATTRY_STATUS_ERROR
        }else{
            if (statusBean.charge == BATTERY_CHARGE_STATUS_OPEN) {
                battery1_add_power_tv.visibility = View.VISIBLE
                battery1_add_power_tv.text = setTextStrings(R.string.add_power_msg_tx,bean!!.info.position.toString(),battertBean!!.charge_es.toString())
                statusIc = R.drawable.battery_ic_add_pwder
                BATTRY_START_ADD_POWER
            }else{
                statusIc = R.drawable.battery_ic_nomrl
                battery1_add_power_tv.visibility = View.INVISIBLE
                BATTRY_START_NOMORL
            }
        }
    }

    val capacity = battertBean?.capacity
    TrustLogUtils.d(TAG,"电池电量 $capacity")
    when (status) {
        BATTRY_START_NOMORL -> {
            if(capacity != null && capacity <= 20){
                statusIc = R.drawable.battery_ic_low_power
                viewBgColor = getBsColor(R.color.colorRead)
                battery_status_tv.text = getBsString(homeBatterInfoTx.statusLowPowerTx)
            }else if(capacity != null && capacity <= 20){
                battery_status_tv.text = getBsString(homeBatterInfoTx.statusNomrlTx)
                viewBgColor = getBsColor(R.color.blue0ff)
            }else{
                battery_status_tv.text = getBsString(homeBatterInfoTx.statusNomrlTx)
                viewBgColor = getBsColor(R.color.blue0ff)
            }

        }
        BATTRY_START_ADD_POWER -> {
            battery_status_tv.text = getBsString(homeBatterInfoTx.statusAddPower)
            viewBgColor = getBsColor(R.color.greenf55)
        }
        BATTRY_START_NOTHING -> {
            battery_status_tv.text = getBsString(homeBatterInfoTx.statusNothingTx)
            viewBgColor = getBsColor(R.color.color_9298a0)
        }
        BATTRY_STATUS_ERROR -> {
            battery_status_tv.text = getBsString(homeBatterInfoTx.statusErrorTx)
            viewBgColor = getBsColor(R.color.orange700)
        }
        else ->{

        }
    }

    bg.setBgColor(viewBgColor)
    battery_status_tv.setTextColor(viewBgColor)
    titleLayout!!.setBackgroundColor(viewBgColor)
    TrustLogUtils.d(TAG,"需要显示的电量 $capacity")
    powerNumTv.text = "${capacity ?:0}%"
    battery1_tem_text_tv.text = getBsString(R.string.battery_tem_num_tx,battertBean?.temp.toString())
    batteryStatisIc.setImageResource(statusIc)
    return  if(bean != null) battertBean!!.mile_es else 0
}


private fun changeViewAlpha(v: TextView?, txV: TextView?,isShowInfo:Boolean,isAlpha:Boolean = false) {
    val alpha = if (isAlpha && !isShowInfo) {
        0.5f
    }else{ 1f }
    v?.alpha = alpha
    txV?.alpha = alpha
}

private val homeBatterInfoTx = BatterInfoTx(R.string.battery_nomorl_tx,R.string.battery_nothing_tx,
        R.string.battery_error_tx,R.string.battery_add_power_tx
        ,R.string.battery_status_nomrl_tx,R.string.battery_status_error_tx
        ,R.string.battery_status_add_power_tx,R.string.battery_status_nothing_tx,R.string.battery_status_low_power_tx)

private val batterInfoTx = BatterInfoTx(R.string.battery_nomorl_info_tx,R.string.battery_nothing_tx,
        R.string.battery_error_tx,R.string.battery_add_power_tx
        ,R.string.battery_status_nomrl_tx,R.string.battery_status_error_tx
        ,R.string.battery_status_add_power_tx,R.string.battery_status_nothing_tx,R.string.battery_status_low_power_tx)

private class BatterInfoTx(var nomorl:Int,var nothing:Int,var error:Int,var addPower:Int
                           ,var statusNomrlTx:Int,var statusErrorTx:Int,var statusAddPower:Int,
                           var statusNothingTx:Int,var statusLowPowerTx:Int)


fun checkIsLogin():Boolean{
    if (!getAuthentication().getUserLoginStatus()) {
        arouterStartActivity(ROUTE_PATH_LOGING)
        return false
    }
    return true
}

fun userPrivateStart(v:TextView, color:Int, context: Context, checkBox: CheckBox) {
    checkBox.isChecked = true
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        v.text = setHtmlSpanneds(R.string.login_fragment_bind_phone_layout_protocol, "", callBack = SizeLabel.HtmlOnClickListener { action ->
            if (action == "0") {
                intentProtocol(context,URL_USER_SERVICES_AGREEMENT)
            } else {
                intentProtocol(context,URL_PRIVACY_POLICY)
            }
        }, color = color)
    }

    v.movementMethod = LinkMovementMethod.getInstance()
    v.highlightColor = Color.parseColor("#00000000")
}

/**
 * 跳转协议页面
 */
private fun intentProtocol(context: Context,type:String){
    val intent = Intent(context, UserProtocolActivity::class.java)
    intent.putExtra(WEB_INTENT_USER_TYPE_KEY,type)
    context?.startActivity(intent)
}


fun getString(stringId:Int):String{
    return TrustAppUtils.getContext().getString(stringId)
}


fun showMapAddress(bean:CarLoctionBean.BodyBean):String{
    val TAG = "showMapAddress"
    val gps = bean.gps
    val province = gps.province
    val city = gps.city
    val district = gps.district
    val details = gps.details
    val desc = gps.desc

    var address = StringBuffer()
    address.insert(0,details)
     if (!checkLengthIsOk(address)) {
        address.insert(0,district)
        if (!checkLengthIsOk(address)) {
            address.insert(0,city)
            if (!checkLengthIsOk(address)) {
                address.insert(0,province)
            }
            address
        }else{
            address
        }
    }else{
        address
    }
    return if(address.length >14){
        address.substring(0, 14) + "..."
    }else{
        address.toString()
    }
}

private fun checkLengthIsOk(msg:StringBuffer):Boolean{
    return msg.length >= 14
}

 fun getFragmentMyCar(): FragmentMyCar? {
    val fragmentMyCar =
            TrustMVPFragment.mFragmentManger[FragmentMyCar::class.java.canonicalName]
    if (fragmentMyCar != null) {
        return fragmentMyCar as FragmentMyCar
    }
    return null
}


fun updateShowFragmentUi(){
    val controllCar = TrustMVPFragment.mFragmentManger[FragmentHomeControlCard::class.java.canonicalName]
    if (controllCar != null) {
        controllCar as FragmentHomeControlCard
        controllCar.updateFragmentUi()
    }
    val homeMapCard = TrustMVPFragment.mFragmentManger[FragmentHomeMapCard::class.java.canonicalName]
    if (homeMapCard != null) {
        homeMapCard as FragmentHomeMapCard
        homeMapCard.updateFragmentUi()
    }
    val homeHistoryCard = TrustMVPFragment.mFragmentManger[FragmentHomeHistoryCard::class.java.canonicalName]
    if (homeHistoryCard != null) {
        homeHistoryCard as FragmentHomeHistoryCard
        homeHistoryCard.updateFragmentUi()
    }
}


fun strDoubleNum(str:Double):String{ return TrustStringUtils.strDoubleNum(str) }

fun strDouble(str:Double):String{ return TrustStringUtils.strDouble(str) }

fun getMileage(mileage:Int):String{ return strDoubleNum(mileage.toDouble()) }

fun getMileageDouble(mileage:Int):String{
    return strDouble(mileage / 1000.0)
}

fun getTime(time:Int):String{
   return  strDouble(time / 60.0  / 60.0 )
}

fun getSpeed(speed:Int):String{
    return strDouble(speed / 1000.0 )
}

fun getHMS(time:Int):String{
    return NumFormat((time / 60 / 60).toLong()) + ":" + NumFormat((time / 60 % 60).toLong()) + ":" + NumFormat((time % 60).toLong())
}

private fun NumFormat(i: Long): String {
    return if (i.toString().length < 2) {
        "0$i"
    } else {
        i.toString()
    }
}

fun extUser(){
    getAuthentication().allUserExt()
    bleDisConnection()
    mBsApplication?.getService()?.getWebSocket()?.disConnect()
    MainHomeActivity.mFragmentJumpType = DEFUTE
    BsActivityLifecycleCallbacks.getInstance().getMainHomeActivity().updateFragment(Intent())
}


fun webSocketdissconnection(){
    mBsApplication?.getService()?.getWebSocket()?.disConnect()
}

fun getExtsBean(exts:String?): WebAlertBean.BodyBean.ExtsBean?{
    if (exts != null) {
        return TrustAnalysis.resultTrustBean(exts,WebAlertBean.BodyBean.ExtsBean::class.java)
    }else{
        return null
    }
}


/**
 * toast 自定义
 */
fun  showToastFree( ctx:Context, str:String):Toast{
    val toast = Toast.makeText(ctx, str, Toast.LENGTH_SHORT);
    val toastView =
    LayoutInflater.from(ctx).inflate(R.layout.toast_layout, null);
    val tv = toastView.findViewById<TextView>(R.id.toast_msg_tv)
    tv.text = str;
    toast.view = toastView;
    toast.show();
    return toast;
}



/**
 * 根据textview 宽度自动设置字体大小
 */
fun  adjustTvTextSize( tv:TextView?, maxWidth:Int, text:String? = null,textSpanned:Spanned? = null):Float {
    if (tv != null) {
        val avaiWidth = maxWidth - tv.getPaddingLeft() - tv.getPaddingRight() - 10;

        if (avaiWidth <= 0 || TextUtils.isEmpty(text) || TextUtils.isEmpty(textSpanned)) {
            return tv.getPaint().getTextSize();
        }

        val textPaintClone =  TextPaint(tv.getPaint());
        // note that Paint text size works in px not sp
        var trySize = textPaintClone.getTextSize();

        if (text != null) {
            while (textPaintClone.measureText(text) > avaiWidth) {
                trySize--;
                textPaintClone.setTextSize(trySize);
            }
        }else if (textSpanned != null){
            while (textPaintClone.measureText(textSpanned.toString()) > avaiWidth) {
                trySize--;
                textPaintClone.setTextSize(trySize);
            }
        }else{ }

        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, trySize)
        return trySize
    }else{
        return 0f
    }

}

fun getApplicetion():BsApplication{
    return  BsApplication.mBsApplication!!
}



fun getBsString(textView:TextView,stringId:Int,msg:String = ""){
    textView.text = getBsString(stringId,msg)
}


fun getBsString(stringId:Int,msg:String  = ""):String{
    return getApplicetion().getString(stringId,msg)
}

fun getBsColor(colorInt:Int):Int{
    val context = TrustAppUtils.getContext()
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.resources.getColor(colorInt,null)
    }else{
        context.resources.getColor(colorInt)
    }
}



 fun checkStringIsNullAndShowMsg(msg:String?,errorMsg:String):Boolean{
    return if (!TextUtils.isEmpty(msg)) {
        false
    }else{
        showToast(errorMsg)
        true
    }
}


fun formatPhone(phone:String):String{
    val sb = StringBuilder(phone)
    var str = "****"
    sb.replace(3, 7, str)
    return sb.toString()
}


fun sendLocationBleEletrion(errorString:String?){
    dataAnalyCenter().sendLocalData(BLE_ELETRION,errorString)
}

fun sendLocationBleCushionInduction(errorString:String?){
    dataAnalyCenter().sendLocalData(BLE_CUSHION_INDUCTION,errorString)
}

fun registerBleEletrion(tag: String,listener: DataAnalysisCenter.onDataAnalysisCallBack){
    dataAnalyCenter().unRegisterCallBack(BLE_ELETRION,tag)
    dataAnalyCenter().registerCallBack(BLE_ELETRION,listener,tag)
}

fun registerBleCushionInduction(tag: String,listener: DataAnalysisCenter.onDataAnalysisCallBack){
    dataAnalyCenter().unRegisterCallBack(BLE_CUSHION_INDUCTION,tag)
    dataAnalyCenter().registerCallBack(BLE_CUSHION_INDUCTION,listener,tag)
}



fun registerBleBikeInfo(tag:String,listener: DataAnalysisCenter.onDataAnalysisCallBack){
    dataAnalyCenter().unRegisterCallBack(BLE_CONTROLL_BIKE_INFO,tag)
    dataAnalyCenter().registerCallBack(BLE_CONTROLL_BIKE_INFO,listener,tag)
}


fun isDemoStatus():Boolean{
    return  TrustAppConfig.USE_TYPE == USE_DEMO
}


fun getTodayZero(time:Long) :Long{
    val date =  Date(time);
    val l = 24*60*60*1000; //每天的毫秒数
    //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
    //减8个小时的毫秒值是为了解决时区的问题。
    return (date.getTime() - (date.getTime()%l) - 8* 60 * 60 *1000);
}


