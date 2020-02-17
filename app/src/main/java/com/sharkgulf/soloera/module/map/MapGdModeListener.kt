package com.sharkgulf.soloera.module.map

import com.sharkgulf.soloera.presenter.map.IMapPresenterListener
import com.bs.trust.mapslibrary.gd.GdMapTool

/*
 *Created by Trust on 2018/12/26
 */
interface MapGdModeListener {
    fun getAddressName(lat:Double,lng:Double,addressListerner: GdMapTool.onAddressListerner)

    fun getUserLocation(mapListener: IMapPresenterListener.MapListener)
}