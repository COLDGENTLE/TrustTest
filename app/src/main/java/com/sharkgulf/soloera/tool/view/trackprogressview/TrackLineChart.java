package com.sharkgulf.soloera.tool.view.trackprogressview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import java.util.List;

/**
 * Created by mht on 2017/8/30.
 */

public class TrackLineChart extends View {

    private float mMaxYAxis = 100;
    private float mMinYAxis = 0;

    private int mAxisColor;
    private int mAxisWidth;

    private int mYMarkColor;
    private int mYMarkWidth;

    private int mLineColor;
    private int mLineWarnColor;
    private int mLineWidth;

    private int mProgressColor;
    private int mProgressWidth;

    private TrackProgressView.OnProgressListener mOnProgressListener;
    private TrackProgressView.OnProgressDataListener mOnProgressDataListener;

    private float mYMark = -1;
    private List<Data> mData;
    private Data mMinData;
    private Data mMaxData;

    private int mMaxProgress;
    private int mProgress = 0;  //当前数据下标

    public TrackLineChart(Context context) {
        super(context);
        init(context);
    }

    public TrackLineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TrackLineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TrackLineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mAxisColor = Color.parseColor("#0072ff");
        mLineWarnColor = Color.parseColor("#0072ff");
        mAxisWidth = Screen.getInstance(context).dipToPx(1.5f);

        mYMarkColor = Color.parseColor("#0072ff");
        mYMarkWidth = Screen.getInstance(context).dipToPx(1f);

        mLineColor = Color.parseColor("#0072ff");
        mLineWidth = Screen.getInstance(context).dipToPx(2f);

        mProgressColor = Color.parseColor("#0072ff");
        mProgressWidth = Screen.getInstance(context).dipToPx(1.5f);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        int width = getWidth();
        int height = getHeight();

        //画横纵坐标轴
        paint.setStrokeWidth(mAxisWidth);
        paint.setColor(mAxisColor);
        canvas.drawLine(mAxisWidth/2, 0, mAxisWidth/2, height, paint);
        canvas.drawLine(0, height - mAxisWidth/2, width - mAxisWidth/2, height, paint);

        //画data
        int size = getDataSize();
        if(size < 2) {
            return;
        }
        float space = (float) width / (size - 1);
        Path path = new Path();
        path.moveTo(0, dataToY(height, mData.get(0).y));
        for (int i=1; i<size; i++) {
            Data item = mData.get(i);
            path.lineTo(i * space, dataToY(height, item.y));
        }
        if(mYMark > mMaxData.y) {
            paint.setStrokeWidth(mLineWidth);
            paint.setColor(mLineColor);
            canvas.drawPath(path, paint);
        } else if(mYMark < mMinData.y) {
            paint.setStrokeWidth(mLineWidth);
            paint.setColor(mLineWarnColor);
            canvas.drawPath(path, paint);
        } else {
            //画超速警戒线
            float yMarkHeight = dataToY(height, mYMark);
            paint.setStrokeWidth(mYMarkWidth);
            paint.setColor(mYMarkColor);
            canvas.drawLine(0, yMarkHeight, width, yMarkHeight, paint);

            paint.setStrokeWidth(mLineWidth);
            paint.setColor(mLineColor);
            canvas.drawPath(path, paint);

            //待优化
            int saveCount = canvas.saveLayer(mAxisWidth, 0, width - mAxisWidth, yMarkHeight, paint, Canvas.ALL_SAVE_FLAG);
            paint.setColor(mLineWarnColor);
            canvas.drawPath(path, paint);
            canvas.restoreToCount(saveCount);
        }

        //画progress
        paint.setStrokeWidth(mProgressWidth);
        paint.setColor(mProgressColor);
        float progressX = progressToX(width, mProgress);
        canvas.drawLine(progressX, 0, progressX, height, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pro = event.getX() / getWidth();
        int progress = (int) (mMaxProgress * pro);
        setProgress(progress);
        if(mOnProgressListener != null) {
            mOnProgressListener.onProgress(mProgress);
        }
        return true;
    }

    private float progressToX(int width, float progress) {
        return (width - mProgressWidth) * progress / mMaxProgress + mProgressWidth/2;
    }

    private float dataToY(int height, float data) {
        return height * (mMaxYAxis - data) / (mMaxYAxis - mMinYAxis);
    }

    public void setMaxYAxis(int maxYAxis) {
        mMaxYAxis = maxYAxis;
        invalidate();
    }

    public void setProgress(int progress) {
        if(progress < 0) {
            progress = 0;
        }
        if(progress > mMaxProgress) {
            progress = mMaxProgress;
        }
        mProgress = progress;

        float progressWidth = (getWidth() - mProgressWidth) * mProgress / mMaxProgress + mProgressWidth/2;
        mOnProgressDataListener.onProgressData(progressWidth, mData.get(mProgress));
        invalidate();
    }

    public void setData(List<Data> data, Data min, Data max) {
        mData = data;
        mMaxProgress = getDataSize() - 1;
        mMinData = min;
        mMaxData = max;

        float dis = max.y - min.y;
        if(dis == 0) {
            mMaxYAxis = max.y + max.y * 0.1f;
            mMinYAxis = min.y - min.y * 0.1f;
        } else {
            mMaxYAxis = max.y + dis * 0.1f;
            mMinYAxis = min.y - dis * 0.1f;
        }
        invalidate();
    }

    public int getDataSize() {
        return mData==null ? 0 : mData.size();
    }

    public void setYMark(float mark) {
        mYMark = mark;
        invalidate();
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setOnProgressListener(TrackProgressView.OnProgressListener l) {
        mOnProgressListener = l;
    }

    void setOnProgressDataListener(TrackProgressView.OnProgressDataListener l) {
        mOnProgressDataListener = l;
    }

    public static class Data {
        float y;
        String selectYDesc;
        String selectXDesc;
        Object tag;

        public Data(float y) {
            this.y = y;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }

        public void setSelectYDesc(String selectYDesc) {
            this.selectYDesc = selectYDesc;
        }

        public void setSelectXDesc(String selectXDesc) {
            this.selectXDesc = selectXDesc;
        }
    }
}
