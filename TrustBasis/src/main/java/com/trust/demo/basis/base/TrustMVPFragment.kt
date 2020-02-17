package com.trust.demo.basis.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import com.trust.demo.basis.R
import com.trust.demo.basis.base.delegate.TrustMvpCallback
import com.trust.demo.basis.base.delegate.fragment.TrustMvpFragmentDelegate
import com.trust.demo.basis.base.delegate.fragment.TrustMvpFragmentDelegateImpl
import com.trust.demo.basis.base.presenter.TrustPresenter
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.TrustLogUtils
import com.trust.demo.basis.trust.utils.TrustStringUtils
import com.trust.demo.basis.trust.weight.dialog.TrustDialogFragment
import com.trust.statusbarlibrary.TrustStatusBarUtils
import java.util.concurrent.TimeUnit

/**
 * Created by Trust on 2018/6/26.
 * MvpFragment
 * 第一重代理：代理对象
 * 第二重代理：目标对象
 */
abstract class TrustMVPFragment <V : TrustView,P : TrustPresenters<V>>: Fragment() ,TrustView,TrustMvpCallback<V,P>,TrustFragmentListener{
    protected var mActivity: AppCompatActivity? = null
    protected var mContext: Context? = null
    protected abstract fun getLayoutId():Int
    protected abstract fun initView(view:View?,savedInstanceState: Bundle?)
    protected abstract fun initData()
    private var mTrustMvpFragmentDelegate: TrustMvpFragmentDelegate<V,P>? = null
    private var mRootView:View? = null
    private var mPresenter:P? = null

