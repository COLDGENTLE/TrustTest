package com.sharkgulf.soloera.presenter

import com.sharkgulf.soloera.module.HttpModel
import com.sharkgulf.soloera.module.UtilsListener
import com.sharkgulf.soloera.module.bean.BsAppVersionInfoBean
import com.sharkgulf.soloera.mvpview.UtilsView
import com.trust.demo.basis.base.ModuleResultInterface
import com.trust.demo.basis.base.presenter.TrustPresenters

/**
 *  Created by user on 2019/3/13
 */
class UtilsPresenters:TrustPresenters<UtilsView>(),UtilsPresentersListener {
    private var mMode :UtilsListener? = null

    init {
        mMode = HttpModel()
    }



    override fun requestAppVersion(map: HashMap<String, Any>) {
        mMode?.requestAppVersion(map,object :ModuleResultInterface<BsAppVersionInfoBean>{
            override fun resultData(bean: BsAppVersionInfoBean?,pos:Int?) {
                view?.diassDialog()
                view?.resultAppVersionInfo(bean)
            }

            override fun resultError(msg: String) {
                view?.diassDialog()
                view?.resultError(msg)
            }

        })
    }
}