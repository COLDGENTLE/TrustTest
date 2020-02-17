package com.sharkgulf.soloera.main

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.tim.lib_permission.annotation.Permission
import com.next.easynavigation.constant.Anim
import com.sharkgulf.soloera.appliction.BsApplication.Companion.mAuthentication
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.appliction.BsApplication.Companion.mTrustWebSocket
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.tool.alert.AlertDataCenter
import com.sharkgulf.soloera.tool.arouter.ROUTE_PATH_LOGING
import com.sharkgulf.soloera.tool.arouter.ROUTE_PATH_MAIN_HOME
import com.sharkgulf.soloera.tool.arouter.arouterStartActivity
import com.sharkgulf.soloera.tool.config.bleSetIsReConnection
import com.sharkgulf.soloera.tool.config.*
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.base.TrustMVPFragment.Companion.mFragmentManger
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.activity_main_home.*
import kotlin.concurrent.thread
import androidx.drawerlayout.widget.DrawerLayout
import android.view.*
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.module.bean.BsAppVersionInfoBean
import com.sharkgulf.soloera.module.bean.BsGetCarInfoBean
import com.sharkgulf.soloera.mvpview.UtilsView
import com.sharkgulf.soloera.presenter.UtilsPresenters
import com.sharkgulf.soloera.tool.BeanUtils.Companion.CODE_SUCCESS
import com.sharkgulf.soloera.tool.view.dialog.TrustGeneralPurposePopupwindow
import com.trust.demo.basis.trust.TrustTools
import razerdp.basepopup.BasePopupWindow


private val tabText = arrayOf(setTextStrings(R.string.main_my_car_tx))
//,
//setTextStrings(R.string.main_blue_shark_tx),
//setTextStrings(R.string.main_shopping_tx),
//setTextStrings(R.string.main_my_tx)

//未选中icon
private val normalIcon = intArrayOf(R.drawable.main_car_ic)

//,
//R.drawable.main_bs_ic,R.drawable.heart,
//R.drawable.main_my_ic

//选中时icon
private val selectIcon = intArrayOf(R.drawable.main_car_sel_ic)
//,
//R.drawable.main_bs_sel_ic,R.drawable.heart_sel,
//R.drawable.main_mysel_ic

private val fragments = arrayListOf<Fragment>()
@Route(path = ROUTE_PATH_MAIN_HOME)
class MainHomeActivity : TrustMVPActivtiy<UtilsView, UtilsPresenters>(), UtilsView {


    companion object{
         var mFragmentJumpType = DEFUTE
         val TO_ADD_CAR_REQUEST_CEODE = 1
         val RESULT_ADD_CAR_CEODE = 2
        var mMainHomeActivity:MainHomeActivity? = null
    }
    private val TAG = MainHomeActivity::class.java.canonicalName!!

    private var misDrawerLayoutisOpen = true


    override fun getLayoutId(): Int { return R.layout.activity_main_home }

    override fun onCreate(savedInstanceState: Bundle?) {

        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        super.onCreate(savedInstanceState)
    }

