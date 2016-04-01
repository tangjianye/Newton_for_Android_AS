package com.fpliu.newton.framework.ui.popup;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fpliu.newton.framework.ui.UIUtil;
import com.fpliu.newton.framework.ui.adapter.Item;
import com.fpliu.newton.framework.ui.adapter.ItemAdapter;

/**
 * 自定义PopupWindow
 * 
 * @author 792793182@qq.com 2015-06-19
 *
 */
public class SingleListPopup<T> extends CustomPopup {

	public SingleListPopup(Activity activity, List<T> items, final OnResult<T> onResult) {
		super(activity);
		
		if (items == null) {
			return;
		}

		ListView listView = new ListView(activity);
		listView.setCacheColorHint(Color.TRANSPARENT);
		//注意：下面两行代码的顺序不能变
		listView.setDivider(new ColorDrawable(Color.parseColor("#d1d1d1")));
		listView.setDividerHeight(1);
		
		List<ItemProxy<T>> itemProxies = new ArrayList<ItemProxy<T>>();
		for (int i = 0; i < items.size(); i++) {
			itemProxies.add(new ItemProxy<T>(items.get(i)));
		}
		
		final ItemProxyAdapter adapter = new ItemProxyAdapter(itemProxies);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ItemProxy<T> itemProxy = adapter.getItem(position);
				
				//钩子
				SingleListPopup.this.onItemClick(parent, view, position, itemProxy.getItem(), id);
				
				//回调
				if (onResult != null) {
					onResult.onResult(itemProxy.getItem());
				}
				
				dismiss();
			}
		});
		
		setContentView(listView);
		
		setHeight(getTotalHeightofListView(listView));
	}
	
	protected void onItemClick(AdapterView<?> parent, View view, int position, T item, long id) {
		
	}
	
	public View getView(int position, T item, View convertView, ViewGroup parent) {
		//默认实现
		TextView textView = new TextView(parent.getContext());
		textView.setText(item.toString());
		textView.setTextSize(16);
		textView.setTextColor(Color.BLACK);
		int padding = UIUtil.dip2px(parent.getContext(), 10);
		textView.setPadding(padding, padding, padding, padding);
		return textView;
	}

	public final int getTotalHeightofListView(ListView listView) {
		ListAdapter adapter = listView.getAdapter();
		if (adapter == null) {
			return 0;
		}
		int totalHeight = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			View convertView = adapter.getView(i, null, listView);
			convertView.measure(0, 0);
			totalHeight += convertView.getMeasuredHeight();
		}
		totalHeight += listView.getDividerHeight() * (adapter.getCount() - 1);
		return totalHeight;
	}

	private final class ItemProxyAdapter extends ItemAdapter<ItemProxy<T>> {

		protected ItemProxyAdapter(List<ItemProxy<T>> items) {
			super(items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ItemProxy<T> itemProxy = getItem(position);
			return SingleListPopup.this.getView(position, itemProxy.getItem(), convertView, parent);
		}
		
	}
	
	private static final class ItemProxy<I> implements Item {
		private I item;
		
		public ItemProxy(I item) {
			this.item = item;
		}
		
		public I getItem() {
			return item;
		}
	}
	
	public static interface OnResult<T> {

		void onResult(T item);
	}
	
	@Override
	protected Animation getInAnimation() {
		AlphaAnimation inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setInterpolator(new AccelerateInterpolator());
        inAnimation.setFillAfter(true);
        inAnimation.setDuration(300);
		return inAnimation;
	}
	
	@Override
	protected Animation getOutAnimation() {
		AlphaAnimation outAnimation = new AlphaAnimation(1f, 0f);
        outAnimation.setInterpolator(new DecelerateInterpolator());
        outAnimation.setDuration(300);
		return outAnimation;
	}
}
