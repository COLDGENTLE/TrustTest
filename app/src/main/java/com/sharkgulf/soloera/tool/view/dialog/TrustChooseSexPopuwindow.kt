package com.sharkgulf.soloera.tool.view.dialog

import android.content.Context
import android.view.View
import android.view.animation.Animation
import com.sharkgulf.soloera.R
import razerdp.basepopup.BasePopupWindow

/**
 *  Created by user on 2019/2/21
 */
class TrustChooseSexPopuwindow(context: Context) : BasePopupWindow(context) {
    private var mPopupWindowSexListener:PopupWindowSexListener? = null

    override fun onCreateContentView(): View {
        val createPopupById = createPopupById(R.layout.popupwindow_choose_sex_layout)
        createPopupById.findViewById<View>(R.id.popu_sex_cancel_btn).setOnClickListener {
            dismiss()
        }
        createPopupById.findViewById<View>(R.id.popu_man_btn).setOnClickListener {
            mPopupWindowSexListener?.SexListener(1)
            dismiss()
        }
        createPopupById.findViewById<View>(R.id.popu_woman_btn).setOnClickListener {
            mPopupWindowSexListener?.SexListener(2)
            dismiss()
        }
        return createPopupById
    }

    override fun onCreateShowAnimation(): Animation {
        return getDefaultScaleAnimation(true)
    }

    override fun onCreateDismissAnimation(): Animation {
        return getDefaultScaleAnimation(false)
    }
    interface PopupWindowSexListener{
        fun SexListener(sex:Int)
    }


    fun setPopupWindowSexListener(popupWindowSexListener:PopupWindowSexListener):TrustChooseSexPopuwindow{
        mPopupWindowSexListener = popupWindowSexListener
        return this
    }
}