    override fun initView(savedInstanceState: Bundle?) {
        isFirstStartHomeMain = true
        messagerIsReadStatus(true)
        registerMainNoReadStatus()
        appIsInit = true
        bleSetIsReConnection(true)
        val fragmentMyCar = FragmentMyCar()
        fragments.add(fragmentMyCar)
        fragmentMyCar.setFragmentListener(jumpListener)
//        fragments.add(FragmentBlueShark())
//        fragments.add(FragmentShopping())
//        fragments.add(FragmentBlueShark())
        navigationBar.getmViewPager().offscreenPageLimit =1
        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .navigationHeight(0)
                .fragmentManager(supportFragmentManager)
                .normalTextColor(Color.parseColor("#666666"))   //Tab未选中时字体颜色
                .selectTextColor(Color.parseColor("#0000ff"))   //Tab选中时字体颜色
                .smoothScroll(false)  //点击Tab  Viewpager切换是否有动画
                .lineHeight(1)         //分割线高度  默认1px
                .lineColor(Color.parseColor("#c4c4c4"))
                .onTabClickListener { view, pos ->
                    //可以判断是否登陆 false 不拦截 true 拦截
//                    when (pos) {
//                        0,3 -> {
//                            checkUserStatus()
//                        }
//                        else -> {
//                            false
//                        }
//                    }
//                    mFragmentJumpType = DEFUTE
                    false
                }
                .build()
        navigationBar.selectTab(0)
        navigationBar.anim(Anim.RubberBand)

        //点击Tab时的动画
//        //数字消息大于99显示99+ 小于等于0不显示，取消显示则可以navigationBar.setMsgPointCount(2, 0)
//        navigationBar.setMsgPointCount(2, 109);
//        navigationBar.setMsgPointCount(0, 5);
        //红点提示 第二个参数控制是否显示


//        TestPay(this)
//
//        addPayCallBack(object :PayCallBack{
//            override fun callBack(result: String?, errorMsg: String?, extraMsg: String?) {
//                Log.d("lhh","支付回调  result $result  errorMsg $errorMsg  extraMsg $extraMsg")
//            }
//        })
        checkUserCarStatus()
        checkUserStatus()

        dataAnalyCenter().registerCallBack(APP_MAIN_HOME_UPDATE,object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
                checkUserCarStatus()
            }
            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        },TAG)
        thread {
            Thread.sleep(1000)
            runOnUiThread {
                if (!getAppPrivacyPolicyStatus()) {
                    getAlerTool().showPrivacyPolicy()
                }
            }
        }


