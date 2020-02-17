package com.trust.demo.basis.trust.weight.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.trust.demo.basis.R



@SuppressLint("ValidFragment")
/*
 *Created by Trust on 2018/12/28
 */
class TrustDialogFragment(private val mBuilder:DialogBuilder) : DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val view = LayoutInflater.from(activity).inflate(mBuilder.dialogLayout(), null)
        mBuilder.dialogListener?.onCreateDialog(view)
        builder.setView(view)
        return builder.create()
    }


     class DialogBuilder{
        private var mLayout:Int = R.layout.dialog_error
        fun dialogLayout():Int{ return mLayout}
        fun setDialogLayout(layout:Int) : DialogBuilder{ mLayout = layout
            return this}
        var dialogListener:DialogListener? = null
        fun setDialogListener( listener:DialogListener) : DialogBuilder{
            dialogListener  = listener
            return this
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