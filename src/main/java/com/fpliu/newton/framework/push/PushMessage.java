package com.fpliu.newton.framework.push;

import net.tsz.afinal.annotation.sqlite.Table;

/**
 * 推送的消息实体类
 * 
 * @author 792793182@qq.com 2014-12-4
 * 
 */
@Table(name = "PushMessage")
public final class PushMessage {

	/*** 用户名 **/
	private int uuid;

	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	/*** 主键id **/
	private int id;

	/***
	 * Action - cn.jpush.android.intent.REGISTRATION SDK 向 JPush Server 注册所得到的注册
	 * 全局唯一的 ID ，可以通过此 ID 向对应的客户端发送消息和通知。
	 **/
	private String registration_id;

	/***
	 * Action - cn.jpush.android.intent.MESSAGE_RECEIVED SDK
	 * 对自定义消息，只是传递，不会有任何界面上的展示。 如果开发者想推送自定义消息 Push，则需要在 AndroidManifest.xml 里配置此
	 * Receiver action，并且在自己写的 BroadcastReceiver 里接收处理。 保存服务器推送下来的消息的标题,对应 API
	 * 消息内容的 title 字段,对应 Portal 推送消息界面上的“标题”字段（可选）
	 **/
	private String title;
	/*** 保存服务器推送下来的消息内容。对应 API 消息内容的 message 字段。对应 Portal 推送消息界面上的"消息内容”字段。 **/
	private String message;
	/*** 保存服务器推送下来的内容类型,对应 API 消息内容的 content_type 字段 **/
	private String extra;
	/*** 富媒体通消息推送下载后的文件路径和文件名 **/
	private String richpush_file_path;

	/***
	 * 唯一标识消息的 ID,
	 * 可用于上报统计等,cn.jpush.android.intent.NOTIFICATION_RECEIVED中也包含，cn.
	 * jpush.android.intent.NOTIFICATION_OPENED也包含
	 **/
	private String msg_id;

	/***
	 * Action - cn.jpush.android.intent.NOTIFICATION_RECEIVED 保存服务器推送下来的通知的标题,对应
	 * API 通知内容的 n_title 字段,对应 Portal
	 * 推送通知界面上的“通知标题”字段，cn.jpush.android.intent.NOTIFICATION_OPENED也包含
	 **/
	private String notification_title;

	/***
	 * 保存服务器推送下来的通知内容,对应 API 通知内容的 n_content 字段,对应 Portal
	 * 推送通知界面上的“通知内容”字段,cn.jpush.android.intent.NOTIFICATION_OPENED也包含
	 **/
	private String alert;

	/***
	 * 保存服务器推送下来的附加字段。这是个 JSON 字符串。对应 API 通知内容的 n_extras 字段。对应 Portal
	 * 推送通知界面上的“自定义内容”字段，cn.jpush.android.intent.NOTIFICATION_OPENED也包含
	 **/
	private String notification_extra;

	/***
	 * 通知栏的Notification
	 * ID，可以用于清除Notification，cn.jpush.android.intent.NOTIFICATION_OPENED也包含
	 **/
	private String notification_id;

	/*** 保存服务器推送下来的内容类型,对应 API 消息内容的 content_type 字段 **/
	private String content_type;

	/*** 富媒体通知推送下载的HTML的文件路径,用于展现WebView。 **/
	private String richpush_html_path;

	/***
	 * 富媒体通知推送下载的图片资源的文件名,多个文件名用 “，” 分开。 与
	 * “JPushInterface.EXTRA_RICHPUSH_HTML_PATH” 位于同一个路径
	 **/

	private String richpush_html_res;

	/***
	 * Action - cn.jpush.android.intent.NOTIFICATION_OPENED,如果开发者在
	 * AndroidManifest.xml 里未配置此 receiver action，那么，SDK 会默认打开应用程序的主
	 * Activity，相当于用户点击桌面图标的效果。
	 **/

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRegistration_id() {
		return registration_id;
	}

	public void setRegistration_id(String registration_id) {
		this.registration_id = registration_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getRichpush_file_path() {
		return richpush_file_path;
	}

	public void setRichpush_file_path(String richpush_file_path) {
		this.richpush_file_path = richpush_file_path;
	}

	public String getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}

	public String getNotification_title() {
		return notification_title;
	}

	public void setNotification_title(String notification_title) {
		this.notification_title = notification_title;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public String getNotification_extra() {
		return notification_extra;
	}

	public void setNotification_extra(String notification_extra) {
		this.notification_extra = notification_extra;
	}

	public String getNotification_id() {
		return notification_id;
	}

	public void setNotification_id(String notification_id) {
		this.notification_id = notification_id;
	}

	public String getContent_type() {
		return content_type;
	}

	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	public String getRichpush_html_path() {
		return richpush_html_path;
	}

	public void setRichpush_html_path(String richpush_html_path) {
		this.richpush_html_path = richpush_html_path;
	}

	public String getRichpush_html_res() {
		return richpush_html_res;
	}

	public void setRichpush_html_res(String richpush_html_res) {
		this.richpush_html_res = richpush_html_res;
	}

}