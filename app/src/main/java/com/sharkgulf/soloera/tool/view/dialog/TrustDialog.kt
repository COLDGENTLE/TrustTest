package com.sharkgulf.soloera.tool.view.dialog

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import android.view.View
import android.widget.ImageView
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.tool.animation.TrustAnimation

/*
 *Created by Trust on 2019/1/4
 */
class TrustDialog {
    private var mDialogFragment: DialogFragment? = null

    companion object{
        private var mDialog:TrustDialog? = null
        fun getInstant():TrustDialog{
            if (mDialog == null) {
                mDialog = TrustDialog()
            }
            return mDialog!!
        }
    }

    fun showDialog(manager: FragmentManager, tag:String = "test"): DialogFragment {
//        mDialogFragment?.dismiss()
        mDialogFragment = TrustDialogFragment.DialogBuilder()
                .setDialogLayout(R.layout.dialog_wait_layout)
                .setDialogListener(object : TrustDialogFragment.DialogBuilder.DialogListener {
                    override fun onCreateDialog(v: View): View {
                        val loadIc = v.findViewById<ImageView>(R.id.wait_load_ic)
                        TrustAnimation.getTrustAnimation().addRotateAnimation(noReset = false)
                                .startAnimation(loadIc)
                        return v } })
                .setIsCanceled(false)
                .Builde()
        mDialogFragment!!.show(manager,tag)
        return mDialogFragment!!
    }

    fun closeDialog(){
        TrustAnimation.getTrustAnimation().cancelAnimation()
        mDialogFragment?.dismiss()
    }
}