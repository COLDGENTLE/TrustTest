package com.sharkgulf.soloera.tool.view.dialog

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.bindcar.fragment.FragmentBindCarSetCar
import com.sharkgulf.soloera.bindcar.fragment.FragmentBindCarSetCar.Companion.STATUS_2
import com.sharkgulf.soloera.module.bean.BsGetBindInfoBean
import com.sharkgulf.soloera.tool.config.*
import com.trust.demo.basis.trust.utils.TrustLogUtils
import razerdp.basepopup.BasePopupWindow

/**
 *  Created by user on 2019/10/14
 */
class TrustGeneralPurposePopupwindow {
    companion object{
        private var mTrustGeneralPurposePopupwindow:TrustGeneralPurposePopupwindow? = null

        fun getInstance():TrustGeneralPurposePopupwindow{
            if (mTrustGeneralPurposePopupwindow == null) {
                mTrustGeneralPurposePopupwindow = TrustGeneralPurposePopupwindow()
            }
            return mTrustGeneralPurposePopupwindow!!
        }
    }


    fun showOnlyBtnPopu(titleTx:String? = null,msgTx:String,btnTx:String,listener: PopupOnclickListener.OnlyBtnOnclickListener,isBack:Boolean = false){
        var mPopuwindow: BasePopupWindow? = null
        mPopuwindow = TrustBasePopuwindow.getPopuwindow(getContext(), R.layout.popupwindow_only_btn_layout,
                object : TrustBasePopuwindow.TrustPopuwindowOnclickListener {
            override fun resultPopuwindowViewListener(v: View?): View {
                v?.findViewById<TextView>(R.id.only_title_tx)?.text = titleTx
                v?.findViewById<TextView>(R.id.only_msg_tx)?.text = msgTx
                val btnTv = v?.findViewById<TextView>(R.id.only_btn)
                btnTv?.text = btnTx
                btnTv?.setOnClickListener { listener.onClickListener(mPopuwindow!!) }
                return v!!
            }
        })
        mPopuwindow.setBackgroundColor(Color.TRANSPARENT)
        mPopuwindow.setBackPressEnable(isBack)
        mPopuwindow.showPopupWindow()
    }

    fun showDoubleBtnPopu(titleTx:String? = null,msgTx:String? = null,btn1Tx:String,btn2Tx:String,doubleBtnOnclickListener: PopupOnclickListener.DoubleBtnOnclickListener){
        var mPopuwindow: BasePopupWindow? = null
        mPopuwindow = TrustBasePopuwindow.getPopuwindow(getBsActivityLifecycleCallbacks().getActivity(), R.layout.popupwindow_double_btn_layout, object : TrustBasePopuwindow.TrustPopuwindowOnclickListener {
            override fun resultPopuwindowViewListener(v: View?): View {
                if (titleTx != null) {
                    val title = v?.findViewById<TextView>(R.id.double_title_tx)
                    title?.visibility = View.VISIBLE
                    title?.text = titleTx
                }
                if (msgTx != null) {
                    val msg = v?.findViewById<TextView>(R.id.double_msg_tx)
                    msg?.visibility = View.VISIBLE
                    msg?.text = msgTx
                }
                val btn1Tv = v?.findViewById<TextView>(R.id.double_btn1_btn)
                val btn2Tv = v?.findViewById<TextView>(R.id.double_btn2_btn)
                btn1Tv?.setOnClickListener { mPopuwindow?.dismiss()
                    doubleBtnOnclickListener.onClickListener(true) }
                btn2Tv?.setOnClickListener { mPopuwindow?.dismiss()
                    doubleBtnOnclickListener.onClickListener(false) }
                btn1Tv?.text = btn1Tx
                btn2Tv?.text = btn2Tx

                return v!!
            }
        })
//        mPopuwindow.setBackgroundColor(Color.TRANSPARENT)
        mPopuwindow.showPopupWindow()
    }



    interface PopupOnclickListener{
        interface OnlyBtnOnclickListener{ fun onClickListener(view:BasePopupWindow)}
        interface DoubleBtnOnclickListener{ fun onClickListener(isBtn1:Boolean)}
        interface DownLoadBtnListener{ fun onClickListener(isBtn1:Boolean,v:BasePopupWindow) }
    }


    fun showWaitPopu(msgTx: String):BasePopupWindow{
        var mPopuwindow: BasePopupWindow? = null
        mPopuwindow = TrustBasePopuwindow.getPopuwindow(getContext(), R.layout.wait_layout, object : TrustBasePopuwindow.TrustPopuwindowOnclickListener {
            override fun resultPopuwindowViewListener(v: View?): View {
                v?.findViewById<TextView>(R.id.wait_layout_msg_tv)?.text = msgTx
                return v!!
            }
        })
        mPopuwindow.setBackgroundColor(Color.TRANSPARENT)
        mPopuwindow.setBackPressEnable(false)
        mPopuwindow.showPopupWindow()
        return mPopuwindow
    }


