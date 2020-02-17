package com.sharkgulf.soloera.tool.view.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trust.demo.basis.R
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.Gravity


@SuppressLint("ValidFragment")
/*
 *Created by Trust on 2018/12/28
 */
class TrustDialogFragment(private val mBuilder:DialogBuilder) : DialogFragment(){

    //public static final int FILL_PARENT = -1;
//public static final int MATCH_PARENT = -1;
//public static final int WRAP_CONTENT = -2;




    override fun onStart() {
        super.onStart()
        val win = dialog.window
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(  ColorDrawable(Color.TRANSPARENT))

        val dm =  DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics( dm )

        val params = win.attributes
        params.gravity = Gravity.CENTER
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width =  ViewGroup.LayoutParams.WRAP_CONTENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        win.attributes = params
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val view = LayoutInflater.from(activity).inflate(mBuilder.dialogLayout(), null)
        mBuilder.dialogListener?.onCreateDialog(view)
        builder.setView(view)
        val create = builder.create()
        create.setCanceledOnTouchOutside(mBuilder.getIsCanceled())
        return create
    }


     class DialogBuilder{
        private var mLayout:Int = R.layout.dialog_error
         private var mIsCanceled:Boolean = true
        fun dialogLayout():Int{ return mLayout}
        fun setDialogLayout(layout:Int) : DialogBuilder{ mLayout = layout
            return this}
        var dialogListener:DialogListener? = null
        fun setDialogListener( listener:DialogListener) : DialogBuilder{
            dialogListener  = listener
            return this
        }

         fun setIsCanceled(isCanceled:Boolean):DialogBuilder{
             mIsCanceled = isCanceled
             return this
         }

         fun getIsCanceled():Boolean{
             return mIsCanceled
         }

         fun Builde(): DialogFragment {
             return TrustDialogFragment(this)
         }

        interface DialogListener {
            fun onCreateDialog(v:View):View
            interface DialogControllListener{
                fun dialogOnclikListener()
            }
        }
    }




}