package com.sharkgulf.soloera.cards

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.dn.tim.lib_permission.annotation.Permission
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.tool.config.getBsColor
import com.sharkgulf.soloera.tool.config.getGPPopup
import com.sharkgulf.soloera.tool.view.dialog.TrustGeneralPurposePopupwindow
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import kotlinx.android.synthetic.main.activity_sos_service.*
import kotlinx.android.synthetic.main.tite_layout.*

class SosServiceActivity : TrustMVPActivtiy<TrustView,TrustPresenters<TrustView>>(),TrustView {
    override fun getLayoutId(): Int { return R.layout.activity_sos_service }

    override fun initView(savedInstanceState: Bundle?) {
        title_tx.text = "服务中心"

        sos_title_layout.setBackgroundColor(getBsColor(R.color.colorRead))
        baseSetOnClick(title_back_btn)
        baseSetOnClick(sos_call_btn)
    }

    override fun initData() {}

    override fun baseResultOnClick(v: View) {
        if (v.id ==R.id.title_back_btn) {
            finish()
        }else{
            getGPPopup().showListPopu(arrayListOf("021-52216418"),object : TrustGeneralPurposePopupwindow.ListPopuAdapter.itemOnlickListener{
                override fun callBack(data: String) {
                    showCallPhone(data)
                }
            },"请拨打您需要的电话")
        }
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) {}

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {}

    override fun createPresenter(): TrustPresenters<TrustView> {
        return object :TrustPresenters<TrustView>(){}
    }

    @Permission(Manifest.permission.CALL_PHONE)
    private fun showCallPhone(data: String) {
        getGPPopup().showDoubleBtnPopu("拨打电话", "是否拨打:$data?", doubleBtnOnclickListener = object : TrustGeneralPurposePopupwindow.PopupOnclickListener.DoubleBtnOnclickListener {
            override fun onClickListener(isBtn1: Boolean) {
                if (!isBtn1) {
                    callPhone(data)
                }
            }
        }, btn1Tx = "取消", btn2Tx = "拨打")
    }

    /**
     * 拨打电话（直接拨打电话）
     * @param phoneNum 电话号码
     */
    fun callPhone(phoneNum :String ){
        val intent =  Intent(Intent.ACTION_CALL)
        val data = Uri.parse("tel:$phoneNum")
        intent.data = data
        startActivity(intent)
    }
}
