package com.fpliu.newton.business.setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.SimpleExpandableListAdapter;

import com.fpliu.newton.R;
import com.fpliu.newton.framework.ui.fragment.BaseFragment;

/**
 * 帮助页面
 * 
 * @author 792793182@qq.com 2014-9-22
 * 
 */
public final class HelpFragment extends BaseFragment {

	@Override
	public RelativeLayout onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout rootView = super.onCreateView(inflater, container,
				savedInstanceState);
		addContentView(inflater.inflate(R.layout.help, rootView, false));
		setTitleText(R.string.setting_help);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ExpandableListView helpEx = (ExpandableListView) findViewById(R.id.helpEx);
		// 去掉默认的图标
		helpEx.setGroupIndicator(null);
				
		Context context = getActivity();

		List<Map<String, String>> groups = new ArrayList<Map<String, String>>();
		Map<String, String> group1 = new HashMap<String, String>();
		group1.put("group", getString(R.string.aboutus));
		Map<String, String> group2 = new HashMap<String, String>();
		group2.put("group", getString(R.string.aboutus));
		Map<String, String> group3 = new HashMap<String, String>();
		group3.put("group", getString(R.string.aboutus));
		Map<String, String> group4 = new HashMap<String, String>();
		group4.put("group", getString(R.string.aboutus));
		groups.add(group1);
		groups.add(group2);
		groups.add(group3);
		groups.add(group4);

		List<Map<String, String>> child1 = new ArrayList<Map<String, String>>();
		Map<String, String> child1data1 = new HashMap<String, String>();
		child1data1.put("child", getString(R.string.aboutus));
		child1.add(child1data1);

		List<Map<String, String>> child2 = new ArrayList<Map<String, String>>();
		Map<String, String> child1data2 = new HashMap<String, String>();
		child1data2.put("child", getString(R.string.aboutus));
		child2.add(child1data2);

		List<Map<String, String>> child3 = new ArrayList<Map<String, String>>();
		Map<String, String> child1data3 = new HashMap<String, String>();
		child1data3.put("child", getString(R.string.aboutus));
		child3.add(child1data3);

		List<Map<String, String>> child4 = new ArrayList<Map<String, String>>();
		Map<String, String> child1data4 = new HashMap<String, String>();
		child1data4.put("child", getString(R.string.aboutus));
		child4.add(child1data4);

		List<List<Map<String, String>>> childs = new ArrayList<List<Map<String, String>>>();
		childs.add(child1);
		childs.add(child2);
		childs.add(child3);
		childs.add(child4);

		SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
				context, groups, R.layout.help_group, new String[] { "group" },
				new int[] { R.id.groupTo }, childs, R.layout.help_child,
				new String[] { "child" }, new int[] { R.id.childTo });

		helpEx.setAdapter(adapter);
		helpEx.setSelector(R.drawable.help_ex_selector);
	}

}
