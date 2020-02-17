package com.sharkgulf.soloera.tool.view.trackprogressview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sharkgulf.soloera.R;

import java.util.List;

/**
 * Progress：代表时间单位分钟
 * @author m
 */
public class TrackProgressView extends FrameLayout {
	private static final String TAG = "TrackProgressBar";

	private TrackLineChart mTrackLineChart;
	private List<TrackLineChart.Data> mData;

	private TextView mtvXaxisLable;
	private TextView mtvYaxisLable;

	private TextView mtvProgressXdesc;
	private TextView mtvProgressYdesc;

	private float mInitMarginLeft;

	OnProgressDataListener mProgressDataListener = new OnProgressDataListener() {
		@Override
		public void onProgressData(float progressWidth, TrackLineChart.Data data) {
			mtvProgressXdesc.setText(data.selectXDesc);
			mtvProgressYdesc.setText(data.selectYDesc);

			RelativeLayout.LayoutParams progressXdesclp = (RelativeLayout.LayoutParams) mtvProgressXdesc.getLayoutParams();
			RelativeLayout.LayoutParams progressYdesclp = (RelativeLayout.LayoutParams) mtvProgressYdesc.getLayoutParams();
			progressXdesclp.leftMargin = (int) (mInitMarginLeft + progressWidth);
			progressYdesclp.leftMargin = progressXdesclp.leftMargin;
			mtvProgressXdesc.setX(mInitMarginLeft + progressWidth);
			mtvProgressYdesc.setX(progressXdesclp.leftMargin);
//			mtvProgressXdesc.setLayoutParams(progressXdesclp);
//			mtvProgressYdesc.setLayoutParams(progressYdesclp);
		}
	};

	public TrackProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public TrackProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TrackProgressView(Context context) {
		super(context);
		init(context);
	}

	private void init(final Context context) {
		final View contentView = LayoutInflater.from(context).inflate(R.layout.view_track_progress, null);
		addView(contentView);
		mtvProgressXdesc = (TextView) contentView.findViewById(R.id.tv_progress_xdesc);
		mtvProgressYdesc = (TextView) contentView.findViewById(R.id.tv_progress_ydesc);
		mtvXaxisLable = (TextView) contentView.findViewById(R.id.tv_xaxis_lable);
		mtvYaxisLable = (TextView) contentView.findViewById(R.id.tv_yaxis_lable);
		mTrackLineChart = (TrackLineChart) contentView.findViewById(R.id.trackLineChart);
		mTrackLineChart.setOnProgressDataListener(mProgressDataListener);

		mInitMarginLeft = ((RelativeLayout.LayoutParams)mtvXaxisLable.getLayoutParams()).leftMargin;
	}

	public void setData(List<TrackLineChart.Data> data) {
		mData = data;
		if(mData.size() > 0) {
			TrackLineChart.Data min = data.get(0);
			TrackLineChart.Data max = min;
			for (TrackLineChart.Data item : data) {
				if(item.y < min.y) {
					min = item;
				}
				if(item.y > max.y) {
					max = item;
				}
			}
			mtvXaxisLable.setText(mData.get(mData.size()-1).selectXDesc);
			mtvYaxisLable.setText(max.selectYDesc);
			mTrackLineChart.setData(data, min, max);
		}
	}

	public void setProgress(int progress) {
		mTrackLineChart.setProgress(progress);
	}

	/**
	 * 设置y轴警告线
	 *
	 * @param mark
	 */
	public void setYMark(float mark) {
		mTrackLineChart.setYMark(mark);
	}

	/**
	 * 设置y轴警告线
	 *
	 * @param maxYAxis
	 */
	public void setMaxYAxis(int maxYAxis) {
		mTrackLineChart.setMaxYAxis(maxYAxis);
	}

	public void setOnProgressListener(OnProgressListener l) {
		mTrackLineChart.setOnProgressListener(l);
    }

    public interface OnProgressListener {
        void onProgress(int progress);
	}

	interface OnProgressDataListener {
		void onProgressData(float progressWidth, TrackLineChart.Data data);
	}
}