    fun getMvpDelegate():TrustMvpFragmentDelegate<V,P>?{
        if (mTrustMvpFragmentDelegate == null){
            this.mTrustMvpFragmentDelegate = TrustMvpFragmentDelegateImpl<V,P>(this)
        }
        return  mTrustMvpFragmentDelegate
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getMvpDelegate()!!.onCreateView(inflater,container,savedInstanceState)
        return inflater!!.inflate(getLayoutId(), null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (mRootView == null) {
            TrustLogUtils.d("TrustFragment","create fragment ${javaClass.canonicalName}")
            if (mFragmentManger[javaClass.canonicalName] == null) {
                mFragmentManger[javaClass.canonicalName!!] = this
            }
            mRootView = view

            if (userVisibleHint) {
//                onFragmentFirstVisible()
                isFirstVisible = false
            }
//            onFragmentVisibleChange(userVisibleHint)
            onFragmentFirstVisible()
            isFragmentVisible = true
        }

        super.onViewCreated(view!!, savedInstanceState)
        getMvpDelegate()!!.onViewCreated(view,savedInstanceState)
        initView(view,savedInstanceState)
        initData()
    }

    fun setStatusBar(color:Int){
        TrustStatusBarUtils.getSingleton(mContext!!).setStatusBarColor(mActivity!!, color)
    }



    private fun setAndroidNativeLightStatusBar(activity: Activity, dark: Boolean) {
        val decor = activity.window.decorView
        if (dark) {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getMvpDelegate()!!.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        getMvpDelegate()!!.onStart()
    }

    override fun onResume() {
        super.onResume()
        getMvpDelegate()!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        getMvpDelegate()!!.onPause()
    }

    override fun onStop() {
        super.onStop()
        getMvpDelegate()!!.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        getMvpDelegate()!!.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState!!)
        getMvpDelegate()!!.onSaveInstanceState(outState)
    }

    @SuppressWarnings("deprecation")
    override fun onAttach(activity: Activity?) {
        mActivity = activity as AppCompatActivity?
        mContext = activity
        super.onAttach(activity)
    }


    override fun createPresenter(): P {
        return this.mPresenter!!
    }

    override fun getMvpView(): V {
        return this as V
    }

    override fun getPresenter(): P? {
        return this.mPresenter
    }

    override fun setPresenter(prensent: P) {
        this.mPresenter = prensent
    }

    protected fun startActivityResult(activity: Activity,clasz:Class<*>,code:Int){
        val intent:Intent = Intent(activity,clasz)
        startActivityForResult(intent,code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onTrustViewActivityResult(requestCode, resultCode, data)
    }


    protected fun baseSetOnClick(v:View,seconds:Long = 1000){
        RxView
                .clicks(v)
                .throttleFirst(seconds, TimeUnit.MILLISECONDS)
                .subscribe { baseResultOnClick(v) }
    }

    abstract fun baseResultOnClick(v:View)

    protected fun baseShowToast(msg:String?){
        if (activityIsClose()) {
            if (msg != null) {
                mActivity?.runOnUiThread {
                    Toast.makeText(mActivity,msg,Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    protected fun checkStringIsNullAndShowMsg(msg:String?,errorMsg:String):Boolean{
        return if (!TextUtils.isEmpty(msg)) {
            false
        }else{
            if (activityIsClose()) {
                Toast.makeText(mActivity,errorMsg,Toast.LENGTH_LONG).show()
            }
            true
        }
    }

    protected fun checkPwdIsError(msg:String?,errorMsg:String):Boolean{
        return if (!TextUtils.isEmpty(msg) && msg!!.length >= 6) {
            false
        }else{
            if (activityIsClose()) {
                Toast.makeText(mActivity,errorMsg,Toast.LENGTH_LONG).show()
            }
            true
        }
    }


    protected fun checkPhone(phone:String?,errorMsg:String):Boolean{
        return if (TrustStringUtils.isPhoneNumberValid(phone)) {
            true
        }else{
            if (activityIsClose()) {
                Toast.makeText(mActivity,errorMsg,Toast.LENGTH_LONG).show()
            }
            false
        }
    }



    protected  fun activityIsClose():Boolean{
        if(mActivity!=null && !mActivity!!.isFinishing){
            return true
        }
        return false
    }

    fun getTxString(id:Int):String?{
        if (mActivity != null) {
            return TrustTools.getString(mActivity!!,id)
        }else{
            return null
        }
    }

    fun checkData(status:String?,info:String?):Boolean{
        return if (status!= null || !status.equals("00")) {
            showToast(info)
            true
        }else{
            false
        }
    }

    private var isFragmentVisible:Boolean = false
    private var isFirstVisible:Boolean = false
    private var mReuseView:Boolean = false
    //setUserVisibleHint()在Fragment创建时会先被调用一次，传入isVisibleToUser = false
    //如果当前Fragment可见，那么setUserVisibleHint()会再次被调用一次，传入isVisibleToUser = true
    //如果Fragment从可见->不可见，那么setUserVisibleHint()也会被调用，传入isVisibleToUser = false
    //总结：setUserVisibleHint()除了Fragment的可见状态发生变化时会被回调外，在new Fragment()时也会被回调
    //如果我们需要在 Fragment 可见与不可见时干点事，用这个的话就会有多余的回调了，那么就需要重新封装一个
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        //有可能在生命周期外调用 防止控件未初始化报错
        if (mRootView == null) {
            return
        }

        if (isFirstVisible && isVisibleToUser) {
            onFragmentFirstVisible();
            isFirstVisible = false;
        }
        if (isVisibleToUser) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible) {
            isFragmentVisible = false;
            onFragmentVisibleChange(false);
        }
    }

    /**
     * 去除setUserVisibleHint()多余的回调场景，保证只有当fragment可见状态发生变化时才回调
     * 回调时机在view创建完后，所以支持ui操作，解决在setUserVisibleHint()里进行ui操作有可能报null异常的问题
     *
     * 可在该回调方法里进行一些ui显示与隐藏，比如加载框的显示和隐藏
     *
     * @param isVisible true  不可见 -> 可见
     * false 可见  -> 不可见
     */
    protected fun onFragmentVisibleChange(isVisible: Boolean) {
        onTrustFragmentVisibleChange(isVisible)
    }


    /**
     * 在fragment首次可见时回调，可在这里进行加载数据，保证只在第一次打开Fragment时才会加载数据，
     * 这样就可以防止每次进入都重复加载数据
     * 该方法会在 onFragmentVisibleChange() 之前调用，所以第一次打开时，可以用一个全局变量表示数据下载状态，
     * 然后在该方法内将状态设置为下载状态，接着去执行下载的任务
     * 最后在 onFragmentVisibleChange() 里根据数据下载状态来控制下载进度ui控件的显示与隐藏
     */
    protected fun onFragmentFirstVisible() {
        onTrustFragmentFirstVisible()
    }

    protected fun isFragmentVisible(): Boolean {
        return isFragmentVisible
    }


    private fun initVariable() {
        isFirstVisible = true
        isFragmentVisible = false
        mRootView = null
    }

    override fun onDestroy() {
        mFragmentManger.remove(javaClass.canonicalName)
        initVariable()
        super.onDestroy()
    }

    companion object {
         val mFragmentManger = hashMapOf<String, Fragment>()
    }


}