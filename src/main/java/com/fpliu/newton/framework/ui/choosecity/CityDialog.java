package com.fpliu.newton.framework.ui.choosecity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.fpliu.newton.R;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.framework.ui.choosecity.SideBar.OnTouchingLetterChangedListener;
import com.fpliu.newton.framework.ui.dialog.CustomDialog;

/**
 * 城市选择弹出框
 * 
 * @author 792793182@qq.com 2014-9-22
 *
 */
public final class CityDialog extends CustomDialog {

	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;

	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	private PinyinComparator pinyinComparator;
	
	private OnSelectedListenner onSelectedListenner;
	
	public CityDialog(Activity context) {
		super(context);
		
		//设置高度和宽度
		setWindowWidth(Environment.getInstance().getScreenWidth());
		setWindowHeight(Environment.getInstance().getScreenHeight());
		
		//设置模数度
		setDim(0.2f);
		
		View view = LayoutInflater.from(context).inflate(R.layout.choose_city, null);
		setContentView(view);
		
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) view.findViewById(R.id.sidrbar);
		dialog = (TextView) view.findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (ListView) view.findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				String city = ((SortModel) adapter.getItem(position)).getName();
				dismiss();
				if (onSelectedListenner != null) {
					onSelectedListenner.onSelected(city);
				}
			}
		});

		SourceDateList = filledData(context.getResources().getStringArray(R.array.cities));

		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(context, SourceDateList);
		sortListView.setAdapter(adapter);

		mClearEditText = (ClearEditText) view.findViewById(R.id.filter_edit);

		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
	
	private List<SortModel> filledData(String[] cityArray) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < cityArray.length; i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(cityArray[i]);
			String pinyin = characterParser.getSelling(cityArray[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr) != -1
						|| characterParser.getSelling(name).startsWith(filterStr)) {
					filterDateList.add(sortModel);
				}
			}
		}

		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}
	
	public void setOnSelectedListenner(OnSelectedListenner listenner) {
		onSelectedListenner = listenner;
	}
	
	public static interface OnSelectedListenner {
		void onSelected(String city);
	}
	
	@Override
	protected Animation getDefaultInAnimation() {
		return AnimationUtils.loadAnimation(getContext(), R.anim.push_left_in);
	}
	
	@Override
	protected Animation getDefaultOutAnimation() {
		return AnimationUtils.loadAnimation(getContext(), R.anim.back_right_out);
	}
}
