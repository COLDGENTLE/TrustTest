package com.sharkgulf.soloera.tool

import android.content.Intent
import android.content.ComponentName
import android.content.Context
import android.net.Uri
import com.sharkgulf.soloera.tool.config.getChannel
import com.trust.demo.basis.trust.utils.TrustLogUtils


/**
 *  Created by user on 2019/10/17
 */
class AppVersionUpdate {
    private val TAG = "AppVersionUpdate"
    companion object{
        private var mAppVersionUpdate:AppVersionUpdate? = null

        fun getInstance():AppVersionUpdate{
            if (mAppVersionUpdate == null) {
                mAppVersionUpdate = AppVersionUpdate()
            }
            return mAppVersionUpdate!!
        }
    }


    // 跳转到应用宝的网页版地址
    private val WEB_YINGYONGBAO_MARKET_URL = "https://a.app.qq.com/o/simple.jsp?pkgname=com.dk.collage"

    private val MARKET_PKG_NAME_MI = "com.xiaomi.market"
    private val MARKET_PKG_NAME_360 = "com.qihoo.appstore"
    private val MARKET_PKG_NAME_VIVO = "com.bbk.appstore"
    private val MARKET_PKG_NAME_OPPO = "com.oppo.market"
    private val MARKET_PKG_NAME_YINGYONGBAO = "com.tencent.android.qqdownloader"
    private val MARKET_PKG_NAME_ANZHI = "cn.goapk.market"
    private val MARKET_PKG_NAME_HUAWEI = "com.huawei.appmarket"
    private val MARKET_PKG_NAME_BAIDU = "com.baidu.appsearch"
    private val MARKET_PKG_NAME_LIQU = "com.liqucn.android"
    private val MARKET_PKG_NAME_SOUGOU = "com.sougou.androidtool"
    private val MARKET_PKG_NAME_MEIZU = "com.meizu.mstore"

    /**
     * 跳转到渠道对应的市场，如果没有该市场，就跳转到应用宝（App或者网页版）
     * @param context
     */
    fun goToAppMarket(context: Context) {
        try {
//            val uri = Uri.parse("market://details?id=" + context.applicationContext.packageName)
            val uri = Uri.parse("market://details?id=com.tencent.mobileqq" )
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            // 获取当前渠道channelName
            val channelName = getChannel("UMENG_CHANNEL")

            val pm = context.packageManager
            val resInfo = pm.queryIntentActivities(intent, 0)

            var pkgName = ""

            when (channelName) {
                "normal" -> pkgName = MARKET_PKG_NAME_YINGYONGBAO
                "baidu" -> pkgName = MARKET_PKG_NAME_BAIDU
                "BS_HUAWEI" -> pkgName = MARKET_PKG_NAME_HUAWEI
                "BS_OPPO" -> pkgName = MARKET_PKG_NAME_OPPO
                "qihu360" -> pkgName = MARKET_PKG_NAME_360
                "BS_VIVO" -> pkgName = MARKET_PKG_NAME_VIVO
                "BS_XIAOMI" -> pkgName = MARKET_PKG_NAME_MI
                "yingyongbao" -> pkgName = MARKET_PKG_NAME_YINGYONGBAO
                "anzi" -> pkgName = MARKET_PKG_NAME_ANZHI
                "liqu" -> pkgName = MARKET_PKG_NAME_LIQU
                "sougou" -> pkgName = MARKET_PKG_NAME_SOUGOU
                "BS_MEIZU" -> pkgName = MARKET_PKG_NAME_MEIZU
                else -> pkgName = MARKET_PKG_NAME_OPPO
            }

//            // 给一个默认的 应用宝
//            if (NullUtil.isNull(pkgName)) {
//                pkgName = MARKET_PKG_NAME_YINGYONGBAO
//            }

            // 筛选指定包名的市场intent
            if (resInfo.size > 0) {
                for (i in resInfo.indices) {
                    val resolveInfo = resInfo.get(i)
                    val packageName = resolveInfo.activityInfo.packageName
                    if (packageName.toLowerCase() == pkgName) {
                        val intentFilterItem = Intent(Intent.ACTION_VIEW, uri)
                        intentFilterItem.component = ComponentName(packageName, resolveInfo.activityInfo.name)
                        context.startActivity(intentFilterItem)
                        return
                    }
                }
            }
            // 未匹配到，跳转到应用宝网页版
//            goToYingYongBaoWeb(context)
        } catch (e: Exception) {
            TrustLogUtils.d(TAG,e.toString())
            e.printStackTrace()
            // 发生异常，跳转到应用宝网页版
//            goToYingYongBaoWeb(context)
        }

    }

    /**
     * 跳转到应用宝网页版 多客拼团页面
     */
    fun goToYingYongBaoWeb(context: Context) {
        try {
            val uri = Uri.parse(WEB_YINGYONGBAO_MARKET_URL)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}