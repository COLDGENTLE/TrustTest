package com.sharkgulf.soloera.module.alertlist

import com.sharkgulf.soloera.module.bean.BsAlertBean
import com.trust.demo.basis.base.ModuleResultInterface

interface IAlertListModelListener {

    fun filerData(bean: BsAlertBean.DataBean,listener:ModuleResultInterface<ArrayList<AlertListModel.AlertListBean>>)
}
