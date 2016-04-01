package com.fpliu.newton.business.account;

import java.io.Serializable;

/**
 * 用户信息
 * 
 * @author 792793182@qq.com 2014-11-05
 * 
 */
public class UserInfo implements Serializable {

	/** 注意：此值不能随意修改 */
	private static final long serialVersionUID = -810358671966354665L;

	/** 此用户的唯一标志 */
	private String userId = "";

	/** 此用户的用户名 */
	private String userName = "";

	/** 此用户的密码 */
	private String password = "";

	/** 此用户的昵称 */
	private String nickName = "";

	/** 此用户的真实姓名 */
	private String realName = "";

	/** 此用户的身份证号 */
	private String idCardNumber = "";

	/** 此用户的支付宝账号 */
	private String alipayNumber = "";

	/** 此用户的所在地 */
	private String city = "";

	/** 此用户的性别 */
	private int gender;

	/** 此用户的手机号 */
	private String phoneNum = "";

	/** 此用户的邮箱 */
	private String email = "";

	/** 此用户的头像 */
	private String avatar = "";

	/** 此用户的积分 */
	private int score;

	/** 此用户的等级名称 */
	private String levelName = "";

	/** 此用户的下一等级名称 */
	private String nextLevelName = "";

	/** 此用户的下一等级的积分 */
	private int nextLevelScore;

	/** 此用户登录的时间 */
	private long signInTime;

	/** 此用户退出的时间 */
	private long signOutTime;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	public String getAlipayNumber() {
		return alipayNumber;
	}

	public void setAlipayNumber(String alipayNumber) {
		this.alipayNumber = alipayNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getNextLevelName() {
		return nextLevelName;
	}

	public void setNextLevelName(String nextLevelName) {
		this.nextLevelName = nextLevelName;
	}

	public int getNextLevelScore() {
		return nextLevelScore;
	}

	public void setNextLevelScore(int nextLevelScore) {
		this.nextLevelScore = nextLevelScore;
	}

	public long getSignInTime() {
		return signInTime;
	}

	public void setSignInTime(long signInTime) {
		this.signInTime = signInTime;
	}

	public long getSignOutTime() {
		return signOutTime;
	}

	public void setSignOutTime(long signOutTime) {
		this.signOutTime = signOutTime;
	}
	
	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", userName=" + userName
				+ ", password=" + password + ", nickName=" + nickName
				+ ", realName=" + realName + ", idCardNumber=" + idCardNumber
				+ ", alipayNumber=" + alipayNumber + ", city=" + city
				+ ", gender=" + gender + ", phoneNum=" + phoneNum + ", email="
				+ email + ", avatar=" + avatar + ", score=" + score
				+ ", levelName=" + levelName + ", nextLevelName="
				+ nextLevelName + ", nextLevelScore=" + nextLevelScore
				+ ", signInTime=" + signInTime + ", signOutTime=" + signOutTime
				+ ", " + super.toString();
	}
}
