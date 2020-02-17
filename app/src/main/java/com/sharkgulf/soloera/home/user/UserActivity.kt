package com.sharkgulf.soloera.home.user

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.db.dbmanger.BsDbManger
import com.sharkgulf.soloera.tool.config.glideUserIc
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import com.trust.demo.basis.trust.utils.TrustAppUtils
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : TrustMVPActivtiy<TrustView,TrustPresenters<TrustView>>(),TrustView {
    override fun getLayoutId(): Int {
        return R.layout.activity_user
    }

    override fun initView(savedInstanceState: Bundle?) {
        /*baseSetOnClick(user_ext_btn)*/
        baseSetOnClick(user_info_btn)
        baseSetOnClick(user_cars_btn)
        baseSetOnClick(user_app_version_btn)
    }

    override fun initData() {
        changeUi()

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun baseResultOnClick(v: View) {
        when (v.id) {

            /* R.id.user_ext_btn->{
               val checkUserLoginStatus = BsApplication.bsDbManger!!.checkUserLoginStatus()
               checkUserLoginStatus!!.userLoginStatus = false
               BsApplication.bsDbManger!!.setUserLoginStatus(checkUserLoginStatus)
               startActivity(Intent(this,LogingActivity::class.java))
               finishActivityList()
           }
           else -> {
           }*/
            R.id.user_app_version_btn ->{
                startActivity(Intent(this,AboutBsActivity::class.java))
            }
            R.id.user_info_btn->{
                startActivity(Intent(this,UserInfoActivity::class.java))
            }
            R.id.user_cars_btn->{
//                startActivity(Intent(this, CarsActivity::class.java))
            }
        }
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

    override fun onResume() {
        changeUi()
        super.onResume()
    }

    private fun changeUi(){
        val checkUserLoginStatus = BsDbManger.mDbUserLoginStatusBean
        if (checkUserLoginStatus != null) {
            user_phone_tx.text = checkUserLoginStatus.userPhone
            user_name_tx.text = checkUserLoginStatus.userNickName
            user_name_tx.text = checkUserLoginStatus.userBean?.nick_name
            user_phone_tx.text = checkUserLoginStatus.userBean?.phone_num
//            user_signature_tx.text = checkUserLoginStatus.userBean?.per_sign
            user_logo_img.glideUserIc(checkUserLoginStatus.userBean?.icon,true)
        }

        user_version_tx.text = "版本:${TrustAppUtils.getAppVersionName(this)}"
    }
}
