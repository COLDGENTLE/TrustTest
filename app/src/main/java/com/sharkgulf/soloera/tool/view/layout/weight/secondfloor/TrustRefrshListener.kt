package com.sharkgulf.soloera.tool.view.layout.weight.secondfloor

/*
 *Created by Trust on 2019/1/9
 */
interface TrustRefrshListener {
    /**
     * 当处于SecondFloorView，页面的显示效果
     */
    fun onRefreshingSecondFloorViewState()

    /**
     * 当处于headView，页面的显示效果
     */
    fun onRefreshingHeadViewState()


    /**
     * 默认状态时，页面显示效果，主要是为了复位各种状态
     */
    fun onDefaultState()
}