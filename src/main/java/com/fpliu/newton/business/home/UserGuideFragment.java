package com.fpliu.newton.business.home;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fpliu.newton.R;
import com.fpliu.newton.framework.ui.fragment.BaseFragment;

/**
 * 用户引导界面
 * 
 * @author 792793182@qq.com 2014-9-22
 * 
 */
public class UserGuideFragment extends BaseFragment {
	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private ImageView imageView;
	private ImageView[] imageViews;
	private ViewGroup main;
	private ViewGroup group;

	@Override
	public RelativeLayout onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout rootView = super.onCreateView(inflater, container, savedInstanceState);
		
		pageViews = new ArrayList<View>();
		pageViews.add(inflater.inflate(R.layout.welcome, rootView, false));
		pageViews.add(inflater.inflate(R.layout.welcome2, rootView, false));
		pageViews.add(inflater.inflate(R.layout.welcome3, rootView, false));

		imageViews = new ImageView[pageViews.size()];
		main = (ViewGroup) inflater.inflate(R.layout.welcome_viewpager, rootView, false);

		group = (ViewGroup) main.findViewById(R.id.welcome_viewGroup);
		viewPager = (ViewPager) main.findViewById(R.id.welcome_guidePages);

		Context context = getActivity();
		
		for (int i = 0; i < pageViews.size(); i++) {
			imageView = new ImageView(context);
			imageView.setLayoutParams(new LayoutParams(20, 20));
			imageView.setPadding(20, 0, 20, 0);
			imageViews[i] = imageView;

			if (i == 0) {
				imageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.page_indicator);
			}

			group.addView(imageViews[i]);
		}

		setContentView(main);

		viewPager.setAdapter(new GuidePageAdapter());
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
		
		return rootView;
	}

	public void myclick(View v) {
		Context context = v.getContext();
		Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
		v.startAnimation(shake);
		
		finish();
	}
	
	class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}
	}
	
	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0].setBackgroundResource(R.drawable.page_indicator_focused);

				if (arg0 != i) {
					imageViews[i].setBackgroundResource(R.drawable.page_indicator);
				}
			}
		}
	}

}