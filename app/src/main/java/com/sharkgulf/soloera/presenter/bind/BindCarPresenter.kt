package com.sharkgulf.soloera.presenter.bind

import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.module.bind.BindCarMode
import com.sharkgulf.soloera.module.bind.BindCarModeListener
import com.sharkgulf.soloera.mvpview.bind.IBindView
import com.sharkgulf.soloera.tool.config.DEFULT
import com.sharkgulf.soloera.tool.config.bleReadDeviceStatus
import com.trust.demo.basis.base.ModuleResultInterface
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.TrustAppUtils
import com.trust.demo.basis.trust.utils.TrustLogUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/*
 *Created by Trust on 2019/1/5
 */
class BindCarPresenter: TrustPresenters<IBindView>() ,IBindCarPresenterListener{
    override fun updatePhone(hashMap: HashMap<String, Any>) {}

    override fun phoneLogin(map: HashMap<String, Any>) {}

    private  var bindMode : BindCarModeListener? = null
    private val TAG = BindCarPresenter::class.java.canonicalName
    init {
        bindMode = BindCarMode()
    }

    fun getDevicesInfo(){
        stopTiming()
        val context = TrustAppUtils.getContext()
        var msg:String? = null

        Observable.create(ObservableOnSubscribe<Int> { emitter ->
            val bleReadDeviceStatus = bleReadDeviceStatus()
            if (bleReadDeviceStatus != null) {
                emitter.onNext(bleReadDeviceStatus)
            }else{
                emitter.onNext(DEFULT)
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Int> {
                    override fun onComplete() {}

                    override fun onSubscribe(d: Disposable) { mDisposable = d}

                    override fun onNext(t: Int) {
                        val result = when (t) {
                            TrustAppConfig.BIKE_STATUS_DEFUTE -> {
                                msg = context.getString(R.string.no_bind_tx)
                                false
                            }
                            TrustAppConfig.BIKE_STATUS_ADDED -> {
                                msg = context.getString(R.string.bind_tx)
                                false
                            }
                            TrustAppConfig.BIKE_STATUS_ADDING -> {
                                msg = context.getString(R.string.bind_ing_tx)
                                false
                            }
                            TrustAppConfig.BIKE_STATUS_READY_ADD -> {
                                true
                            }
                            DEFULT -> {
                                msg = "读取设备状态异常"
                                false
                            }
                            else -> {
                                msg = "未知 $t"
                                false
                            }
                        }
                        TrustLogUtils.d(TAG,"msg : $msg")
                        view.resultDeviceStatus(result,msg)
                    }
                    override fun onError(e: Throwable) {}
                })




    }


    override fun requestBindCar(hashMap: HashMap<String, Any>?) {
        bindMode!!.requestBindCar(hashMap,object :ModuleResultInterface<BsBindCarBean>{
            override fun resultData(bean: BsBindCarBean?,pos:Int?) {
                view.diassDialog()
                view.resultBindCar(bean)
            }

            override fun resultError(msg: String) {
                view.resultError(msg)
                view.diassDialog()
            }

        })
    }



    override fun requestGetBleSign(hashMap: HashMap<String, Any>?) {
        bindMode!!.requestGetBleSign(hashMap,object :ModuleResultInterface<BsBleSignBean>{
            override fun resultData(bean: BsBleSignBean?,pos:Int?) {
                view.diassDialog()
                view.resultGetBleSign(bean)
            }

            override fun resultError(msg: String) {
                view.resultError(msg)
                view.diassDialog()
            }

        })
    }

    override fun requestUpdateBindStatus(hashMap: HashMap<String, Any>?) {
        bindMode!!.requestUpdateBindStatus(hashMap,object :ModuleResultInterface<BsUpdateBindStatusBean>{
            override fun resultData(bean: BsUpdateBindStatusBean?,pos:Int?) {
                view.diassDialog()
                view.resultUpdateBindStatus(bean)
            }

            override fun resultError(msg: String) {
                view.resultError(msg)
                view.diassDialog()
            }
        })
    }

    override fun requestUpdateCarInfo(hashMap: HashMap<String, Any>?,carInfoBean: CarInfoBean?) {
        bindMode!!.requestUpdateCarInfo(hashMap,object :ModuleResultInterface<BsUpdateCarInfoBean>{
            override fun resultData(bean: BsUpdateCarInfoBean?,pos:Int?) {
                view.diassDialog()
                view.resultUpdateCarInfo(bean)
            }

            override fun resultError(msg: String) {
                view.resultError(msg)
                view.diassDialog()
            }
        })
    }

    override fun requestGetBindInfo(hashMap: HashMap<String, Any>?) {
        bindMode?.requestGetBindInfo(hashMap,object :ModuleResultInterface<BsGetBindInfoBean>{
            override fun resultData(bean: BsGetBindInfoBean?,pos:Int?) {
                view.diassDialog()
                view.resultGetBindInfo(bean)
            }

            override fun resultError(msg: String) {
                view.resultError(msg)
                view.diassDialog()
            }
        })
    }

    private var mDisposable: Disposable? = null

    override fun startTiming() {
        stopTiming()
        mDisposable = TrustTools<View>().setCountdown(120) {
            view.showSeachBtn()
        }
    }

    override fun stopTiming() {
        mDisposable?.dispose()
    }
}