package com.sharkgulf.soloera.bindcar.fragment

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import kotlinx.android.synthetic.main.fragment_bind_car_description.*
import com.dn.tim.lib_permission.annotation.Permission
import com.dn.tim.lib_permission.annotation.PermissionCanceled
import com.dn.tim.lib_permission.annotation.PermissionDenied
import com.sharkgulf.soloera.bindcar.BindCarActivity
import com.sharkgulf.soloera.bindcar.BindCarActivity.Companion.FRAGMENT_TYPE_BIND_CAR
import com.sharkgulf.soloera.tool.config.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

import com.trust.demo.basis.trust.utils.TrustLogUtils


/*
 *Created by Trust on 2019/1/2
 */
 class FragmentBindCarDescription:TrustMVPFragment<TrustView,TrustPresenters<TrustView>>(),TrustView{
    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {}

    override fun onTrustFragmentFirstVisible() {}

    override fun getLayoutId(): Int { return R.layout.fragment_bind_car_description }

    companion object{
         var mCallBack: BindCarActivity.onFragmentCallBack? = null
        fun setOnFragmentCallBack(callBack: BindCarActivity.onFragmentCallBack){
            mCallBack = callBack
        }
    }

    private var mAdapter:BindCarRecyclerAdapter? = null
    private val TAG = FragmentBindCarDescription::class.java.canonicalName
    private var mList =arrayListOf(
            BindCarRecyclerAdapter.PagerHolderData(R.drawable.bind_car_connection_low_ic, R.string.add_cars_description_2_tx,
                    R.string.add_cars_description_3_tx, getBsString(R.string.robor_lite)),
            BindCarRecyclerAdapter.PagerHolderData(R.drawable.bind_car_connection_android_ic,R.string.add_cars_descroption_2_high_tx,
                            R.string.add_cars_descroption_3_high_tx,getBsString(R.string.robor)))
    override fun initView(view: View?, savedInstanceState: Bundle?) {
        baseSetOnClick(fragment_bind_car_next_btn)
        add_cars_description_2_tx.text = setHtmlSpanneds(R.string.add_cars_description_2_tx, callBack = mHtmlSpannedsCallBack)
    }

    private var mChoosePos = -1

    override fun initData() {
        setIsReGprsActivate(false)
        appStartBle()
        bleSetIsReConnection(false)
        bleDisConnection()

        mAdapter = BindCarRecyclerAdapter(mList )
        // 定义一个线性布局管理器
        val manager = LinearLayoutManager(mActivity!!)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        // 设置布局管理器
        bind_car_connection_viewpager.layoutManager = manager
        bind_car_connection_viewpager.adapter = mAdapter


        bind_car_connection_viewpager.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val l = recyclerView.layoutManager as LinearLayoutManager?
                val adapterNowPos = l!!.findFirstVisibleItemPosition()
                val allItems = l.itemCount
                if (mChoosePos != adapterNowPos) {
                    mChoosePos = adapterNowPos
                    val pagerHolder = mList[mChoosePos]
                    changePonit(mChoosePos)
                    add_cars_description_2_tx.text = setHtmlSpanneds(pagerHolder.msg2, callBack = mHtmlSpannedsCallBack)
                    add_cars_description_3_tx.text = setHtmlSpanneds(pagerHolder.msg3, callBack = mHtmlSpannedsCallBack)
                }
            }
        })



        PagerSnapHelper().attachToRecyclerView(bind_car_connection_viewpager)
    }


    @Permission(Manifest.permission.ACCESS_FINE_LOCATION)
    override fun baseResultOnClick(v: View) {
       if (v.id == R.id.fragment_bind_car_next_btn) {
           startBle()
       }
    }

    @PermissionCanceled //点击取消执行这个函数
    private fun cancel() {
        com.sharkgulf.soloera.tool.config.showToast(getBsString(R.string.no_perssion_error_tx))
    }


    @PermissionDenied//点击取消和不在提醒 执行这个函数 注意 这个函数执行后 会自动跳转到手机系统设置权限得页面
    private fun denied() {
        com.sharkgulf.soloera.tool.config.showToast(getBsString(R.string.no_perssion_error_tx))
    }


    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) {
        com.sharkgulf.soloera.tool.config.showToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {}

   override fun createPresenter(): TrustPresenters<TrustView> {
      return object :TrustPresenters<TrustView>(){}
   }

    private val mHtmlSpannedsCallBack = object : HtmlSpannedsCallBack {
        override fun callBack(source: String): Int {
            TrustLogUtils.d(TAG,"source :$source")
            return if (source == "1") {
                R.drawable.add_car_warn_ic
            }else{
                R.drawable.add_car_btn_ic
            }
        }
    }




    private fun startBle(){
        val hasSystemFeature = com.sharkgulf.soloera.tool.config.getContext().packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
        TrustLogUtils.d(com.sharkgulf.soloera.tool.config.TAG,"hasSystemFeature:$hasSystemFeature")
        if (hasSystemFeature) {
            val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (mBluetoothAdapter.state ==  BluetoothAdapter.STATE_OFF) {
                mBluetoothAdapter.enable() //开启
            }else{
                mCallBack?.fragmentCallBack(FRAGMENT_TYPE_BIND_CAR,isNewStart = true)
            }
        }
    }


    private fun changePonit(pos : Int){
        if (pos == 0) {
            fragment_bind_car_point1.setColor("#00c6ff")
            fragment_bind_car_point2.setColor("#bfbfbf")
        }else{
            fragment_bind_car_point1.setColor("#bfbfbf")
            fragment_bind_car_point2.setColor("#00c6ff")
        }

    }
}