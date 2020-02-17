package com.sharkgulf.soloera.bindcar

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.bindcar.fragment.FragmentBindCar
import com.sharkgulf.soloera.bindcar.fragment.FragmentBindCarConnection
import com.sharkgulf.soloera.bindcar.fragment.FragmentBindCarDescription
import com.sharkgulf.soloera.main.MainHomeActivity
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import kotlinx.android.synthetic.main.tite_layout.*
import android.view.KeyEvent
import com.sharkgulf.soloera.TrustAppConfig.HOME_UPDATE_KEY
import com.sharkgulf.soloera.bindcar.fragment.FragmentBindCar.Companion.STATUS_NO_FOIND
import com.sharkgulf.soloera.bindcar.fragment.FragmentBindCarSetCar
import com.sharkgulf.soloera.main.MainHomeActivity.Companion.RESULT_ADD_CAR_CEODE
import com.sharkgulf.soloera.module.bean.BsGetBindInfoBean
import com.sharkgulf.soloera.tool.config.*


class BindCarActivity : TrustMVPActivtiy<TrustView,TrustPresenters<TrustView>>(),TrustView {
    private var mFragmentBindCarDescription:FragmentBindCarDescription? = null
    private var mFragmentBindCar: FragmentBindCar? = null
    private var mFragmentBindCarConnection: FragmentBindCarConnection? = null
    private var mFragmentBindCarSetCar: FragmentBindCarSetCar? = null
    private val fragmentList = arrayListOf<Fragment>()
    private var mFragmentStatus = FRAGMENT_TYPE_DESCRIPTION
    private var mMac:String? = null
    private var mBikeId:Int? = null
    private val TAG = BindCarActivity::class.java.canonicalName
    val manager = supportFragmentManager
    override fun getLayoutId(): Int { return R.layout.activity_bind_car }

    companion object {
        var userPhone:String? = null
        const val FRAGMENT_TYPE_DESCRIPTION = 0
        const val FRAGMENT_TYPE_BIND_CAR = 1
        const val FRAGMENT_TYPE_BIND_CAR_CONNECTION = 2
        const val FRAGMENT_TYPE_BIND_CAR_SET_CAR = 3
        const val FRAGMENT_TYPE_CLOSE = 4
        private var mBindCarActivity:BindCarActivity? = null
        fun getBindCar():BindCarActivity{
            return mBindCarActivity!!
        }
    }


    override fun initView(savedInstanceState: Bundle?) {
        mBindCarActivity = this
        baseSetOnClick(title_back_btn)
        baseSetOnClick(title_submint_btn)
//        activity_bind_car_refresh.setOnRefreshListener {
//            refreshListener?.refreshListener()
//            activity_bind_car_refresh.isRefreshing = false;
//        }
        FragmentBindCarDescription.setOnFragmentCallBack(callBack)
        FragmentBindCar.setOnFragmentCallBack(callBack)
        FragmentBindCarConnection.setOnFragmentCallBack(callBack)
        FragmentBindCarSetCar.setOnFragmentCallBack(callBack)


//        callBack.fragmentCallBack(DEFULT)
    }

    override fun initData() {
        val phone = intent.getStringExtra("phone")
        if (phone == null) {
            val checkUserLoginStatus = BsApplication.bsDbManger!!.checkUserLoginStatus()
            if (checkUserLoginStatus != null) {
                userPhone=  checkUserLoginStatus.userPhone
            }
        }else{
            userPhone = phone
        }

        if (mFragmentBindCarDescription == null) {
            mFragmentBindCarDescription = FragmentBindCarDescription()
            supportFragmentManager.beginTransaction().add(R.id.activity_bind_car_frame_layout,mFragmentBindCarDescription!!).show(mFragmentBindCarDescription!!).commitAllowingStateLoss()
            fragmentList.add(mFragmentBindCarDescription!!)
        }
        title_tx.text = "添加车辆"
        title_tx.setTextColor(Color.WHITE)
        title_back_btn.setColor("#ffffff")
        title_submint_btn.setTextColor(Color.WHITE)
        title_submint_btn.text = "帮助"
//        title_back_txt.text = "首页"
    }




