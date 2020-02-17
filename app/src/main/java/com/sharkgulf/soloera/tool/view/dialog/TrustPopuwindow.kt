package com.sharkgulf.soloera.tool.view.dialog

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.widget.RadioButton
import android.widget.RadioGroup
import com.sharkgulf.soloera.R
import razerdp.basepopup.BasePopupWindow

/**
 *  Created by user on 2019/1/22
 */
class TrustPopuwindow(context:Context) : BasePopupWindow(context) {
    companion object {
         var mapType:Int = 0
    }

    override fun onCreateShowAnimation(): Animation {
        return getDefaultScaleAnimation(true)
    }

    override fun onCreateDismissAnimation(): Animation {
        return getDefaultScaleAnimation(false)
    }

    private var mapRadioButton:RadioButton? = null
    private var wxMapRadioButton:RadioButton? = null
    private var theredMapRadioButton:RadioButton? = null
    private var radioGroup:RadioGroup? = null
    private var mMapPopuwindowListener:MapPopuwindowListener? = null
    private var radioButtonList:ArrayList<RadioButton> = arrayListOf()

    override fun onCreateContentView(): View {
        radioButtonList = arrayListOf()
        val createPopupById = createPopupById(R.layout.dialog_choose_map_tyoe_layout)
        mapRadioButton = createPopupById.findViewById<RadioButton>(R.id.radio_map_btn)
        wxMapRadioButton = createPopupById.findViewById<RadioButton>(R.id.radio_wxmap_btn)
        theredMapRadioButton = createPopupById.findViewById<RadioButton>(R.id.radio_theredmap_btn)
        radioButtonList.add(mapRadioButton!!)
        radioButtonList.add(wxMapRadioButton!!)
        radioButtonList.add(theredMapRadioButton!!)

        radioButtonList[mapType].isChecked = true
        radioGroup = createPopupById.findViewById<RadioGroup>(R.id.radiogroup)
        radioGroup?.setOnCheckedChangeListener { group, checkedId ->
            mapType = when (checkedId) {
                R.id.radio_map_btn-> {
                     0
                }
                R.id.radio_wxmap_btn->{
                    1
                }
                R.id.radio_theredmap_btn->{
                    2
                }else ->{
                    0
                }
            }
            mMapPopuwindowListener?.CallBack(mapType)
            dismiss()
        }
        return createPopupById
    }

    fun setClearMapType(){
        mapType = 0
    }

    fun setMapPopuwindowListener(mapPopuwindowListener:MapPopuwindowListener):TrustPopuwindow{
        mMapPopuwindowListener = mapPopuwindowListener
        return this
    }

    interface MapPopuwindowListener{
        fun CallBack(type:Int)
    }
}