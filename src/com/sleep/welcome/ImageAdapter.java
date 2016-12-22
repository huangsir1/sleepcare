package com.sleep.welcome;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

public class ImageAdapter extends PagerAdapter {
	private Context context;
	private List<ImageView> all;
	public ImageAdapter(Context context, List<ImageView> all) {
		super();
		this.context = context;
		this.all = all;
	}

	@Override
	public int getCount() {
		return all.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}
	@Override
	public Object instantiateItem(View container, int position) {
		ViewPager vp=(ViewPager) container;
		ImageView imageView=all.get(position);
		vp.addView(imageView);
//		if(position==all.size()-1){
//			imageView.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					Intent intent=new Intent(context,UserRegisterActivity.class);
//					context.startActivity(intent);
//					((Activity) context).finish();
//				}
//
//			});
//		}
		return all.get(position);
 	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		ImageView imageView=(ImageView) object;
		ViewPager vp=(ViewPager) container;
		vp.removeView(imageView);
	}
	

}

