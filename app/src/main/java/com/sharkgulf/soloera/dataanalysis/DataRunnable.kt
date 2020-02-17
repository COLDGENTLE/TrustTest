package com.sharkgulf.soloera.dataanalysis

import com.sharkgulf.soloera.TrustAppConfig.*
import io.reactivex.Observable
import org.json.JSONObject
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 *  Created by user on 2019/8/7
 */
class DataRunnable(val data:String?):Runnable {
    override fun run() {
        if (data == null) {
            return
        }
        filterType()
    }

    private fun filterType(){
            Observable.create(ObservableOnSubscribe<String> { emitter ->
                val jsonObject = JSONObject(data)
                val header = JSONObject(jsonObject.getString("header"))
                val ack = header.getInt("ack")
                val path = jsonObject.getString("path")
                if (ack==IS_ANSWER ) {
                    DataAnalysisCenter.getInstance().sendAnswer("$WEB_SOKECT_CACK$path",
                            header.getString("uuid")
                            ,NO_ANSWER)
                }
                emitter.onNext(path)
            }).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<String> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: String) {
                    DataDistribution.getInstance().updateData(t,data!!)
                }

                override fun onError(e: Throwable) {
                }

            })






    }
}

