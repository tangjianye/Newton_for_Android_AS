package com.fpliu.newton.business.config;

/**
 * 请求服务端的url配置
 * 
 * @author 792793182@qq.com 2014-9-22
 *
 */
public final class UrlConfig {
	
	private UrlConfig() { }

	
	/**账号中心baseUrl1*/
	private static final String baseUrl = "";
	
	/**数据中心baseUrl2*/
	private static final String baseUrl2 = "";
	
	/** 生产环境和测试环境根据连接的WIFI自动解析域名 */
/** -------------------------------------------------------------------------------------------------- */
	
	/** 第三方登录 */
	public final static String thirdPartLoginUrl = baseUrl + "";
	
	/** 登录 */
	public final static String loginUrl = baseUrl + "";
	
	/** 注册*/
	public final static String registerUrl = baseUrl + "";
	
	/** 修改密码 */
	public final static String changePasswordUrl = baseUrl + "";
	
	/** 获取验证码 */
	public final static String registerGetVerificationUrl = baseUrl + "";
	
	/** 校验验证码 */
	public final static String registerVerificationUrl = baseUrl + "";
	
	/** 绑定手机号*/
	public final static String getBindPhoneUrl = baseUrl + "";
	
	/** 上传头像 */
	public final static String getPostAvatarUrl = baseUrl + "";
	
/** -------------------------------------------------------------------------------------------------- */
	
	/** 获取服务端的配置 */
	public static final String GET_CONFIGURATION = baseUrl2 + "";
	
	/** 保存个人基本信息（完善或者修改个人信息时，城市，昵称，性别）*/
	public final static String getsavePersonUrl = baseUrl2 + "";
	
	/** 获取用户信息 */
	public final static String getUserInfoUrl = baseUrl2 + "";

	/**数据结果上传 - 以base64编码的形式提交数据 */
	public final static String getUploadFileUrl = baseUrl2 + "";
	
	/**数据结果上传 - 以表单的形式提交数据 */
	public final static String getUploadFileUrl2 = baseUrl2 + "";

	
	/** 跟踪反馈 */
	public final static String PostTraceMessageUrl = baseUrl2 + "";
	
	/** 上传奔溃日志 */
	public final static String POST_UNCAUGHT_EXCEPTION = baseUrl2 + "";
	
	
	
	/** 更新配置 */
	public static final String UPDATE_VERSION = "";
	
	/** 更新的APK */
	public static final String UPDATE_APK = "";
	
	public static String getAuthorization(String accessToken) {
		return "Bearer " + accessToken;
	}
}
