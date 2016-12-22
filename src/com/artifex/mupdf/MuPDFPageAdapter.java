package com.artifex.mupdf;


import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MuPDFPageAdapter extends BaseAdapter {
	
	private final Context mContext;
	private final MuPDFCore mCore;
	/**
	 * SparseArray<E>采用了二分法方式代替HashMap<Integer,E>
	 * PointF 表示在二维平面中定义点的浮点 x 和 y 坐标的有序对
	 */
	private final SparseArray<PointF> mPageSizes = new SparseArray<PointF>();

	public MuPDFPageAdapter(Context c, MuPDFCore core) {
		mContext = c;
		mCore = core;
	}

	@Override
	public int getCount() {
		return mCore.countPages();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		final MuPDFPageView pageView;
		PointF pageSize = mPageSizes.get(position);
		
		if (convertView == null) {
			pageView = new MuPDFPageView(mContext, mCore, new Point(parent.getWidth(), parent.getHeight()));
		} else {
			pageView = (MuPDFPageView) convertView;
		}
		if (pageSize != null) {
			pageView.setPage(position, pageSize);
		} else {
			pageView.blank(position);
			AsyncTask<Void,Void,PointF> sizingTask = new AsyncTask<Void,Void,PointF>() {
				@Override
				protected PointF doInBackground(Void... arg0) {
					return mCore.getPageSize(position);
				}

				@Override
				protected void onPostExecute(PointF result) {
					super.onPostExecute(result);
					
					mPageSizes.put(position, result);
					// Check that this view hasn't been reused for
					// another page since we started
					if (pageView.getPage() == position)
						pageView.setPage(position, result);
				}
			};
			try
			{
				sizingTask.execute((Void)null);
			}
			catch (java.util.concurrent.RejectedExecutionException e)
			{
				// If we can't do it in the background, just
				// do it in the foreground.
				PointF result = mCore.getPageSize(position);
				mPageSizes.put(position, result);
				// Check that this view hasn't been reused for
				// another page since we started
				if (pageView.getPage() == position)
					pageView.setPage(position, result);
			}
		}
		return pageView;
	}
}
