package com.sharkgulf.soloera.tool.view

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import com.sharkgulf.soloera.R
import com.trust.demo.basis.trust.utils.TrustAppUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 *  Created by user on 2019/11/9
 */
class ControllUtils {
    companion object{
        private var mControllUtils:ControllUtils? = null
        fun getInstance():ControllUtils{
            if (mControllUtils == null) {
                mControllUtils = ControllUtils()
            }
            return mControllUtils!!
        }
    }

    private val READ = 0
    private val LOAD = 1

    private var status = READ
    private var isRun = false
    private var mDisposable:Disposable? = null
    private var mV:TextDrawable? = null
    private var mOldIc:Int? = null
    private var mNewIc:Int? = null
    private var mLoadIc:Int? = null
    private var mOldStr:String? = null
    private var mNewStr:String? = null
    private var mSuccessStr:String? = null
    private var mErrorStr:String? = null
    private var mTimeOutStr:String? = null
    private val TYPE_TEXT_DRAWABLE = 1
    private val TYPE_IMGVIEW = 2
    private var mType = TYPE_TEXT_DRAWABLE
    private var mImagerView:ImageView? = null
    fun changeViewConfing(v:TextDrawable,oldIc:Int,newIc:Int,loadIc:Int,oldStr:String,newStr:String,
                          successStr:String,errorStr:String,timeOutStr:String):ControllUtils{
        mType = TYPE_TEXT_DRAWABLE
        if (status == READ) {
            mV = v
            mOldIc = oldIc
            mNewIc = newIc
            mLoadIc = loadIc
            mOldStr = oldStr
            mNewStr = newStr
            mSuccessStr = successStr
            mErrorStr = errorStr
            mTimeOutStr = timeOutStr
        }
        return this
    }

    fun changeViewConfing(v:ImageView,oldIc:Int,newIc:Int,loadIc:Int,oldStr:String,newStr:String,
                          successStr:String,errorStr:String,timeOutStr:String):ControllUtils{
        mType = TYPE_IMGVIEW
        if (status == READ) {
            mImagerView = v
            mOldIc = oldIc
            mNewIc = newIc
            mLoadIc = loadIc
            mOldStr = oldStr
            mNewStr = newStr
            mSuccessStr = successStr
            mErrorStr = errorStr
            mTimeOutStr = timeOutStr
        }
        return this
    }

    fun start(){
        if (status == READ) {
            status = LOAD
            load()
        }
    }


    @SuppressLint("CheckResult", "ResourceType")
    private fun load(){
        if(mType == TYPE_TEXT_DRAWABLE){
            mV?.setDrawableTop(R.anim.rotate_anime)
        }else{
            mImagerView?.setImageDrawable(TrustAppUtils.getContext().getResources().getDrawable(R.anim.rotate_anime,null))
        }
        isRun = true
        var num = 0
        Observable.create(ObservableOnSubscribe<Int> { emitter ->
            try {
                while (isRun){
                    Thread.sleep(20)
                    num+= 100
                    emitter.onNext(num)
                }
            }catch (e:InterruptedException ){

            }


        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Int> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {}
                    override fun onSubscribe(d: Disposable) { mDisposable = d }
                    override fun onNext(t: Int) {
                        if(mType == TYPE_TEXT_DRAWABLE){
                            mV?.compoundDrawables?.forEach { it?.level = num }
                        }else{
                            mImagerView?.drawable?.level = num
                        }

                       }
                })

    }

     fun end(isSuccess:Boolean){
        isRun = false
        mDisposable?.dispose()
         if(mType == TYPE_TEXT_DRAWABLE){
             mV?.setDrawableTop(if(isSuccess) mNewIc!! else mOldIc!!)
         }else{
             mImagerView?.setImageResource(if(isSuccess) mNewIc!! else mOldIc!!)
             mImagerView?.visibility = View.GONE
         }

        status = READ
    }


    fun getIsRead():Boolean{
        return status == READ
    }

}