//        getPermission()
//        AppVersionUpdate.getInstance().goToAppMarket(this)

        navigationBar.addViewLayout.visibility = View.GONE


        //初始化 侧滑栏布局
        drawer_layout.addDrawerListener(object : DrawerLayout.DrawerListener{
            override fun onDrawerStateChanged(pos: Int) {}

            override fun onDrawerSlide(p0: View, p1: Float) {}

            override fun onDrawerClosed(p0: View) {
                misDrawerLayoutisOpen = true
            }

            override fun onDrawerOpened(p0: View) {
                misDrawerLayoutisOpen = false
            }

        })


    }

    override fun initData() {
        getPresenter()?.requestAppVersion(RequestConfig.requestAppVersion())
        TrustTools<View>().setCountdown(3,object :TrustTools.CountdownCallBack{
            override fun callBackCountDown() {
                val msg = intent.getStringExtra(ALERT_NOTIFICATIION_KEY)
                if (msg != null) {
                    AlertDataCenter.getInstance().checkData(msg)
                }
            }
        })

        updateFragment(intent)
        (main_fragment_my as FragmentMy).setFragmentListener(jumpListener)
        navigationBar.getmViewPager().adapter?.notifyDataSetChanged()
    }

    override fun baseResultOnClick(v: View) {}

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) {}

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {}

    override fun createPresenter(): UtilsPresenters {
        return UtilsPresenters()
    }

    private var closeTime = 0L


    override fun onDestroy() {
        fragments.clear()
        mainRvControlOrder.clear()
        dataAnalyCenter().unRegisterCallBack(APP_MESSAGER_READ,TAG!!)
        dataAnalyCenter().unRegisterCallBack(APP_MAIN_HOME_UPDATE,TAG)
        super.onDestroy()
    }

    override fun onBackPressed() {
//        if (!FragmentShopping.webCanBlack  && !FragmentBlueShark.webCanBlack && !FragmentBlueShark.shoppingIsShow) {
            if((System.currentTimeMillis() - closeTime) < 200){
                finish()
            }else{
                closeTime = System.currentTimeMillis()
                com.sharkgulf.soloera.tool.config.showToast(getString(R.string.is_ext_tx))
            }
//        }

    }


    override fun onNewIntent(intent: Intent?) {
        isFirstStartHomeMain = true
        super.onNewIntent(intent)
        drawer_layout.closeDrawer(navigation_view)
        updateFragment(intent)
        navigationBar.adapter.notifyDataSetChanged()
    }

     fun updateFragment(intent: Intent? = null) {
         bleSetIsReConnection(true)
         if (intent != null) {
             getFragmentMyCar()?.updateFragment(intent.getBooleanExtra(HOME_UPDATE_KEY,false))
         }
        //更新fragment


         updateUsreFragment()


         val fragmentShopping =
                mFragmentManger[FragmentShopping::class.java.canonicalName]
         if (fragmentShopping != null) {
             fragmentShopping as FragmentShopping
             fragmentShopping.updateFragment()
         }


         checkUserStatus()
         checkUserCarStatus()

    }



    private fun updateUsreFragment() {
        val fragmentMy =
                mFragmentManger[FragmentMy::class.java.canonicalName]
        if (fragmentMy != null) {
            fragmentMy as FragmentMy
            fragmentMy.updateFragment()
        }
    }


    private fun checkUserStatus(){
        val user = mAuthentication.getUser()
        if ( user!= null && mAuthentication.getUserLoginStatus()) {
            if (isFirstStartHomeMain) {
                isFirstStartHomeMain = false
            mTrustWebSocket?.connect(user.userToken!!)
            }
        }else if(USE_TYPE == USE_DEMO){
            mTrustWebSocket?.connect(DEMO_TOKEN)
        }
    }

    private fun checkUserCarStatus() {
        if(mFragmentJumpType == EXPEROEMCE){
            navigationBar.selectTab(2)
            thread {
                Thread.sleep(2000)
//                mFragmentJumpType = DEFUTE
            }
//            mFragmentJumpType = DEFUTE
        }else if (mAuthentication.getUserCarInfo()) {
            navigationBar.selectTab(0)
        }else{
            navigationBar.selectTab(0)
        }
    }


    private fun registerMainNoReadStatus(){
        dataAnalyCenter().registerCallBack(APP_MESSAGER_READ,object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
                messagerIsReadStatus(msg!!.toBoolean())
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        },TAG)
    }

    private fun messagerIsReadStatus(isRead:Boolean){
        runOnUiThread {
            navigationBar.setHintPoint(0, !isRead)
        }
    }

    interface onFragmentJumpListener{
        fun onJumpListener(jumpType:Int?)
        fun onControllDrawLayout()
        fun onUpdateUi()
        fun onChooseCar(bean: BsGetCarInfoBean.DataBean.BikesBean?)
    }

    private val jumpListener = object :onFragmentJumpListener{
        override fun onChooseCar(bean: BsGetCarInfoBean.DataBean.BikesBean?) {
//            getFragmentMyCar()?.updateChooseCar(bean)
        }

        override fun onUpdateUi() {
            updateUsreFragment()
        }

        override fun onControllDrawLayout() {
            showSettingsLayout()
        }

        override fun onJumpListener(jumpType: Int?) {
            if (jumpType != null) {
                mFragmentJumpType = jumpType
            }
            if (mFragmentJumpType != DEFUTE) {
                arouterStartActivity(ROUTE_PATH_LOGING)
            }
            TrustLogUtils.d(TAG,"mFragmentJumpType $mFragmentJumpType")
        }
    }

    @Permission(Manifest.permission.READ_EXTERNAL_STORAGE)
    private fun getPermission(){}


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        TrustLogUtils.d(TAG,"requestCode:$requestCode  ||| resultCode:$resultCode")
        if (TO_ADD_CAR_REQUEST_CEODE== requestCode && resultCode == RESULT_ADD_CAR_CEODE) {
            bleSetIsReConnection(true)
            //更新fragment
            val fragmentMyCar =
                    mFragmentManger[FragmentMyCar::class.java.canonicalName]
            if (fragmentMyCar != null) {
                fragmentMyCar  as FragmentMyCar
                fragmentMyCar.bleStartConnection()
            }
        }
    }


    private fun showSettingsLayout() {
        if (misDrawerLayoutisOpen) {
            drawer_layout.openDrawer(navigation_view)
        }else{
            drawer_layout.closeDrawer(navigation_view)
        }
        misDrawerLayoutisOpen = !misDrawerLayoutisOpen
    }


    override fun resultAppVersionInfo(bean: BsAppVersionInfoBean?) {
        val state = bean?.getState()
        if (state != null && state == CODE_SUCCESS) {
            appVersionUpdate(this,bean,object :TrustGeneralPurposePopupwindow.PopupOnclickListener.DownLoadBtnListener{
                override fun onClickListener(isBtn1: Boolean, v: BasePopupWindow) {
                    if (!isBtn1) {
                        downLoadApp(v)
                    }
                }
            })
        }
    }

    @Permission(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private fun downLoadApp(v: BasePopupWindow){
        downLoad(v)
    }
}
