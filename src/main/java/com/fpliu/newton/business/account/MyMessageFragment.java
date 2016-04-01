package com.fpliu.newton.business.account;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.fpliu.newton.R;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.framework.designpattern.Observable;
import com.fpliu.newton.framework.designpattern.Observer;
import com.fpliu.newton.framework.push.PushManager;
import com.fpliu.newton.framework.push.PushMessage;
import com.fpliu.newton.framework.ui.CustomAlertDialog;
import com.fpliu.newton.framework.ui.CustomDialog;
import com.fpliu.newton.framework.ui.StateView;
import com.fpliu.newton.framework.ui.UIUtil;
import com.fpliu.newton.framework.ui.fragment.BaseFragment;
import com.fpliu.newton.framework.ui.swipemenulistview.SwipeMenu;
import com.fpliu.newton.framework.ui.swipemenulistview.SwipeMenuCreator;
import com.fpliu.newton.framework.ui.swipemenulistview.SwipeMenuItem;
import com.fpliu.newton.framework.ui.swipemenulistview.SwipeMenuListView;
import com.fpliu.newton.framework.ui.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.fpliu.newton.framework.ui.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.fpliu.newton.framework.ui.toast.CustomToast;

/**
 * 我的消息界面
 * 
 * @author 792793182@qq.com 2014-11-26
 * 
 */
public class MyMessageFragment extends BaseFragment implements Observer<PushMessage> {
	private static final String TAG = MyMessageFragment.class.getSimpleName();

	/** 清除按钮 */
	private Button clearBtn;
	/** listView */
	private SwipeMenuListView listView;

	private SwipeAdapter mAdapter;

	private List<PushMessage> listMessage;

	private FinalDb finalDb;

	/*** 删除项 **/
	private final int ITEM_DELETE = 0;

