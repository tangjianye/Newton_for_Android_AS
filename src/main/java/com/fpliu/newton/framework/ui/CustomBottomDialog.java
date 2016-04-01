package com.fpliu.newton.framework.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fpliu.newton.R;

/**
 * 自定义对话框(底部显示)
 * 
 * @author 792793182@qq.com 2014年11月4日
 * 
 */
public class CustomBottomDialog extends Dialog {

	public CustomBottomDialog(Context context) {
		super(context);
	}

	public CustomBottomDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String title;

		private String positiveButtonText;
		private String negativeButtonText;
		private String cancelButtonText;

		private LinearLayout alt_lin;

		private boolean alt_st = true;

		private DialogInterface.OnClickListener positiveButtonClickListener;
		private DialogInterface.OnClickListener negativeButtonClickListener;
		private DialogInterface.OnClickListener cancelButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setCancelButton(int cancelButtonText,
				DialogInterface.OnClickListener listener) {
			this.cancelButtonText = (String) context.getText(cancelButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setCancelButton(String cancelButtonText,
				DialogInterface.OnClickListener listener) {
			this.cancelButtonText = cancelButtonText;
			this.cancelButtonClickListener = listener;
			return this;
		}

		public CustomBottomDialog create() {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CustomBottomDialog dialog = new CustomBottomDialog(context,
					R.style.Dialog);
			View layout = inflater.inflate(R.layout.dialog_data_layout, null);

			this.alt_lin = (LinearLayout) layout.findViewById(R.id.alt_lin);

			if (("").equals(title) || title == null) {
				alt_st = false;
			}
			if (this.alt_st)
				this.alt_lin.setVisibility(View.VISIBLE);
			else
				this.alt_lin.setVisibility(View.GONE);

			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			dialog.setCanceledOnTouchOutside(false);
			// set the dialog title
			((TextView) layout.findViewById(R.id.dialog_title)).setText(title);
			// set the confirm button

			if (positiveButtonText != null) {
				((TextView) layout.findViewById(R.id.positiveButtonText))
						.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					((TextView) layout.findViewById(R.id.positiveButtonText))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.positiveButtonText).setVisibility(
						View.GONE);
			}
			// set the cancel button
			if (negativeButtonText != null) {
				((TextView) layout.findViewById(R.id.negativeButtonText))
						.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					((TextView) layout.findViewById(R.id.negativeButtonText))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.negativeButtonText).setVisibility(
						View.GONE);
			}

			if (cancelButtonText != null) {
				((TextView) layout.findViewById(R.id.cancelButtonText))
						.setText(cancelButtonText);
				if (cancelButtonClickListener != null) {
					((TextView) layout.findViewById(R.id.cancelButtonText))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									cancelButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEUTRAL);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.cancelButtonText).setVisibility(
						View.GONE);
			}

			// set the content message
			dialog.setContentView(layout);
			return dialog;
		}

	}
}
