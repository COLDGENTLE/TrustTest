package com.bs.trust.mapslibrary;

import android.app.Activity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;

import org.jetbrains.annotations.Nullable;

/**
 * Created by user on 2019/7/12
 */
@Route(path = "/lib/main")
public class LibTest extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_main);
    }
}