	/*** 显示状态的视图 **/
	private StateView stateView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		PushManager.getInstance().registerObserver(this);
	}
	
	@Override
	public RelativeLayout onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout rootView = super.onCreateView(inflater, container, savedInstanceState);
		addContentView(inflater.inflate(R.layout.my_message, rootView, false));
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Context context = getActivity();
		DebugLog.v(TAG, "onViewCreate");
		setTitleText(R.string.myMessageFragment_title);

		finalDb = FinalDb.create(context, "pushMessage.db");

		listMessage = finalDb.findAll(PushMessage.class);

		if (listMessage.isEmpty())
			DebugLog.v(TAG, "empty");
		else {
			DebugLog.v(TAG, "size" + listMessage.size());
		}
		// 初始化view
		initView(context);

		// 设置监听swipListView
		initSwipeMenuListView(context);

		mAdapter = new SwipeAdapter();
		listView.setAdapter(mAdapter);

		// 设置stateView 加载进度

		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		lp.addRule(RelativeLayout.BELOW, ID_TITLE_BAR);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		stateView = new StateView(context);
		appendView(stateView, lp);

		// 判断是否有消息
		if (listMessage.isEmpty()) {
			stateView.showErrorNoButton(getResources().getString(R.string.myMessageFragment_untreated_message));
		} else {
			stateView.setVisibility(View.GONE);
		}

	}

	private void initView(Context context) {
		clearBtn = new Button(context);
		clearBtn.setText(getResources().getString(R.string.myMessageFragment_editor));
		clearBtn.setTextColor(Color.WHITE);
		clearBtn.setTextSize(18);
		clearBtn.setBackgroundResource(R.drawable.button_focused);

		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
				UIUtil.dip2px(context, 60), LayoutParams.MATCH_PARENT);
		lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp2.addRule(RelativeLayout.CENTER_VERTICAL);
		addViewInTitleBar(clearBtn, lp2);

		listView = (SwipeMenuListView) findViewById(R.id.mymessage_listview);

		// 注册按钮的清除事件
		clearBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (!listMessage.isEmpty()) {
					// 删除所有消息对话框
					alertClearAllMessageDialog();
				} else {
					CustomToast.makeText(getActivity(), R.string.myMessageFragment_message_inexistence, 2000).show();
				}

			}
		});
	}

	private void initSwipeMenuListView(final Context context) {

		// 创建菜单生成器
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {

				// 删除项
				SwipeMenuItem deleteItem = new SwipeMenuItem(context);
				// 背景顏色
				deleteItem.setBackground(new ColorDrawable(Color.rgb(181, 19,
						14)));
				// 寬度
				deleteItem.setWidth(UIUtil.dip2px(context, 90));
				// 图标
				deleteItem.setIcon(R.drawable.ic_delete);
				// 添加菜單
				menu.addMenuItem(deleteItem);
			}
		};
		// 设置菜单到listview
		listView.setMenuCreator(creator);

		// 菜单按钮事件
		listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {

				switch (index) {
				case ITEM_DELETE:
					// 删除消息
					DebugLog.v(TAG, "position" + position);
					DebugLog.v(TAG, "listView position" + listMessage.size());
					// 数据库中删除

					finalDb.delete(listMessage.get(position));

					// 判断list是否为空
					if (listMessage.isEmpty()) {
						DebugLog.v(TAG, "listMessage is empty");
						finish();
					}
					break;
				}
			}
		});

		// 设置SwipeListener
		listView.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
				DebugLog.v(TAG, "onSwipeStart");
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
				DebugLog.v(TAG, "onSwipeEnd");
			}
		});

		// 长点击事件
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				alertClearItemMessageDialog(position);

				return true;
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 显示详细信息
				alertMessageDialog(position);
			}
		});

	}

	/**
	 * 对话框
	 */
	private void alertMessageDialog(int position) {

		PushMessage item = listMessage.get(position);
		CustomAlertDialog.Builder builder = new CustomAlertDialog.Builder(
				getActivity());
		// 设置对话框颜色
		builder.setBackground(new ColorDrawable(Color.BLACK));
		builder.setCancelable(true);
		// 设置为不需要关闭按钮
		builder.setNeedCloseButton(false);

		String title = item.getNotification_title();
		String content = item.getAlert();
		if (!TextUtils.isEmpty(title)) {
			builder.setTitle(title);
		} else {
			builder.setTitle(R.string.myMessageFragment_message_detail);
		}

		if (!TextUtils.isEmpty(content)) {
			builder.setMessage(content);
		} else {
			builder.setTitle(R.string.myMessageFragment_message_content_inexistence);
		}

		builder.create().show();
	}

	/**
	 * 对话框
	 */
	private void alertClearAllMessageDialog() {
		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setMessage(R.string.myMessageFragment_message_delete_all);
		builder.setVisibility(false, true, true);

		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

						if (!listMessage.isEmpty()) {
							// 删除数据
							listMessage.clear();
							finalDb.deleteByWhere(PushMessage.class, "");
						}
						finish();
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// 退出界面
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	/**
	 * 对话框
	 */
	private void alertClearItemMessageDialog(final int position) {
		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setVisibility(false, true, true);
		builder.setMessage(R.string.myMessageFragment_message_delete_current);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

						if (!listMessage.isEmpty()) {
							// 删除数据
							// 数据库中删除
							finalDb.delete(listMessage.get(position));

							if (listMessage.isEmpty()) {
								DebugLog.v(TAG, "listMessage is empty");
								finish();
							}
							if (listMessage.isEmpty()) {
								finish();
							}
						}

					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// 退出界面
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	// 设置adaper
	private class SwipeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return listMessage.size();
		}

		@Override
		public PushMessage getItem(int position) {
			return listMessage.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {

				convertView = View.inflate(getActivity(),
						R.layout.item_list_message, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			PushMessage item = getItem(position);
			String content = item.getAlert();
			if (!TextUtils.isEmpty(content)) {
				holder.tv_content.setText(content);
			}

			return convertView;
		}

		private class ViewHolder {
			ImageView iv_icon;
			// TextView tv_title;
			TextView tv_content;

			public ViewHolder(View view) {
				iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				// tv_title = (TextView) view.findViewById(R.id.tv_title);
				tv_content = (TextView) view.findViewById(R.id.tv_content);
				view.setTag(this);
			}
		}
	}

	@Override
	public void update(Observable<PushMessage> observable, PushMessage... data) {
		DebugLog.v(TAG, "update()");
		
		listMessage.clear();
		listMessage = finalDb.findAll(PushMessage.class);
		mAdapter = null;
		mAdapter = new SwipeAdapter();
		DebugLog.v(TAG, "adapter size:" + mAdapter.getCount());

		listView.setAdapter(mAdapter);
		DebugLog.v(TAG, "size:" + listMessage.size());

		// 设置stateView为隐藏
		stateView.setVisibility(View.GONE);
	}
}
