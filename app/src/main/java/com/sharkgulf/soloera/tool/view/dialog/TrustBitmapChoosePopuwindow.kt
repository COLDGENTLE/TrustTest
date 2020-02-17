package com.sharkgulf.soloera.tool.view.dialog

import android.content.Context
import android.view.View
import android.view.animation.Animation
import com.sharkgulf.soloera.R
import razerdp.basepopup.BasePopupWindow

/**
 *  Created by user on 2019/2/21
 */
class TrustBitmapChoosePopuwindow(context: Context) : BasePopupWindow(context) {
    private var mPopupWindowBitmapListener:PopupWindowBitmapListener? = null

    override fun onCreateContentView(): View {
        val createPopupById = createPopupById(R.layout.popupwindow_choose_bitmap_layout)
        createPopupById.findViewById<View>(R.id.popu_cancel_btn).setOnClickListener {
            dismiss()
        }
        createPopupById.findViewById<View>(R.id.popu_album_btn).setOnClickListener {
            mPopupWindowBitmapListener?.BitMApListener(false)
            dismiss()
        }
        createPopupById.findViewById<View>(R.id.popu_camera_btn).setOnClickListener {
            mPopupWindowBitmapListener?.BitMApListener(true)
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
    interface PopupWindowBitmapListener{
        fun BitMApListener(isCamera:Boolean)
    }


    fun setPopupWindowBitmapListener(popupWindowBitmapListener:PopupWindowBitmapListener):TrustBitmapChoosePopuwindow{
        mPopupWindowBitmapListener = popupWindowBitmapListener
        return this
    }
}