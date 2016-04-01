package com.fpliu.newton.framework.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.EditText;
import android.widget.ImageView;

import com.fpliu.newton.R;

public class LoginEditTextEx extends EditText {

	/** 右边的删除按钮的背景 */
	private Drawable mDelBk;
	
	/** 左边标志的图片 */
	private Drawable mTitleBk;

	/** 删除按钮的内间距 */
	private int mDelOffset;

	/** 右边的删除按钮 */
	private ImageView mDelButton;
	
	/** 右边的删除按钮 */
	private ImageView mTitleButton;
	
	private boolean mDelButtonFocused;
	public LoginEditTextEx(Context context, Drawable delBg, int padding) {
		super(context);
		
		mDelBk = delBg;
		mDelOffset = padding;
		
		addTextChangedListener(mEditWatcher);
		creatDelbutton(context);
	}

	
	public LoginEditTextEx(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.EditTextEx);

		mTitleBk = a.getDrawable(R.styleable.EditTextEx_titleBk);
		mDelBk = a.getDrawable(R.styleable.EditTextEx_delBk);
		mDelOffset = a.getDimensionPixelSize(R.styleable.EditTextEx_delOffset, 8);

		a.recycle();
		addTextChangedListener(mEditWatcher);
		creatDelbutton(context);
		creatTitlebutton(context);
	}

	private void creatDelbutton(Context context) {
		mDelButton = new ImageView(context);
		setDelDrawable(mDelBk);
		checkDelButton();
	}
	
	private void creatTitlebutton(Context context) {
		mTitleButton = new ImageView(context);
		setTitleDrawable(mTitleBk);
	}

	public void setDelButtonVisibility(int visibility) {
		mDelButton.setVisibility(visibility);
		invalidate();
	}

	private OnClickListener mDelButtonClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setText("");
			invalidate();
		}
	};

	private TextWatcher mEditWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			checkDelButton();
		}
	};

	private void checkDelButton() {
		String textEdit = getText().toString();
		mDelButton.setVisibility(TextUtils.isEmpty(textEdit) ? View.GONE : View.VISIBLE);
	}

	public void setDelDrawable(Drawable drawable) {
		mDelBk = drawable;
		mDelButton.setBackgroundColor(Color.TRANSPARENT);
		mDelButton.setImageDrawable(mDelBk);
		mDelButton.setOnClickListener(mDelButtonClick);
		invalidate();
	}

	public void setTitleDrawable(Drawable drawable) {
		mTitleBk = drawable;
		mTitleButton.setBackgroundColor(Color.TRANSPARENT);
		mTitleButton.setImageDrawable(mTitleBk);
	}
	public Drawable getDelDrawable() {
		return mDelBk;
	}

	public void setDelOff(int offset) {
		mDelOffset = offset;
	}

	public int getDelOff() {
		return mDelOffset;
	}

	@Override
	public int getCompoundPaddingRight() {
		Rect paddingBk = new Rect();
		getBackground().getPadding(paddingBk);
		return super.getCompoundPaddingRight() + mDelButton.getWidth()
				+ mDelOffset - paddingBk.right;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		mDelButton.measure(0, 0);
		mTitleButton.measure(0, 0);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		int edit_w = getWidth();
		int edit_h = getHeight();

		int delButton_w = mDelButton.getMeasuredWidth();
		int delButton_h = mDelButton.getMeasuredHeight();

		mDelButton.layout(edit_w - delButton_w - mDelOffset,
				(edit_h - delButton_h) / 2, edit_w - mDelOffset, edit_h
						- (edit_h - delButton_h) / 2);
		
		int titleButton_w = mTitleButton.getMeasuredWidth();
		int titleButton_h = mTitleButton.getMeasuredHeight();

		mTitleButton.layout(edit_w - titleButton_w - mDelOffset,
				(edit_h - titleButton_h) / 2, edit_w - mDelOffset, edit_h
						- (edit_h - titleButton_h) / 2);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mDelButton.getVisibility() != View.GONE
				&& mDelButton.getVisibility() != View.INVISIBLE) {
			canvas.save();
			canvas.translate(getScrollX() + mDelButton.getLeft(), getScrollY()
					+ mDelButton.getTop());
			mDelButton.draw(canvas);
			canvas.restore();
		}
		
		mTitleButton.setVisibility(View.VISIBLE);
		if (mTitleButton.getVisibility() != View.GONE
				&& mTitleButton.getVisibility() != View.INVISIBLE) {
			canvas.save();
			canvas.translate(getScrollX(), getScrollY()
					+ mTitleButton.getTop());
			mTitleButton.draw(canvas);
			canvas.restore();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (isDelButtonHit(event)) {
				mDelButtonFocused = true;
				if (!mDelButton.isPressed()) {
					mDelButton.setPressed(true);
					invalidate();
				}
				return true;
			}
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (mDelButtonFocused) {
				boolean hit = isDelButtonHit(event);
				if (mDelButton.isPressed() != hit) {
					mDelButton.setPressed(hit);
					invalidate();
				}
				return true;
			}
		} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
			if (mDelButtonFocused) {
				boolean hit = isDelButtonHit(event);
				if (mDelButton.isPressed() != hit) {
					mDelButton.setPressed(hit);
					invalidate();
				}
				mDelButtonFocused = false;
				return true;
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (mDelButtonFocused) {
				if (mDelButton.isPressed() && mDelButtonClick != null)
					mDelButtonClick.onClick(mDelButton);
				if (mDelButton.isPressed()) {
					
					mDelButton.setPressed(false);
					invalidate();
				}
				mDelButtonFocused = false;
				return mDelButton.isPressed();
			}
		}

		return super.dispatchTouchEvent(event);
	}

	
	private boolean isDelButtonHit(MotionEvent event) {

		final Rect frame = new Rect();
		mDelButton.getHitRect(frame);
		return frame.contains((int) event.getX(), (int) event.getY());
	}
}
