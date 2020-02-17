package com.sharkgulf.soloera.controllcar

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.transition.Explode
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.module.bean.BsGetCarInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.sharkgulf.soloera.mvpview.controllcar.IControllCarView
import com.sharkgulf.soloera.presenter.controllcar.ControllCarPresenter
import com.trust.demo.basis.base.TrustMVPActivtiy


class ControllCarActivity : TrustMVPActivtiy<IControllCarView, ControllCarPresenter>(),IControllCarView {
    override fun resultBattryInfo(isDoublePower: Boolean, battry1: BattInfoBean.BodyBean.BattBean?, battry2: BattInfoBean.BodyBean.BattBean?, emerBattBean: BattInfoBean.BodyBean.EmerBattBean?) {}

    override fun getLayoutId(): Int {
        return R.layout.activity_controll_car
    }

    @SuppressLint("NewApi")
    override fun initView(savedInstanceState: Bundle?) {
        window.enterTransition = Explode().setDuration(500)
        window.exitTransition = Explode().setDuration(500)
        val fragmentControllCar = FragmentControllCar()
        val parcelableExtra = intent.getSerializableExtra("carBean")  as BsGetCarInfoBean.DataBean.BikesBean
        fragmentControllCar.setCarBean(parcelableExtra)
        chooseFragment(fragmentControllCar)
    }