    fun showHelpPopu():BasePopupWindow{
        var mPopuwindow: BasePopupWindow? = null
        mPopuwindow = TrustBasePopuwindow.getPopuwindow(getContext(), R.layout.bind_car_help_layout, object : TrustBasePopuwindow.TrustPopuwindowOnclickListener {
            override fun resultPopuwindowViewListener(v: View?): View {

                return v!!
            }
        }).setPopupGravity(BasePopupWindow.GravityMode.ALIGN_TO_ANCHOR_SIDE,Gravity.BOTTOM).setBackgroundColor(Color.TRANSPARENT)
        mPopuwindow.setBackgroundColor(Color.TRANSPARENT)
        mPopuwindow.showPopupWindow()
        return mPopuwindow
    }

    private var mBlePopuwindow:BasePopupWindow? = null
    fun showBleConnectionPopu(mActivitiy: AppCompatActivity, y:Int, ping:String, bindInfoBean: BsGetBindInfoBean.DataBean.BindInfoBean):TrustGeneralPurposePopupwindow{
        dissBleConnectionPopu()
        FragmentBindCarSetCar.setType(STATUS_2,ping)
        FragmentBindCarSetCar.setBleBean(bindInfoBean)
        val findFragmentByTag = mActivitiy.supportFragmentManager.findFragmentByTag("1111")
        if (findFragmentByTag != null) {
            mActivitiy.supportFragmentManager.beginTransaction().remove(findFragmentByTag).commitAllowingStateLoss()
        }
        TrustLogUtils.d("showBleConnectionPopu","activity 有没有 fragment ：${mActivitiy.supportFragmentManager.findFragmentByTag("1111") != null}")


        if (mBlePopuwindow == null) {
            TrustBasePopuwindow.getPopuwindow(mActivitiy,R.layout.bind_car_ble_connection_layout,object : TrustBasePopuwindow.TrustPopuwindowOnclickListener{
                override fun resultPopuwindowViewListener(v: View?): View { return v!! }
            })
        }else{
            TrustBasePopuwindow.layoutId = R.layout.bind_car_ble_connection_layout
            TrustBasePopuwindow.mTrustPopuwindowOnclickListener = object : TrustBasePopuwindow.TrustPopuwindowOnclickListener {
                override fun resultPopuwindowViewListener(v: View?): View {
                    return LayoutInflater.from(mActivitiy).inflate(R.layout.bind_car_ble_connection_layout,null,false) } }
            mBlePopuwindow = TrustBasePopuwindow(mActivitiy)
        }
        mBlePopuwindow?.setBackgroundColor(Color.TRANSPARENT)
            mBlePopuwindow?.setBackgroundColor(Color.TRANSPARENT)
        mBlePopuwindow?.showPopupWindow(0,y)
        return this
    }


    fun dissBleConnectionPopu(){
        mBlePopuwindow?.dismiss()
    }


