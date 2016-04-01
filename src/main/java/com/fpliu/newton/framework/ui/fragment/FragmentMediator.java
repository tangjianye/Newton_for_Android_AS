package com.fpliu.newton.framework.ui.fragment;

import java.util.Stack;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.fpliu.newton.base.DebugLog;

/**
 * 多个BaseFragment的调停者
 * 
 * @author 792793182@qq.com 2014-9-22
 *
 */
public final class FragmentMediator {

	private static final String TAG = FragmentMediator.class.getSimpleName();
	
	private Stack<BaseFragment> mFragments;
	
	
	public FragmentMediator() {
		mFragments = new Stack<BaseFragment>();
	}
	
	public synchronized void push(BaseFragment fragment) {
		mFragments.push(fragment);
		fragment.setMediator(this);
	}
	
	public synchronized BaseFragment pop() {
		return mFragments.pop();
	}
	
	public synchronized BaseFragment peek() {
		return mFragments.peek();
	}
	
	public synchronized boolean isEmpty() {
		return mFragments.isEmpty();
	}
	
	public synchronized int getCount() {
		return mFragments.size();
	}
	
	public synchronized boolean isTop(BaseFragment fragment) {
		return peek() == fragment;
	}
	
	public synchronized void destroy() {
		mFragments.clear();
	}

	public synchronized void addFragment(FragmentActivity activity, BaseFragment fragment) {
		addFragment(activity, fragment, false);
	}
	
	public synchronized void addFragment(FragmentActivity activity, BaseFragment fragment, boolean needFinishCurrentFragment) {
		DebugLog.d(TAG, "addFragment() " +  mFragments.toString());
		
		if (activity == null || fragment == null) {
			return;
		}
		
		//如果还没有添加过任何的Fragment，就直接加入
		if (mFragments.isEmpty()) {
			FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
	        fragmentTransaction.add(BaseFragment.ID_CONTENT_VIEW, fragment);
	        fragmentTransaction.commitAllowingStateLoss();
	        push(fragment);
	        return;
		}
		
		//如果已经添加过了Fragment，要判断现在要加入的Fragment是否已经在显示队列中
		boolean exsit = false;
		
		for (BaseFragment baseFragment : mFragments) {
			if (baseFragment.getClass() == fragment.getClass()) {
				exsit = true;
				break;
			}
		}
		
		if (exsit) {
			BaseFragment fragment2 = null;
			while (!mFragments.isEmpty() && (fragment2 = mFragments.peek()).getClass() != fragment.getClass()) {
				fragment2.finish(false);
			}
			try {
				fragment2.onResume();
			} catch (Exception e) {
				DebugLog.e(TAG, "", e);
			}
		} else {
			if (mFragments.isEmpty()) {
				FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
				fragmentTransaction.add(BaseFragment.ID_CONTENT_VIEW, fragment);
				fragmentTransaction.addToBackStack(null);
		        fragmentTransaction.commitAllowingStateLoss();
		        push(fragment);
			} else {
				if (needFinishCurrentFragment) {
					//先关闭旧的
					BaseFragment currentFragment = pop();
					currentFragment.finish(false);
				}
				
				FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
				fragmentTransaction.add(BaseFragment.ID_CONTENT_VIEW, fragment);
				fragmentTransaction.addToBackStack(null);
		        fragmentTransaction.commitAllowingStateLoss();
		        push(fragment);
			}
		}
	}
	
	public synchronized void replaceFragment(FragmentActivity activity, BaseFragment fragment) {
		if (activity == null || fragment == null) {
			return;
		}
		
		//如果还没有添加过任何的Fragment，就直接加入
		if (mFragments.isEmpty()) {
			FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
			fragmentTransaction.add(BaseFragment.ID_CONTENT_VIEW, fragment);
	        fragmentTransaction.commitAllowingStateLoss();
	        push(fragment);
	        return;
		} else {
			FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(BaseFragment.ID_CONTENT_VIEW, fragment);
	        fragmentTransaction.addToBackStack(null);
	        fragmentTransaction.commitAllowingStateLoss();
	        push(fragment);
		}
	}
	
	public synchronized void removeFragment(FragmentActivity activity, BaseFragment fragment) {
		if (mFragments != null && !mFragments.isEmpty() && isTop(fragment)) {
			pop();
			FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
	        fragmentTransaction.remove(fragment);
	        fragmentTransaction.commitAllowingStateLoss();
		}
	}
}
