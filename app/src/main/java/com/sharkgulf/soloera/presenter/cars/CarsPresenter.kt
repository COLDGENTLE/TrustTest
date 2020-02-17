package com.sharkgulf.soloera.presenter.cars

import com.sharkgulf.soloera.module.HttpModel
import com.sharkgulf.soloera.module.bean.BsGetCarInfoBean
import com.sharkgulf.soloera.module.home.HomeModeListener
import com.sharkgulf.soloera.mvpview.cars.ICars
import com.trust.demo.basis.base.ModuleResultInterface
import com.trust.demo.basis.base.presenter.TrustPresenters

/**
 *  Created by user on 2019/3/7
 */
class CarsPresenter:TrustPresenters<ICars>() ,ICarsPresenterListener{

    private var mode: HomeModeListener? = null

    init {
        mode = HttpModel()
    }


    override fun requestCarInfo(map: HashMap<String, Any>?) {
        mode?.getCarInfo(map!!,object :ModuleResultInterface<BsGetCarInfoBean>{
            override fun resultData(bean: BsGetCarInfoBean?,pos:Int?) {
                view.diassDialog()
                view.resultCarInfo(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }

        })
    }
}