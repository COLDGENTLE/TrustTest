package com.sharkgulf.soloera.home.user.cars

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.module.bean.BsDeleteCarBean
import com.sharkgulf.soloera.tool.BeanUtils
import com.trust.demo.basis.base.TrustMVPActivtiy
import kotlinx.android.synthetic.main.activity_user_cars.*
import androidx.recyclerview.widget.GridLayoutManager
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.module.bean.BsGetCarInfoBean
import com.sharkgulf.soloera.mvpview.cars.ICars
import com.sharkgulf.soloera.presenter.cars.CarsPresenter
import com.trust.demo.basis.trust.utils.TrustAnalysis
import kotlinx.android.synthetic.main.tite_layout.*


class CarsActivity : TrustMVPActivtiy<ICars, CarsPresenter>(),ICars{
    private var mBsGetCarInfoBean:BsGetCarInfoBean? = null
    private var mUserCarsAdapter:CarsAdapter? = null
    override fun getLayoutId(): Int {
        return  R.layout.activity_user_cars
    }

    override fun initView(savedInstanceState: Bundle?) {

        baseSetOnClick(title_back_btn)
        mUserCarsAdapter = CarsAdapter(itemOnClikListener)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        user_cars_recycler.layoutManager = layoutManager
        user_cars_recycler.adapter = mUserCarsAdapter

        user_cars_recycler.addItemDecoration(SpacesItemDecoration(20,Color.parseColor("#ececec")))


        title_tx.text = "车库"
    }

    override fun initData() {
        val map1  = hashMapOf<String,Any>()
        map1["user_id"] = TrustAppConfig.userId
        getPresenter()!!.requestCarInfo(map1)
    }

    override fun baseResultOnClick(v: View) {
        when (v.id) {

            R.id.title_back_btn->{
                finish()
            }
            else -> {
            }
        }
    }




    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
    }

    override fun diassDialog() {
    }

    override fun showToast(msg: String?) {
        com.sharkgulf.soloera.tool.config.showToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    override fun resultSuccess(msg: String) {
    }

    override fun resultError(msg: String) {
        showToast(msg)
    }

    override fun createPresenter(): CarsPresenter {
        return CarsPresenter()
    }


    override fun resultDeleteCar(bean: BsDeleteCarBean?) {
        if(BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!,this)){
            showToast("删除车辆成功")
        }
    }


    class SpacesItemDecoration(private val space: Int,val color:Int) : RecyclerView.ItemDecoration() {
    private var mDivider: Drawable? =null
        override fun getItemOffsets(outRect: Rect, view: View,
                                    parent: RecyclerView, state: RecyclerView.State) {
//            outRect.left = space
//            outRect.right = space
            if (color!=0) {
                mDivider = ColorDrawable(color)
            }

            if (parent.getChildAdapterPosition(view) < 5) {
                outRect.bottom = space
            }

            // Add top margin only for the first item to avoid double space between items
//            if (parent.getChildPosition(view) == 0)
//                outRect.top = space
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val layoutManager = parent.layoutManager as LinearLayoutManager
            //没有子view或者没有没有颜色直接return
            if (mDivider == null || layoutManager.childCount == 0) {
                return
            }
            var left: Int
            var right: Int
            var top: Int
            var bottom: Int
            val childCount = parent.childCount
            if (layoutManager.orientation == GridLayoutManager.VERTICAL) {
                for (i in 0 until childCount - 1) {
                    val child = parent.getChildAt(i)
                    val params = child.layoutParams as RecyclerView.LayoutParams
                    //将有颜色的分割线处于中间位置
                    val center = (layoutManager.getTopDecorationHeight(child) - space) / 2
                    //计算下边的
                    left = layoutManager.getLeftDecorationWidth(child)
                    right = parent.width - layoutManager.getLeftDecorationWidth(child)
                    top = (child.bottom.toFloat() + params.bottomMargin.toFloat() + center).toInt()
                    bottom = top + space
                    mDivider?.setBounds(left, top, right, bottom)
                    mDivider?.draw(c)
                }
            } else {
                for (i in 0 until childCount - 1) {
                    val child = parent.getChildAt(i)
                    val params = child.layoutParams as RecyclerView.LayoutParams
                    //将有颜色的分割线处于中间位置
                    val center = (layoutManager.getLeftDecorationWidth(child) - space) / 2
                    //计算右边的
                    left = (child.right.toFloat() + params.rightMargin.toFloat() + center).toInt()
                    right = left + space
                    top = layoutManager.getTopDecorationHeight(child)
                    bottom = parent.height - layoutManager.getTopDecorationHeight(child)
                    mDivider?.setBounds(left, top, right, bottom)
                    mDivider?.draw(c)
                }
            }
        }
    }



    private val itemOnClikListener = object :CarsAdapter.ItemOnClikListener{
        override fun onClikListener(bean: BsGetCarInfoBean.DataBean.BikesBean) {
            CarInfoActivity.StartActivity(this@CarsActivity,TrustAnalysis.resultString(bean))
        }
    }

    override fun resultCarInfo(bean: BsGetCarInfoBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
            mBsGetCarInfoBean = bean
            changeUi()
        }
    }

    private fun changeUi() {
        mUserCarsAdapter?.setList(mBsGetCarInfoBean)
        mUserCarsAdapter?.notifyDataSetChanged()
    }
}