    override fun baseResultOnClick(v: View) {
        if (v.id == R.id.title_back_btn) {
            back()
        }else if (v.id == R.id.title_submint_btn){
            mFragmentBindCar?.showHelpLayout(true)
//            getGPPopup().showHelpPopu()
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



    private fun changeFragment(fragment: Fragment){
        val beginTransaction = supportFragmentManager.beginTransaction()
        fragmentList.forEach {
            if (it.equals(fragment)) {

            }else{
                beginTransaction.hide(it)
            }
        }
//        beginTransaction.replace(R.id.activity_bind_car_frame_layout,fragment)
        beginTransaction.commit()
//        val beginTransaction = supportFragmentManager.beginTransaction()
//
//        beginTransaction.add(R.id.activity_bind_car_frame_layout,
//                fragment).commitAllowingStateLoss()
//        supportFragmentManager.beginTransaction().replace(R.id.activity_bind_car_frame_layout,
//                fragment).commitAllowingStateLoss()
    }

    interface onFragmentCallBack{
        fun fragmentCallBack(fragmentType:Int,type:Int = -1,mac:String ="",bikeId:Int = 0,carBindBean: BsGetBindInfoBean.DataBean.BindInfoBean? = null,isNewStart:Boolean = false,isBindStatus:Boolean = false)
        fun changeTitle(title:String)
    }

    private val callBack = object :onFragmentCallBack{
        override fun changeTitle(title: String) {
            title_tx.text = title
        }

        override fun fragmentCallBack(fragmentType: Int, type: Int,mac:String,bikeId:Int, bindInfoBean: BsGetBindInfoBean.DataBean.BindInfoBean?,isNewStart:Boolean,isBindStatus:Boolean) {
            mFragmentStatus = fragmentType
            val beginTransaction = manager.beginTransaction()
            val fragment = when (fragmentType) {
                FRAGMENT_TYPE_BIND_CAR -> {
                    FragmentBindCar.mCallBack?.changeTitle("搜索附近车辆")
                    title_submint_btn.visibility = View.VISIBLE
                    bleDisConnection()
                    if (mFragmentBindCar == null ) {
                        mFragmentBindCar = FragmentBindCar()
                        beginTransaction.add(R.id.activity_bind_car_frame_layout,mFragmentBindCar!!)
                        fragmentList.add(mFragmentBindCar!!)
                    }
                    if (isNewStart) {
                        mFragmentBindCar?.setType(FragmentBindCar.STATUS_INIT)
                    }else{
                        mFragmentBindCar?.setType(STATUS_NO_FOIND)
                    }

                    beginTransaction.show(mFragmentBindCar!!).commitAllowingStateLoss()
                    mFragmentBindCar
                }
                FRAGMENT_TYPE_BIND_CAR_CONNECTION -> {
                    FragmentBindCarConnection.mCallBack?.changeTitle("输入PIN码")
                    title_submint_btn.visibility = View.INVISIBLE
                    mMac = mac
                    mBikeId = bikeId
                    if (mFragmentBindCarConnection == null ) {
                        mFragmentBindCarConnection = FragmentBindCarConnection()
                        beginTransaction.add(R.id.activity_bind_car_frame_layout,mFragmentBindCarConnection!!)
                        fragmentList.add(mFragmentBindCarConnection!!)
                    }
                    mFragmentBindCarConnection?.setBleBean(bindInfoBean!!)
                    beginTransaction.show(mFragmentBindCarConnection!!).commitAllowingStateLoss()
                    mFragmentBindCarConnection
                }
                FRAGMENT_TYPE_BIND_CAR_SET_CAR -> {
                    title_submint_btn.visibility = View.INVISIBLE
                    if (mFragmentBindCarSetCar == null) {
                        mFragmentBindCarSetCar = FragmentBindCarSetCar()
                        beginTransaction.add(R.id.activity_bind_car_frame_layout,mFragmentBindCarSetCar!!)
                        fragmentList.add(mFragmentBindCarSetCar!!)
                    }

                    FragmentBindCarSetCar.setBleBean(bindInfoBean!!)
                    beginTransaction.show(mFragmentBindCarSetCar!!).commitAllowingStateLoss()
                    mFragmentBindCarSetCar
                }

                FRAGMENT_TYPE_CLOSE -> {
                    title_submint_btn.visibility = View.INVISIBLE
                    null
                }
                else -> {
                    FragmentBindCarDescription.mCallBack?.changeTitle("添加车辆")
                    title_submint_btn.visibility = View.INVISIBLE
                    bleStopScanner()
                    beginTransaction.show(mFragmentBindCarDescription!!).commitAllowingStateLoss()
                    mFragmentBindCarDescription
                }
            }
            if (fragment != null) {
                changeFragment(fragment)
            }else{
                startMainHomeActivity()
            }
        }
    }

    private fun startMainHomeActivity() {

        activityList.forEach {
            if (it is MainHomeActivity) {
                it.finish()
            }
        }
        val intent = Intent(this@BindCarActivity, MainHomeActivity::class.java)
        intent.putExtra(HOME_UPDATE_KEY,true)
        startActivity(intent)
        finish()
    }




     fun back(isNewStart:Boolean = false){
        when (mFragmentStatus) {
            FRAGMENT_TYPE_DESCRIPTION -> {
                finish()
            }
            FRAGMENT_TYPE_BIND_CAR -> {
                bleStopScanner()
                callBack.fragmentCallBack(FRAGMENT_TYPE_DESCRIPTION,isNewStart = isNewStart)
            }
            FRAGMENT_TYPE_BIND_CAR_CONNECTION -> {
                callBack.fragmentCallBack(FRAGMENT_TYPE_BIND_CAR,FragmentBindCar.STATUS_NO_FOIND,isNewStart = isNewStart)
            }

            FRAGMENT_TYPE_BIND_CAR_SET_CAR ->{
//                callBack.fragmentCallBack(FRAGMENT_TYPE_BIND_CAR_CONNECTION)
                if (FragmentBindCarSetCar.isBindStatus) {
                    startMainHomeActivity()
                }else{
                    callBack.fragmentCallBack(FRAGMENT_TYPE_BIND_CAR,isNewStart = isNewStart)
                }
            }
            else -> {
                setResult(RESULT_ADD_CAR_CEODE)
                finish()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back()
            return false
        } else {
            return super.onKeyDown(keyCode, event)
        }
    }

    override fun onBackPressed() {
        back()
    }

    override fun onDestroy() {
        BsApplication.mBsApplication?.getService()?.setBleCallBack()
        super.onDestroy()
    }
}
