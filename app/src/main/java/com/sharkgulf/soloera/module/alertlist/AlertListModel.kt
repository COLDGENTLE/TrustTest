package com.sharkgulf.soloera.module.alertlist

import com.sharkgulf.soloera.module.bean.BsAlertBean
import com.sharkgulf.soloera.tool.config.DEFULT
import com.trust.demo.basis.base.ModuleResultInterface
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AlertListModel :IAlertListModelListener {
    private var mListener: ModuleResultInterface<ArrayList<AlertListBean>>? = null
    override fun filerData(bean: BsAlertBean.DataBean, listener: ModuleResultInterface<ArrayList<AlertListBean>>) {
        val list = bean.list
        mListener = listener
        var newList = arrayListOf<AlertListBean>()
        if (list != null && list.isNotEmpty()) {
            filer(list)
        }else{
            listener.resultData(newList)
        }
    }


    private fun filer(list:List<BsAlertBean.DataBean.ListBean>){
        Observable.create(ObservableOnSubscribe<ArrayList<AlertListBean>> { emitter ->
            val mMinute = 60 * 15
            val alert = arrayListOf<AlertListBean>()
            var alertListBean = AlertListBean()
            list.forEachIndexed { index, listBean ->
                if(alertListBean.event == DEFULT && alertListBean.bid == DEFULT){
                    alertListBean.event = listBean.event
                    alertListBean.bid = listBean.bid
                    alertListBean.list.add(listBean)
                    alertListBean.ts = listBean.ts
                }else if (alertListBean.event == listBean.event && alertListBean.bid == listBean.bid &&
                        alertListBean.ts - listBean.ts <= mMinute) {
                    alertListBean.list.add(listBean)
                }else{
                    alert.add(alertListBean)
                    alertListBean = AlertListBean()
                    alertListBean.event = listBean.event
                    alertListBean.bid = listBean.bid
                    alertListBean.list.add(listBean)
                    alertListBean.ts = listBean.ts
                    if (index == list.size-1) {
                        alert.add(alertListBean)
                    }
                }
            }

            emitter.onNext(alert)
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ArrayList<AlertListBean>> {
                    override fun onComplete() {}

                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(list: ArrayList<AlertListBean>) { mListener?.resultData(list) }

                    override fun onError(e: Throwable) {mListener?.resultError(e.toString())}

                })

    }

    class AlertListBean(var event:Int = DEFULT,var bid:Int = DEFULT,var ts:Int = DEFULT,var list: ArrayList<BsAlertBean.DataBean.ListBean> = arrayListOf())
}