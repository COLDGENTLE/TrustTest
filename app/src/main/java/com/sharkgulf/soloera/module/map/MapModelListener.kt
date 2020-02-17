package com.sharkgulf.soloera.module.map

import com.sharkgulf.soloera.module.bean.BsGetCarLocationBean
import com.trust.demo.basis.base.ModuleResultInterface

/*
 *Created by Trust on 2018/12/26
 */
interface MapModelListener {
    fun requestCarInfo(map:HashMap<String,Any>,moduleResultInterface:ModuleResultInterface<BsGetCarLocationBean>)
}