package com.sharkgulf.soloera.tool

import android.annotation.SuppressLint
import android.os.Message
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.sharkgulf.soloera.tool.config.DEFULT
import com.trust.demo.basis.trust.utils.TrustLogUtils

/**
 *  Created by user on 2019/11/22
 */
class BsFragmentManger(builder:BsFragmentBuilder) {
    class BsFragmentBuilder(private var fragmentManger: FragmentManager, private var layoutId:Int){
        private var mFragmentList= arrayListOf<Fragment>()
        private var mIsReplace = true
        private var mBsFragmentManger:BsFragmentManger? = null

        fun setFragmentList(list: ArrayList<Fragment>):BsFragmentBuilder{
            mFragmentList = list
            return this
        }

        fun setIsReplace(isReplace:Boolean):BsFragmentBuilder{
            mIsReplace = isReplace
            return this
        }



        fun builder():BsFragmentManger{
            mBsFragmentManger = BsFragmentManger(this)
            return mBsFragmentManger!!
        }


        fun getFragmentList():ArrayList<Fragment>{
            return mFragmentList
        }

        fun getIsReplace():Boolean{
            return mIsReplace
        }

        fun getFragmentManger(): FragmentManager {
            return fragmentManger
        }

        fun getLayoutId():Int{
            return layoutId
        }
    }


    private val TAG = BsFragmentManger::class.java.canonicalName
    private var mFragmentList:ArrayList<FragmentMangerBean> = arrayListOf()
    private var mIsReplace =false
    private var mFragmentManger: FragmentManager? = null
    private var mLayout: Int? = null
    private var mFragmentTransaction: FragmentTransaction? = null
    private var oldFragmentPos = DEFULT
    private var newFragmentPos = DEFULT
    private var mBsFragmentMangerListener:BsFragmentMangerListener? = null
    private var mHandler = @SuppressLint("HandlerLeak")
    object :android.os.Handler(){
        override fun handleMessage(msg: Message?) {
            initTransaction()
//            if (mIsReplace) {//直接替换
//
//            }else{//hide or show
//                showFragment(0)
//            }
        }
    }
    init {
        builder.getFragmentList().forEach { mFragmentList.add(FragmentMangerBean(it,it.toString())) }
        mIsReplace = builder.getIsReplace()
        mFragmentManger = builder.getFragmentManger()
        mLayout = builder.getLayoutId()
        mHandler.sendEmptyMessage(DEFULT)
    }


    private fun initTransaction() {
        mFragmentManger!!.addOnBackStackChangedListener(onBackStackChangedListener)
        mFragmentTransaction = mFragmentManger!!.beginTransaction()
    }


    fun showChooseFragment(fragment: Fragment){
        val findFragment = mFragmentList.find { it.fragment == fragment }
        if (findFragment != null) {
            showFragment(findFragment)
        }else{
            throw (Throwable("the fragment is no add!"))
        }
    }

     fun showChooseFragment(pos:Int){
        if (newFragmentPos == oldFragmentPos) {//初始化第一次
            newFragmentPos = pos
        }else {
            oldFragmentPos = newFragmentPos
            newFragmentPos = pos
        }
        showFragment(mFragmentList[newFragmentPos])
    }


    private fun showFragment(fragmentMangerBean:FragmentMangerBean,isHide:Boolean = false){
        initTransaction()
        val findFragment = mFragmentManger!!.findFragmentByTag(fragmentMangerBean.tag)
        if (findFragment == null) {
            mFragmentTransaction!!.add(mLayout!!,fragmentMangerBean.fragment,fragmentMangerBean.tag).addToBackStack(fragmentMangerBean.tag)
        }
        fragmentMangerBean.fragment.onHiddenChanged(false)
        mFragmentTransaction!!.show(fragmentMangerBean.fragment).commitAllowingStateLoss()
        if (isHide) {
            initTransaction()
            mFragmentList.forEach {
                val fragment = it.fragment
                if (!fragment.equals(fragmentMangerBean.fragment)) {
                    fragment.onHiddenChanged(true)
                    mFragmentTransaction!!.hide(fragment)
                }
            }
            mFragmentTransaction!!.commitAllowingStateLoss()
        }
    }


    fun setBsFragmentMangerListener(list:BsFragmentMangerListener){
        mBsFragmentMangerListener = list
    }


    class FragmentMangerBean(var fragment: Fragment, var tag:String)
    interface BsFragmentMangerListener{
        fun isNothingFragmentShow()
    }

    fun removeFragmentMangerListener(){
        mFragmentManger!!.removeOnBackStackChangedListener(onBackStackChangedListener)
    }

    private val onBackStackChangedListener = FragmentManager.OnBackStackChangedListener {
        val backStackEntryCount = mFragmentManger!!.backStackEntryCount
        if (backStackEntryCount == 0) {
            mBsFragmentMangerListener?.isNothingFragmentShow()
        }else{
//            mFragmentManger?
//            mFragmentManger?.primaryNavigationFragment
//            mFragmentManger?.findFragmentById(R.id.main_content);

            mFragmentList.forEach {
//                it.fragment.hidd
                TrustLogUtils.d(TAG," fragment  可见性 ${it.fragment.isHidden }  ${it.tag} ")
            }
            TrustLogUtils.d(TAG,"${ mFragmentManger?.fragments} ")
        }


    }

    private fun replaceFragment(){

    }
}