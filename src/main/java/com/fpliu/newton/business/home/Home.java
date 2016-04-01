package com.fpliu.newton.business.home;

import android.os.Bundle;

import com.fpliu.newton.framework.bitmap.TakePhotoFragment;
import com.fpliu.newton.framework.ui.fragment.BaseFragmentActivity;

public class Home extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getFragmentMediator().addFragment(this, new TakePhotoFragment());
	}
}
