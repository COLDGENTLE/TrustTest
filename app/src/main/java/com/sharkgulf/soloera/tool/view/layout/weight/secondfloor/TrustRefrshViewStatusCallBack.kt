package com.sharkgulf.soloera.tool.view.layout.weight.secondfloor

import android.os.Build
import androidx.annotation.RequiresApi
import android.view.View

/*
 *Created by Trust on 2019/1/9
 */
class TrustRefrshViewStatusCallBack : TrustRefrshStatusCallBack {
    private var mHeadView :View? = null
    private var mSecondFloorView :View? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(mTrustRefrshLayout: TrustRefrshLayout) {
        mHeadView = mTrustRefrshLayout.getHeadView()
        mSecondFloorView = mTrustRefrshLayout.getSecondFloorView()
    }

    /**
     * 当headView处于下拉时，显示效果
     *
     * @param scrollY        下拉的距离
     * @param headviewHeight 头布局高度
     * @param deltaY         moveY-lastMoveY,正值为向下拉
     */
    override fun onPullDownHeadViewRefreshState(scrollY: Int, headViewHight: Int, deltaY: Int) {
        val f = -(scrollY.toFloat() / headViewHight.toFloat())

    }

    /**
     * 当headView处于松手马上显示时，显示效果
     *
     * @param scrollY 下拉的距离
     * @param deltaY  moveY-lastMoveY,正值为向下拉
     */
    override fun onReleaseHeadViewRefreshState(scrollY: Int, deltaY: Int) {
    }

    /**
     * 当处于headView，页面的显示效果
     */
    override fun onRefreshingHeadViewState() {
    }


    /**
     * 当SecondFloorView处于下拉时，显示效果
     *
     * @param scrollY        下拉的距离
     * @param mSecondFloorView 头布局高度
     * @param deltaY         moveY-lastMoveY,正值为向下拉
     */
    override fun onPullDownmSecondFloorViewRefreshState(scrollY: Int, mSecondFloorViewHight: Int, deltaY: Int) {
    }

    /**
     * 当SecondFloorView处于松手马上显示时，显示效果
     *
     * @param scrollY 下拉的距离
     * @param deltaY  moveY-lastMoveY,正值为向下拉
     */
    override fun onReleaseSecondFloorRefreshState(scrollY: Int, deltaY: Int) {
    }


    /**
     * 当处于SecondFloorView，页面的显示效果
     */
    override fun onRefreshingSecondFloorViewState() {
    }

    /**
     * 默认状态时，页面显示效果，主要是为了复位各种状态
     */
    override fun onDefaultState() {
    }
}