    @SuppressLint("NewApi")
    fun chooseFragment(fragment: Fragment){
        fragment.enterTransition //首次进入显示的动画
        fragment.exitTransition //启动一个新Activity,当前页的退出动画
        fragment.returnTransition//调用 finishAfterTransition() 退出时，当前页退出的动画
        fragment.reenterTransition//重新进入的动画。即第二次进入，可以和首次进入不一样。

        fragment.enterTransition =Explode().setDuration(2000)
        fragment.returnTransition = Explode().setDuration(2000)

//        val changeBounds = ChangeBounds()
//        changeBounds.duration = 1000
//        changeBounds.startDelay = 2000
//        fragment.sharedElementEnterTransition = changeBounds
        supportFragmentManager.beginTransaction().replace(R.id.controll_car_framelayout,
                fragment).commit()
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

    override fun resultCarLock(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {
    }

    override fun resultStartCar(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {
    }

    override fun resultOpenBucket(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {
    }

    override fun resultOpenOrCloseEle(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {
    }

    override fun resultFindCar(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {
    }


    override fun createPresenter(): ControllCarPresenter {
        return ControllCarPresenter()
    }
    /* private var showDialog: DialogFragment? = null

     override fun getLayoutId(): Int {
         return R.layout.activity_controll_car
     }
     var isBucket = false
     var eleStatus = false
     var lockStatus = false
     var startStatus = false
     var findStatus = false
     override fun initView(savedInstanceState: Bundle?) {
         startLottie("controll_car_bg_data.json","images")
         test_view2.addAnimatorListener(object : Animator.AnimatorListener{
             override fun onAnimationRepeat(animation: Animator?) {

             }

             override fun onAnimationEnd(animation: Animator?) {

             }

             override fun onAnimationCancel(animation: Animator?) {
             }

             override fun onAnimationStart(animation: Animator?) {
             }

         })
         title_tx.text = "车辆控制"
         baseSetOnClick(controll_car_lock_btn)
         baseSetOnClick(controll_car_unlock_btn)
         baseSetOnClick(controll_car_start_btn)
         baseSetOnClick(controll_car_ele_btn)
         baseSetOnClick(controll_car_find_btn)
         baseSetOnClick(controll_car_bucket_btn)
         baseSetOnClick(title_back_btn)
     }

     fun startLottie(lottieJson:String,lottleImgs:String) {
         if(lottleImgs!=null){
             test_view.imageAssetsFolder = lottleImgs
         }
         test_view.useHardwareAcceleration();
         test_view.setAnimation(lottieJson)
         test_view.speed = 1F
         test_view.addAnimatorListener(object : Animator.AnimatorListener{
             override fun onAnimationRepeat(animation: Animator?) {

             }

             override fun onAnimationEnd(animation: Animator?) {
             }

             override fun onAnimationCancel(animation: Animator?) {
             }

             override fun onAnimationStart(animation: Animator?) {
             }

         })
     }

     @SuppressLint("NewApi")
     override fun initData() {
         window.enterTransition = Slide().setDuration(2000)
         window.exitTransition = Slide().setDuration(2000)


         *//* if (BsBleEVBHelper.bindBleStatus) {
            val bleConfig = bsDbManger!!.getBleConfig()
            if (bleConfig != null) {
                showWaitDialog("",true,"")
                bsServerBleManger!!.getBsBleEVBHelper().setBleConfig(bleConfig)
                bsServerBleManger!!.getBsBleEVBHelper().conection(object : BsServerBleManger.BsBleListener.conectionListener{
                    override fun conectionStatus(status: Boolean) {
                        diassDialog()
                        if (status) {
                            showToast("与车辆连接成功")
                        }else{
                            showToast("绑定车辆失败")
                        }
                    }
                })
            }
        }else{
            val bleConfig = bsDbManger!!.getBleConfig()
            if (bleConfig != null) {
                showWaitDialog("",true,"")
                bsServerBleManger!!.getBsBleEVBHelper().setBleConfig(bsDbManger!!.getBleConfig()!!)
                bsServerBleManger!!.getBsBleEVBHelper().conection(object : BsServerBleManger.BsBleListener.conectionListener{
                    override fun conectionStatus(status: Boolean) {
                        diassDialog()
                        if (status) {
                            showToast("与车辆连接成功")
                        }else{
                            showToast("绑定车辆失败")
                        }
                    }
                })
            }else{
                showToast("请添加车辆！")
            }
        }
*//*
    }

    @SuppressLint("NewApi")
    override fun baseResultOnClick(v: View) {
        when(v.id){
            R.id.controll_car_lock_btn->{
                carLock(bikeId)
            }
            R.id.controll_car_unlock_btn->{
                offCarLock(bikeId)
            }
            R.id.controll_car_start_btn->{
                carStart(bikeId)
            }
            R.id.controll_car_find_btn->{
                findCar(bikeId)
            }
            R.id.controll_car_ele_btn->{
                val status = if (eleStatus) {//关电门
                    ACTION_CLOSE
                }else{//开电门
                    ACTION_OPEN
                }
                val intenetAction = if (eleStatus) {//关电门
                    INTERNET_ACTION_OFF_ELE
                }else{//开电门
                    INTERNET_ACTION_OPEN_ELE
                }
                carEle(status,bikeId,intenetAction)
            }
            R.id.controll_car_bucket_btn->{
                val status = if (isBucket) {
                    ACTION_CLOSE
                }else{
                    ACTION_OPEN
                }

                val bucketAction = INTERNET_ACTION_OPEN_BUCKET
                carBucket(status, bikeId,bucketAction)
            }
            R.id.title_back_btn->{finishAfterTransition()}
        }
    }

    *//**
     * 开坐桶
     *//*
    fun carBucket(status: Int,bikeId:Int,buckAction:Int) {
        val map = hashMapOf<String,Any>()
        map["bike_id"] = bikeId
        map["command"] = buckAction
        getPresenter()!!.requestOpenBucket(status,map)
    }

    *//**
     * 开关电门
     *//*
    fun carEle(status: Int,bikeId:Int,eleAction:Int) {
        val map = hashMapOf<String,Any>()
        map["bike_id"] = bikeId
        map["command"] = eleAction
        getPresenter()!!.requestOpenOrCloseEle(status,map)
    }

    *//**
     * 寻车
     *//*
    fun findCar(bikeId:Int) {
        val map = hashMapOf<String,Any>()
        map["bike_id"] = bikeId
        map["command"] = INTERNET_ACTION_FIND_CAR
        getPresenter()!!.requestFindCar(ACTION_OPEN,map)
    }

    *//**
     * 一键启动
     *//*
    private fun carStart(bikeId:Int) {
        val map = hashMapOf<String,Any>()
        map["bike_id"] = bikeId
        map["command"] = INTERNET_ACTION_START_CAR
        getPresenter()!!.requestStartCar(ACTION_OPEN,map)
    }

    *//**
     * 设防
     *//*
    private fun offCarLock(bikeId:Int) {
        val map = hashMapOf<String,Any>()
        map["bike_id"] = bikeId
        map["command"] = INTERNET_ACTION_OFF_LOCK
        getPresenter()!!.requestCarLock(ACTION_CLOSE,map)
    }

    *//**
     * 撤防
     *//*
    private fun carLock(bikeId:Int) {
        val map = hashMapOf<String,Any>()
        map["bike_id"] = bikeId
        map["command"] = INTERNET_ACTION_OPEN_LOCK
        getPresenter()!!.requestCarLock(ACTION_OPEN,map)
    }


    override fun resultCarLock(actionType: Int, msg: String?) {
        var ss :String = ""
        if (actionType == ACTION_OPEN) {
            ss = "解锁 ：$msg"
            lockStatus = true
        }else{
            ss = "上锁 ：$msg"
            lockStatus = false
        }
        changeBtn(controll_car_lock_btn,lockStatus,R.drawable.controll_car_unlock_down,R.drawable.controll_car_unlock)
        changeBtn(controll_car_unlock_btn,!lockStatus,R.drawable.controll_car_lock_down,R.drawable.controll_car_lock)
        showToast(ss)
    }

    override fun resultStartCar(actionType: Int, msg: String?) {
        showToast("一键启动:$msg")
        startStatus = !startStatus
        changeBtn(controll_car_start_btn,startStatus,R.drawable.controll_car_start_down,R.drawable.controll_car_start)
    }

    override fun resultOpenBucket(actionType: Int, msg: String?) {
        showToast("坐桶操作: $msg")

        if (isBucket) {//关闭

            test_view2.speed = -1F
            test_view2.addAnimatorListener(object :Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    if (!isBucket) {
                        test_view2.visibility = View.GONE
                        hideOrShowCar(controll_car_ic,0.0f,1.0f)
                    }
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
            test_view2.playAnimation()
            isBucket = false
        }else{//开启
            isBucket = true
            test_view2.speed = 1F
            hideOrShowCar(controll_car_ic,1.0f,0.0f,200)
            test_view2.visibility = View.VISIBLE
            test_view2.playAnimation()
            changeBtn(controll_car_bucket_btn,isBucket,R.drawable.controll_car_sitting_bucket_down,R.drawable.controll_car_sitting_bucket)
        }
    }

    override fun resultOpenOrCloseEle(actionType: Int, msg: String?) {
        var ss :String = ""
        if (actionType == ACTION_OPEN) {
            eleStatus = true
            ss = "开电门 ：$msg"
        }else{
            eleStatus = false
            ss = "关电门 ：$msg"
        }
        changeBtn(controll_car_ele_btn,eleStatus,R.drawable.controll_car_ele_down,R.drawable.controll_car_ele)
        showToast(ss)
    }

    override fun resultFindCar(actionType: Int, msg: String?) {
        findStatus = !findStatus
        changeBtn(controll_car_find_btn,findStatus,R.drawable.controll_car_find_down,R.drawable.controll_car_find)
        showToast("寻车 $msg")
    }


    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
        if (isShow) {
            showDialog = TrustDialog().showDialog(supportFragmentManager)
        }
    }

    override fun diassDialog() {
        showDialog?.dismiss()
    }

    override fun showToast(msg: String?) {
        baseShowToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    override fun resultSuccess(msg: String) {
    }

    override fun resultError(msg: String) {
        baseShowToast(msg)
    }

    override fun createPresenter(): ControllCarPresenter {
        return ControllCarPresenter()
    }


    private fun hideOrShowCar(v:View,start:Float,end:Float,time:Long = 1000){
        TrustAnimation
                .getTrustAnimation()
                .addAlphaAnimation(start,end,time)
                .startAnimation(v)
    }



    private fun changeBtn(v:ImageView,isOpen:Boolean,openIc:Int,closeIc:Int){
//        val ic = if (isOpen) {//打开
//            openIc
//        }else{//关闭
//            closeIc
//        }
//        v.setImageResource(ic)
    }*/
}
