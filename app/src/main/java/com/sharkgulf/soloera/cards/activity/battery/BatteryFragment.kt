package com.sharkgulf.soloera.cards.activity.battery

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import kotlinx.android.synthetic.main.fragment_battery.*

/**
 *  Created by user on 2019/2/22
 */
class BatteryFragment :TrustMVPFragment<TrustView,TrustPresenters<TrustView>>(),TrustView {
    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {
    }

    override fun onTrustFragmentFirstVisible() {
    }

    private var info_sn_txt:TextView? = null
    private var info_rated_v:TextView? = null
    private var info_rated_c:TextView? = null
    private var info_mile_es:TextView? = null
    private var info_capacity:TextView? = null
    private var info_cycle:TextView? = null
    private var info_version:TextView? = null
    private var info_prod_date_tx:TextView? = null
    private var battType = arrayListOf<String>("铅酸","简易锂电池","标准锂电池")
    override fun getLayoutId(): Int {
        return R.layout.fragment_battery
    }

    fun getBatteryInfoChangeListener():BatteryInfoChangeListener{
        return mBatteryInfoChangeListener
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        info_sn_txt = view?.findViewById(R.id.battery_info_sn_tx)
        info_rated_v = view?.findViewById(R.id.battery_info_rated_v_tx)
        info_rated_c = view?.findViewById(R.id.battery_info_rated_c_tx)
        info_mile_es = view?.findViewById(R.id.battery_info_mile_es_tx)
        info_capacity = view?.findViewById(R.id.battery_info_capacity_tx)
        info_cycle = view?.findViewById(R.id.battery_info_cycle_tx)
        info_version = view?.findViewById(R.id.battery_info_version_tx)
        info_prod_date_tx = view?.findViewById(R.id.battery_info_prod_date_tx)
    }

    override fun initData() {

    }

    override fun baseResultOnClick(v: View) {
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
    }

    override fun diassDialog() {
    }

    override fun showToast(msg: String?) {
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    override fun resultSuccess(msg: String) {
    }

    override fun resultError(msg: String) {
    }

    override fun createPresenter(): TrustPresenters<TrustView> {
        return object :TrustPresenters<TrustView>(){}
    }

    private val mBatteryInfoChangeListener = object :BatteryInfoChangeListener{
        override fun infoChangesListener(pos:Int,bean: BattInfoBean.BodyBean.BattBean?) {
                changeUi(pos,bean)
        }
    }

    private fun changeUi(pos:Int,bean1: BattInfoBean.BodyBean.BattBean?){
        battery_info_pos_tx?.text = "电池仓$pos:"
//        if (bean1 != null) {

            battery_info_sn_tx?.text = "电池编号：${strUtils(bean1?.info?.sn,"")}"
            battery_info_rated_v_tx?.text = "电池电压：${strUtils(bean1?.info?.rated_v.toString(),"v")}"
        val loss = bean1?.info?.loss
        battery_info_rated_c_tx?.text = "电池最大容量：${strUtils(if(loss != null) (100 - loss).toString() else "","%")}"
            battery_info_mile_es_tx?.text = "电池类型：${strUtils(getBattType(bean1?.info?.type),"")}"
            battery_info_capacity_tx?.text = "电池容量：${strUtils(bean1?.info?.rated_c.toString(),"Ah")}"
            battery_info_cycle_tx?.text = "电池已使用：${strUtils(bean1?.info?.cycle.toString(),"次")}"
            battery_info_version_tx?.text = "版本：${strUtils(bean1?.info?.version,"")}"
            battery_info_prod_date_tx?.text = "电池生产日期：${strUtils(bean1?.info?.prod_date,"")}"
//        }
    }


    interface BatteryInfoChangeListener{
        fun infoChangesListener(pos:Int,bean: BattInfoBean.BodyBean.BattBean?)
    }

    private fun strUtils(msg:String?,ex:String):String{
        return if (msg != null && msg != "null" && msg != "") {
            msg+ex
        }else{
            "-"
        }
    }

    private fun getBattType(i :Int?):String?{
        return if (i == null) {
            null
        }else{
            if(i<3){
                battType[i]
            }else{
                null
            }
        }
    }
}