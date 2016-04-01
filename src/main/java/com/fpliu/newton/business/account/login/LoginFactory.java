package com.fpliu.newton.business.account.login;

/**
 * 登录工厂
 * @author 792793182@qq.com 2015-03-03
 *
 */
public final class LoginFactory {

	private LoginFactory() {}
	
	public static ILogin newInstance(LoginType loginType) {
		if (loginType == LoginType.Newton) {
			return new NewtonLogin();
		} else if (loginType == LoginType.QQ) {
			return new QQLogin();
		} else if (loginType == LoginType.WeiXin) {
			return new WeixinLogin();
		} else if (loginType == LoginType.SinaWeibo) {
			return new SinaWeiboLogin();
		} else {
			return new NoLogin();
		}
	}
}
