package com.fpliu.newton.framework.share;

import java.util.ArrayList;

/**
 * 
 * 分享内容
 * 
 * @author 792793182@qq.com 2015-02-04
 */
public final class ShareContent {

	private static final String SHARE_TEXT = "牛顿";
	private static final String SHARE_ICON_URL = "";
	private static final String SHARE_LINK_URL = "";
	
	/** 分享的主题 */
	private String title = "牛顿";
	
	/** 分享的具体内容 */
	private String summary = SHARE_TEXT;
	
	/** 分享的小图标 */
	private String iconUrl = SHARE_ICON_URL;
	
	/** 详情连接 */
	private String linkUrl = SHARE_LINK_URL;
	
	/** 截图列表 */
	private ArrayList<String> images;
	
	private String where = "home";

	public ShareContent() {
		images = new ArrayList<String>();
		images.add(SHARE_ICON_URL);
	}
	
	public String getTitle() {
		return title;
	}

	public String getSummary() {
		return summary + linkUrl;
	}

	public String getIconUrl() {
		return iconUrl;
	}
	
	public String getLinkUrl() {
		return linkUrl;
	}

	public ArrayList<String> getImages() {
		return images;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public void addImage(String image) {
		images.add(image);
	}
	
	public void addImages(ArrayList<String> images) {
		images.addAll(images);
	}
	
	public void setImages(ArrayList<String> images) {
		if (images != null) {
			images.add(0, SHARE_ICON_URL);
			this.images = images;
		}
	}
	
	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	@Override
	public String toString() {
		return "ShareContent [title=" + title + ", summary=" + summary
				+ ", iconUrl=" + iconUrl + ", linkUrl=" + linkUrl + ", images="
				+ images + ", where=" + where + "]";
	}
}
