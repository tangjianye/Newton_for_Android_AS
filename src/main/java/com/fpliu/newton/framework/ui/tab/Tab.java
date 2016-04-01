package com.fpliu.newton.framework.ui.tab;

import android.support.v4.app.Fragment;

/**
 * Tab实体
 * 
 * @author 792793182@qq.com 2015-06-12
 * 
 */
public final class Tab {

	/** Tab页的唯一标志 */
	private int id;

	/** Tab页的文字 */
	private String text;
	
	/** Tab页对应的Fragment */
	private Fragment fragment;
	
	public Tab(int id, String text, Fragment fragment) {
		this.id = id;
		this.text = text;
		this.fragment = fragment;
	}

	public int getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public Fragment getFragment() {
		return fragment;
	}

	@Override
	public String toString() {
		return "Tab [id=" + id + ", text=" + text + ", fragment=" + fragment
				+ "]";
	}
}