package com.bs.trust.mapslibrary

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import android.view.View
import com.bs.trust.mapslibrary.tool.TrustDialogFragment
import com.trust.maplibrary.callback.MapDialogCallBack

/**
 *  Created by user on 2019/10/31
 */
class MapDialogUtils {

    private var mDialogFragment: DialogFragment? = null
    fun showOffine1 (manager: FragmentManager, tag:String, listener:MapDialogCallBack.OffinePopuCallBack){
        mDialogFragment = TrustDialogFragment.DialogBuilder()
                .setDialogLayout(R.layout.offine_popu_1_layout)
                .setDialogListener(object : TrustDialogFragment.DialogBuilder.DialogListener {
                    override fun onCreateDialog(v: View): View {
                        v.findViewById<View>(R.id.offpop_1_cancel_btn).setOnClickListener {
                            listener.callback(true)
                            mDialogFragment?.dismiss() }
                        v.findViewById<View>(R.id.offpop_1_confirm_btn).setOnClickListener {
                            listener.callback(false)
                            mDialogFragment?.dismiss()
                        }
                        return v
                    } })
                .Builde()
        mDialogFragment?.show(manager,tag)
    }
}