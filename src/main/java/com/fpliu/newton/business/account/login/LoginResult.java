package com.fpliu.newton.business.account.login;


/**
 * 登录返回的结果
 * 
 * @author 792793182@qq.com 2014-10-8
 * 
 */
public class LoginResult {

	private LoginType loginType = LoginType.NONE;
	
	private String accessToken = "";
	private String refreshToken = "";
	private String tokenType = "";
	private int expiresIn;
	
	/** 登录的用户名 */
	private String userName;

	/** 登录的密码 */
	private String password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	public LoginType getLoginType() {
		return loginType;
	}

	public void setLoginType(LoginType loginType) {
		this.loginType = loginType;
	}

	@Override
	public String toString() {
		return "LoginResult [loginType=" + loginType + ", accessToken="
				+ accessToken + ", refreshToken=" + refreshToken
				+ ", tokenType=" + tokenType + ", expiresIn=" + expiresIn
				+ ", userName=" + userName + ", password=" + password + ","
				+ super.toString();
	}
}
