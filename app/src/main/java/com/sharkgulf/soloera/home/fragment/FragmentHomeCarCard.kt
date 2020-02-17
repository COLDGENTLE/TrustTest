package com.sharkgulf.soloera.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sharkgulf.soloera.R

/*
 *Created by Trust on 2018/12/18
 */
class FragmentHomeCarCard : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_car_card,container,false)
    }
}