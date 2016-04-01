package com.fpliu.newton.framework.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fpliu.newton.R;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.base.Environment;

/**
 * 自定义AlertDialog 注意：
 * 1、Dialog的Window的背景设置成了透明，如需要，只能在contentView上设置背景，调用builder.
 * setBackground(Drawable background)方法
 * 1、Dialog的模态效果设置成了透明，如需要，只能通过调用dialog.getWindow
 * ().getAttributes().dimAmount设置，范围[0,1]
 * 
 * @author fpliu@iflytek.com
 * @date 2012.07.19
 */
public class CustomAlertDialog extends Dialog {

	private static final String TAG = "CustomAlertDialog";

	private View contentView;
	private Animation inAnimation;
	private Animation outAnimation;

	public CustomAlertDialog(Context context) {
		this(context, R.style.CustomAlertDialog);
	}

	public CustomAlertDialog(Context context, int theme) {
		super(context, theme);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}

	@Override
	public void show() {
		DebugLog.d(TAG, "show()");

		if (null == inAnimation) {
			inAnimation = getDefaultInAnimation();
		}
		contentView.startAnimation(inAnimation);

		super.show();
	}

	@Override
	public void dismiss() {
		DebugLog.d(TAG, "dismiss()");

		if (null == outAnimation) {
			outAnimation = getDefaultOutAnimation();
		}

		outAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				DebugLog.d(TAG, "onAnimationStart()");
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				DebugLog.d(TAG, "onAnimationRepeat()");
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				DebugLog.d(TAG, "onAnimationEnd()");
				CustomAlertDialog.super.dismiss();
			}
		});
		contentView.startAnimation(outAnimation);
	}

	@Override
	public void setContentView(View contentView) {
		super.setContentView(contentView);
		this.contentView = contentView;
	}

	@Override
	public void setContentView(View contentView, LayoutParams params) {
		super.setContentView(contentView, params);
		this.contentView = contentView;
	}

	public View getContentView() {
		return contentView;
	}

	public void setinAnimation(Animation inAnimation) {
		this.inAnimation = inAnimation;
	}

	public void setOutAnimation(Animation outAnimation) {
		this.outAnimation = outAnimation;
	}

	/**
	 * show的默认动画，渐变
	 */
	protected Animation getDefaultInAnimation() {
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
		alphaAnimation.setFillAfter(true);
		alphaAnimation.setDuration(500);
		return alphaAnimation;
	}

	/**
	 * dismiss的默认动画，渐变
	 */
	protected Animation getDefaultOutAnimation() {
		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
		alphaAnimation.setDuration(500);
		return alphaAnimation;
	}

	public static class Builder {

		private Context context;
		private View contentView;
		private CharSequence title;
		private CharSequence message;

		private CharSequence positiveButtonText;
		private CharSequence neutualButtonText;
		private CharSequence negativeButtonText;
		private OnClickListener positiveButtonClickListener;
		private OnClickListener neutralButtonClickListener;
		private OnClickListener negativeButtonClickListener;
		private OnClickListener closeButtonClickListener;

		private Animation inAnimation;
		private Animation outAnimation;

		private CustomAlertDialog dialog;
		private Drawable background;

		private boolean cancelable = true;
		private boolean needCloseButton = true;

		private View rootView;

		public Builder(Context context) {
			this.context = context;
		}

		public boolean isCancelable() {
			return cancelable;
		}

		public Builder setCancelable(boolean cancelable) {
			this.cancelable = cancelable;
			return this;
		}

		public Builder setTitle(CharSequence title) {
			this.title = title;
			return this;
		}

		public Builder setTitle(int title) {
			return setTitle(context.getText(title));
		}

		public Builder setMessage(CharSequence message) {
			this.message = message;
			return this;
		}

		public Builder setMessage(int message) {
			return setMessage(context.getText(message));
		}

		public Builder setContentView(View contentView) {
			this.contentView = contentView;
			return this;
		}

		public Builder setBackground(Drawable background) {
			this.background = background;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(int positiveButtonText,
				OnClickListener listener) {
			return setPositiveButton(
					(String) context.getText(positiveButtonText), listener);
		}

		public Builder setNeutralButton(String neutralButtonText,
				OnClickListener listener) {
			this.neutualButtonText = neutralButtonText;
			this.neutralButtonClickListener = listener;
			return this;
		}

		public Builder setNeutralButton(int neutralButtonText,
				OnClickListener listener) {
			return setNeutralButton(
					(String) context.getText(neutralButtonText), listener);
		}

		public Builder setNegativeButton(String negativeButtonText,
				OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
				OnClickListener listener) {
			return setNegativeButton(
					(String) context.getText(negativeButtonText), listener);
		}

		public Builder setInAnimation(Animation inAnimation) {
			this.inAnimation = inAnimation;
			return this;
		}

		public Builder setOutAnimation(Animation outAnimation) {
			this.outAnimation = outAnimation;
			return this;
		}

		public void setNeedCloseButton(boolean needCloseButton) {
			this.needCloseButton = needCloseButton;
		}
		
		public void setNeedCloseButton(boolean needCloseButton, OnClickListener listener) {
			this.needCloseButton = needCloseButton;
			if (needCloseButton) {
				this.closeButtonClickListener = listener;
			}
		}

		/**
		 * 创建对话框中的内容
		 */
		public CustomAlertDialog create() {
			final CustomAlertDialog dialog = new CustomAlertDialog(context);

			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

			LayoutInflater inflater = LayoutInflater.from(context);
			rootView = inflater.inflate(R.layout.viafly_custom_alertdialog, null);

			// 放布局的容器
			LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.custom_dialog_layout);
			if (null != background) {
				linearLayout.setBackgroundDrawable(background);
			}

			// 关闭按钮
			ImageButton closeButton = (ImageButton) rootView
					.findViewById(R.id.custom_dialog_close_btn);

			// 如果需要显示关闭按钮
			if (needCloseButton) {
				closeButton.setVisibility(View.VISIBLE);
				closeButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (null != closeButtonClickListener) {
							closeButtonClickListener.onClick(dialog, "");
						}
						dialog.dismiss();
					}
				});
			} else {
				closeButton.setVisibility(View.GONE);

				RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) linearLayout
						.getLayoutParams();
				lp.leftMargin = 0;
				lp.rightMargin = 0;
				lp.topMargin = 0;
				lp.bottomMargin = 0;
				linearLayout.setLayoutParams(lp);
			}

			TextView titleTextView = (TextView) rootView
					.findViewById(R.id.custom_dialog_title);
			if (null != title) {
				titleTextView.setText(title);
			} else {
				titleTextView.setVisibility(View.GONE);
			}

			if (null != message) { 
				TextView messageTextView = (TextView) rootView
						.findViewById(R.id.custom_dialog_message);
				messageTextView.setText(message);
			} else if (null != contentView) {
				LinearLayout contentLayout = (LinearLayout) rootView
						.findViewById(R.id.custom_dialog_content);
				contentLayout.removeAllViews();
				contentLayout.addView(contentView, layoutParams);
			}

			View buttonPanel = rootView
					.findViewById(R.id.custom_dialog_button_panel);

			Button positiveButton = (Button) rootView
					.findViewById(R.id.custom_dialog_positiveButton);
			Button neutualButton = (Button) rootView
					.findViewById(R.id.custom_dialog_neutralButton);
			Button negativeButton = (Button) rootView
					.findViewById(R.id.custom_dialog_negativeButton);
			// 如果三个按钮都没有设置
			if (null == positiveButtonText && null == neutualButtonText
					&& null == negativeButtonText) {
				buttonPanel.setVisibility(View.GONE);
			} else {
				buttonPanel.setVisibility(View.VISIBLE);

				setPositiveButton(positiveButton, dialog);
				setNeutralButton(neutualButton, dialog);
				setNegativeButton(negativeButton, dialog);
			}

			dialog.setinAnimation(inAnimation);
			dialog.setOutAnimation(outAnimation);
			dialog.setContentView(rootView, layoutParams);
			dialog.setCancelable(cancelable);

			this.dialog = dialog;

			return dialog;
		}

		/**
		 * 显示左边的按钮
		 */
		private void setPositiveButton(Button positiveButton,
				final Dialog dialog) {
			if (positiveButtonText != null) {
				positiveButton.setVisibility(View.VISIBLE);
				positiveButton.setText(positiveButtonText);
				positiveButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (positiveButtonClickListener != null) {
							positiveButtonClickListener.onClick(dialog,
									DialogInterface.BUTTON_POSITIVE);
						}
						dialog.dismiss();
					}
				});
			} else {
				positiveButton.setVisibility(View.GONE);
			}
		}

		/**
		 * 显示中间的按钮
		 */
		private void setNeutralButton(Button neutralButton, final Dialog dialog) {
			if (neutualButtonText != null) {
				neutralButton.setVisibility(View.VISIBLE);
				neutralButton.setText(neutualButtonText);
				neutralButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (neutralButtonClickListener != null) {
							neutralButtonClickListener.onClick(dialog,
									DialogInterface.BUTTON_NEUTRAL);
						}
						dialog.dismiss();
					}
				});
			} else {
				neutralButton.setVisibility(View.GONE);
			}
		}

		/**
		 * 设置右边的按钮
		 */
		private void setNegativeButton(Button negativeButton,
				final Dialog dialog) {
			if (negativeButtonText != null) {
				negativeButton.setVisibility(View.VISIBLE);
				negativeButton.setText(negativeButtonText);
				negativeButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (negativeButtonClickListener != null) {
							negativeButtonClickListener.onClick(dialog,
									DialogInterface.BUTTON_NEGATIVE);
						}
						dialog.dismiss();
					}
				});
			} else {
				negativeButton.setVisibility(View.GONE);
			}
		}

		/**
		 * 显示对话框
		 */
		public CustomAlertDialog show() {
			if (null == dialog) {
				dialog = create();
			}
			if (!dialog.isShowing()) {
				dialog.show();
			}

			return dialog;
		}

		public CustomAlertDialog show(View anchorView) {
			return show(anchorView, 0, 0);
		}

		public CustomAlertDialog show(View anchorView, int xoff, int yoff) {
			int[] locationOfViewOnScreen = new int[2];
			// 获取此view在屏幕上的位置
			anchorView.getLocationOnScreen(locationOfViewOnScreen);

			int screenHeight = Environment.getInstance().getScreenHeight();

			int gravity = Gravity.LEFT | Gravity.BOTTOM;
			int x = locationOfViewOnScreen[0] + xoff;
			int y = screenHeight - locationOfViewOnScreen[1] + yoff;

			return show(gravity, x, y);
		}

		public CustomAlertDialog show(int gravity, int x, int y) {
			if (null == dialog) {
				dialog = create();
			}

			if (!dialog.isShowing()) {
				WindowManager.LayoutParams lp = dialog.getWindow()
						.getAttributes();

				if (gravity == Gravity.NO_GRAVITY) {
					gravity = Gravity.TOP | Gravity.LEFT;
				}
				lp.gravity = gravity;
				lp.x = x;
				lp.y = y;

				dialog.show();
			}

			return dialog;
		}
		
		public Context getContext() {
			return context;
		}
	}

	public interface OnClickListener {
		public void onClick(DialogInterface dialog, Object object);
	}

}