    class ListPopuAdapter(list :ArrayList<String>,listener:itemOnlickListener) : RecyclerView.Adapter<ListPopuAdapter.ListPopuViewHolder>(){
        private val mList  = list
        private val mlistener = listener
        override fun onCreateViewHolder(parent: ViewGroup, pos: Int): ListPopuViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_list_popu,null,false)
            val listPopuViewHolder = ListPopuViewHolder(v)
            listPopuViewHolder.itemView.setOnClickListener {
                mlistener.callBack(mList[listPopuViewHolder.adapterPosition])
            }
            return listPopuViewHolder
        }

        override fun getItemCount(): Int {
            return mList.size
        }

        override fun onBindViewHolder(holder: ListPopuViewHolder, pos: Int) {
            holder.mTextView?.text =  mList[pos]
            if (pos == mList.size-1) {
                holder.mLine?.visibility = View.INVISIBLE
            }
        }

        class ListPopuViewHolder(v:View): RecyclerView.ViewHolder(v){
            var mTextView:TextView? = null
            var mLine:View? = null
            init {
                mTextView = itemView.findViewById(R.id.item_list_popu_tv)
                mLine = itemView.findViewById(R.id.item_list_line)
            }
        }

        interface  itemOnlickListener{
            fun callBack(data:String)
        }
    }

    fun showListPopu(list: ArrayList<String>,listener:ListPopuAdapter.itemOnlickListener,title:String):BasePopupWindow{
        var mPopuwindow: BasePopupWindow? = null
        mPopuwindow = TrustBasePopuwindow.getPopuwindow(getContext(), R.layout.list_popu_layout, object : TrustBasePopuwindow.TrustPopuwindowOnclickListener {
            override fun resultPopuwindowViewListener(v: View?): View {
                v?.findViewById<TextView>(R.id.list_popu_title_tv)?.text = title
                v?.findViewById<View>(R.id.list_popu_cancel_btn)?.setOnClickListener { mPopuwindow?.dismiss() }
                val listPopuAdapter = ListPopuAdapter(list,object :ListPopuAdapter.itemOnlickListener{
                    override fun callBack(data: String) {
                        mPopuwindow?.dismiss()
                        listener.callBack(data)
                    }
                })
                val lp = LinearLayoutManager(v?.context)
                lp.orientation = LinearLayout.VERTICAL
                val recyclerView = v?.findViewById<RecyclerView>(R.id.list_popu_recycler)
                recyclerView?.adapter = listPopuAdapter
                recyclerView?.layoutManager = lp
                return v!!
            }
        }).setPopupGravity(BasePopupWindow.GravityMode.ALIGN_TO_ANCHOR_SIDE,Gravity.BOTTOM)
                .setBackgroundColor(Color.TRANSPARENT)
        mPopuwindow.setBackgroundColor(Color.TRANSPARENT)
        mPopuwindow.showPopupWindow()
        return mPopuwindow
    }




    fun showTrustOnlyBtnPopu(title:String? = null,msg:String? = null,btn1Tx:String? = null,listener: PopupOnclickListener.OnlyBtnOnclickListener):BasePopupWindow{
        var mPopuwindow: BasePopupWindow? = null
        mPopuwindow = TrustBasePopuwindow.getPopuwindow(getContext(), R.layout.trust_only_btn_layout, object : TrustBasePopuwindow.TrustPopuwindowOnclickListener {
            override fun resultPopuwindowViewListener(v: View?): View {

                var titleTx = v?.findViewById<TextView>(R.id.trust_only_title_tv)
                titleTx?.text = title
                if (title != null) {
                    titleTx?.visibility = View.VISIBLE
                }else{
                    titleTx?.visibility = View.GONE
                }


                var msgTx = v?.findViewById<TextView>(R.id.trust_only_msg_tv)
                msgTx?.text = msg
                if (msg != null) {
                    msgTx?.visibility = View.VISIBLE
                }else{
                    msgTx?.visibility = View.GONE
                }


                val btn = v?.findViewById<TextView>(R.id.trust_only_submint_btn)
                if (btn1Tx != null) {
                    btn?.text = btn1Tx
                }

                btn?.setOnClickListener {
                    listener.onClickListener(mPopuwindow!!)
                    mPopuwindow?.dismiss() }
                return v!!
            }
        }).setPopupGravity(BasePopupWindow.GravityMode.ALIGN_TO_ANCHOR_SIDE,Gravity.BOTTOM)
                .setBackgroundColor(Color.TRANSPARENT)
        mPopuwindow.setBackgroundColor(Color.TRANSPARENT)
        mPopuwindow.showPopupWindow()
        return mPopuwindow
    }

    fun showTrustDoubleBtnPopu(title:String? = null,msg:String? = null,btn1Tx:String? = null,btn2Tx:String? = null,listener: PopupOnclickListener.DoubleBtnOnclickListener):BasePopupWindow{
        var mPopuwindow: BasePopupWindow? = null
        mPopuwindow = TrustBasePopuwindow.getPopuwindow(getContext(), R.layout.trust_double_btn_layout, object : TrustBasePopuwindow.TrustPopuwindowOnclickListener {
            override fun resultPopuwindowViewListener(v: View?): View {
                val titleTx = v?.findViewById<TextView>(R.id.trust_double_title_tv)
                if (title != null) { titleTx?.visibility = View.VISIBLE
                }else{ titleTx?.visibility = View.GONE }
                titleTx?.text = title

                val msgTx = v?.findViewById<TextView>(R.id.trust_double_msg_tv)
                if (msg != null) { msgTx?.visibility = View.VISIBLE
                }else{ msgTx?.visibility = View.GONE }
                msgTx?.text = msg
                val btn1 = v?.findViewById<TextView>(R.id.trust_double_left_btn)
                if (btn1Tx != null) {
                    btn1?.text = btn1Tx
                }
                btn1?.setOnClickListener {
                    mPopuwindow?.dismiss()
                    listener.onClickListener(true) }

                var btn2 = v?.findViewById<TextView>(R.id.trust_double_right_btn)
                if (btn2Tx != null) {
                    btn2?.text = btn2Tx
                }
                btn2?.setOnClickListener {
                    mPopuwindow?.dismiss()
                    listener.onClickListener(false) }
                return v!!
            }
        }).setPopupGravity(BasePopupWindow.GravityMode.ALIGN_TO_ANCHOR_SIDE,Gravity.BOTTOM)
                .setBackgroundColor(Color.TRANSPARENT)
        mPopuwindow.setBackgroundColor(Color.TRANSPARENT)
        mPopuwindow.showPopupWindow()
        return mPopuwindow
    }

    private var downLoadPopuV:View? = null
    private var mPopuwindow: BasePopupWindow? = null
    fun showDownLoadPopu(title:String,version:String,msg:String,listener: PopupOnclickListener.DownLoadBtnListener,isMustLayout:Boolean = false):BasePopupWindow{

        mPopuwindow = TrustBasePopuwindow.getPopuwindow(getContext(), R.layout.bs_download_layout, object : TrustBasePopuwindow.TrustPopuwindowOnclickListener {
            override fun resultPopuwindowViewListener(v: View?): View {
                if (isMustLayout) {
                    v?.findViewById<View>(R.id.download_show_layout)?.visibility = View.GONE
                    v?.findViewById<View>(R.id.download_show_must_layout)?.visibility = View.VISIBLE

                    v?.findViewById<TextView>(R.id.show_must_msg_tx)?.text = msg
                    val dec = v?.findViewById<TextView>(R.id.show_must_dec_tx)
                    dec?.text = dec!!.text.toString() + version
                    v?.findViewById<View>(R.id.show_must_down_btn)?.setOnClickListener {
                        listener.onClickListener(false,mPopuwindow!!) }
                }else{
                    v?.findViewById<View>(R.id.download_show_layout)?.visibility = View.VISIBLE
                    v?.findViewById<View>(R.id.download_show_must_layout)?.visibility = View.GONE
                    v?.findViewById<TextView>(R.id.show_msg_tx)?.text = msg
                    val dec = v?.findViewById<TextView>(R.id.show_dec_tx)
                    dec?.text = dec!!.text.toString() + version

                    v?.findViewById<View>(R.id.show_one_btn)?.setOnClickListener {
                        listener.onClickListener(true,mPopuwindow!!) }
                    v?.findViewById<View>(R.id.show_two_btn)?.setOnClickListener {
                        listener.onClickListener(false,mPopuwindow!!) }
                }


                downLoadPopuV = v
                return v!!
            }
        }).setPopupGravity(BasePopupWindow.GravityMode.ALIGN_TO_ANCHOR_SIDE,Gravity.BOTTOM)
                .setBackgroundColor(Color.TRANSPARENT)
        mPopuwindow?.setBackgroundColor(Color.TRANSPARENT)
        mPopuwindow?.showPopupWindow()
        return mPopuwindow!!
    }



    fun changeDownLoadPopuLoading(max:Int,progress:Int){
        if (downLoadPopuV != null) {
            downLoadPopuV!!.findViewById<View>(R.id.download_show_layout).visibility = View.GONE
            downLoadPopuV!!.findViewById<View>(R.id.download_error_layout).visibility = View.GONE
            downLoadPopuV!!.findViewById<View>(R.id.download_show_must_layout).visibility = View.GONE
            downLoadPopuV!!.findViewById<View>(R.id.download_loading_layout).visibility = View.VISIBLE
            val progressBar = downLoadPopuV!!.findViewById<ProgressBar>(R.id.loading_progressbar)
            val progressTv = downLoadPopuV!!.findViewById<TextView>(R.id.loading_progress_tv)
            progressBar.max = max
            progressBar.progress = progress
            progressTv.text = "$progress/$max"
        }
    }

    fun changDownLoadPopuError(error:String,listener: PopupOnclickListener.DownLoadBtnListener,isMustLayout: Boolean = false){
        if (downLoadPopuV != null) {
            downLoadPopuV!!.findViewById<View>(R.id.download_show_layout).visibility = View.GONE
            downLoadPopuV!!.findViewById<View>(R.id.download_show_must_layout).visibility = View.GONE
            downLoadPopuV!!.findViewById<View>(R.id.download_loading_layout).visibility = View.GONE
            val errorLayout = downLoadPopuV!!.findViewById<View>(R.id.download_error_layout)
            errorLayout.visibility = View.VISIBLE
            downLoadPopuV!!.findViewById<TextView>(R.id.error_msg_tx).text = error
            var oneBtn = errorLayout.findViewById<View>(R.id.error_one_btn)
            if (isMustLayout) {
                oneBtn.visibility = View.GONE
            }else{
                oneBtn.visibility = View.VISIBLE
            }
            oneBtn.setOnClickListener {
                listener.onClickListener(true,mPopuwindow!!) }
            errorLayout.findViewById<View>(R.id.error_two_btn).setOnClickListener { listener.onClickListener(false,mPopuwindow!!) }


        }
    }
}