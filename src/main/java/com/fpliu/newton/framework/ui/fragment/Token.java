package com.fpliu.newton.framework.ui.fragment;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

/**
 * 令牌
 * 
 * @author 792793182@qq.com 2014-9-22
 *
 */
public final class Token implements Parcelable {

	/** 返回的Activity */
	private Class<? extends Activity> returnActivity;
	
	/** 返回的Fragment */
	private Class<? extends Fragment> returnFragment;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeSerializable(returnActivity);
		dest.writeSerializable(returnFragment);
	}
	
	public static final Parcelable.Creator<Token> CREATOR = new Parcelable.Creator<Token>() {

		@Override
		public Token createFromParcel(Parcel in) {
			Token token = new Token();
			token.returnActivity = (Class<? extends Activity>) in.readSerializable();
			token.returnFragment = (Class<? extends Fragment>) in.readSerializable();
			return null;
		}

		@Override
		public Token[] newArray(int size) {
			return new Token[size];
		}
	};
}
