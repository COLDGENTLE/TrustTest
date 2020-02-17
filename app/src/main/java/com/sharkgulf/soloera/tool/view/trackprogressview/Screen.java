package com.sharkgulf.soloera.tool.view.trackprogressview;

import android.content.Context;
import android.util.DisplayMetrics;

public class Screen {

	private static Screen mInstance;
	private float fontScale;

	private int mScreenWidth;
	private int mScreenHeight;
	private float mDensity;
	
	public static Screen getInstance(Context context) {
		if(mInstance==null) {
			mInstance = new Screen(context);
		}
		return mInstance;
	}
	
	public Screen(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		mDensity = dm.density;
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		fontScale = dm.scaledDensity;
	}
	
	public int dipToPx(float dip) {
        return (int) (dip * mDensity + 0.5f * (dip >= 0 ? 1 : -1));
	}
	
	public int pxToDip(int px) {
		return (int) (px / mDensity + 0.5f * (px >= 0 ? 1 : -1));
	}

	public  int px2sp(float pxValue) {
		return (int) (pxValue / fontScale + 0.5f);
	}

	public  int sp2px(float spValue) {
		return (int) (spValue * fontScale + 0.5f);
	}

	public int getScreenWidth() {
		return mScreenWidth;
	}

	public int getScreenHeight() {
		return mScreenHeight;
	}
}
