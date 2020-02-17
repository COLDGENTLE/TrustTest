package com.sharkgulf.soloera.presenter.messager

import com.sharkgulf.soloera.db.bean.DbAlertBean
import com.sharkgulf.soloera.module.DbModel
import com.sharkgulf.soloera.module.DbModelListener
import com.sharkgulf.soloera.module.HttpModel
import com.sharkgulf.soloera.module.HttpModelListener
import com.sharkgulf.soloera.module.alertlist.AlertListModel
import com.sharkgulf.soloera.module.alertlist.IAlertListModelListener
import com.sharkgulf.soloera.module.bean.BsAlertBean
import com.sharkgulf.soloera.mvpview.message.MessagerView
import com.sharkgulf.soloera.tool.config.DEFULT
import com.trust.demo.basis.base.ModuleResultInterface
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.TrustLogUtils

/**
 *  Created by user on 2019/9/9
 */
class MessagerPresenter :TrustPresenters<MessagerView>(),IMessagerPresenterListener {
    private val TAG = MessagerPresenter::class.java.canonicalName
    private var mHttpModel:HttpModelListener? = null
    private var mDbModel :DbModelListener? = null
    private var mAlertListListener:IAlertListModelListener? = null
    init {
        mHttpModel = HttpModel()
        mDbModel = DbModel()
        mAlertListListener = AlertListModel()
    }


    override fun requestAlertList(map: HashMap<String, Any>) {
        view.showWaitDialog("",true,"")
        mHttpModel?.requestAlertList(map,object :ModuleResultInterface<BsAlertBean>{
            override fun resultData(bean: BsAlertBean?,pos:Int?) {

                if (map["date"].toString() == TrustTools.getTime(System.currentTimeMillis())) { setDb(map["date"].toString(),bean!!) }
                view.diassDialog()
                view.resultAlertList(bean)
//                    if (bean != null) {
//                        if (BeanUtils.checkSuccess(bean.state!!,bean.state_info!!,null)) {
//                            filerData(bean)
//                        }else{
//                            view.diassDialog()
//                            showBsToast(bean.state_info)
//                        }
//                    }
                }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }

        })
    }

    private fun filerData(bean: BsAlertBean) {
        mAlertListListener?.filerData(bean.data, object : ModuleResultInterface<ArrayList<AlertListModel.AlertListBean>> {

            override fun resultData(bean: ArrayList<AlertListModel.AlertListBean>?, pos: Int?) {
                view.diassDialog()
                if (bean != null) {
                    bean.forEach {
                        TrustLogUtils.d(TAG,"---------------------------")
                        it.list.forEach {
                            TrustLogUtils.d(TAG,"小类 ${it.name} |ts: ${it.ts}  | event :${it.event} | it uuid  ${it.uuid}")
                        }
                    }


                    TrustLogUtils.d(TAG,"bean.size ${bean.size}")
                }else{
                    TrustLogUtils.d(TAG,"bean.size is  no !!!")
                }
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }
        })
    }

    private fun setDb(day:String,bean: BsAlertBean){
        mDbModel?.putAlertBean(DEFULT,day,bean)
    }

    override fun getAlertList(map: HashMap<String, Any>) {
        mDbModel?.getAlertList(map["bike_id"] as Int,map["date"].toString(),map["is_read"] as Boolean,object :ModuleResultInterface<DbAlertBean>{
            override fun resultData(bean: DbAlertBean?,pos:Int?) {
                view.diassDialog()
                view.resultBsAlertList(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }
        })
    }
}