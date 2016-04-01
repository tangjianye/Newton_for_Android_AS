package com.fpliu.newton.business.account;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

import com.fpliu.newton.MyApp;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.base.setting.ISetting;
import com.fpliu.newton.business.account.login.ILogin;
import com.fpliu.newton.business.account.login.LoginFactory;
import com.fpliu.newton.business.account.login.LoginFragment;
import com.fpliu.newton.business.account.login.LoginResult;
import com.fpliu.newton.framework.net.RequestCallback;
import com.fpliu.newton.framework.net.RequestStatus;
import com.fpliu.newton.framework.ui.fragment.BaseFragmentActivity;
import com.fpliu.newton.framework.ui.fragment.FragmentMediator;
import com.fpliu.newton.framework.util.TestSetting;

/**
 * 用户管理
 * 
 * @author 792793182@qq.com 2014-10-15
 * 
 */
public final class UserManager {

	private static final String TAG = UserManager.class.getSimpleName();

	/** 用户信息文件 - 隐藏文件 */
	private static final String FILE_NAME_USER_INFO = "/.userinfo";
	private static final String FILE_NAME_LOGIN_RESULT = "/.loginResult";
	
	/** 用户信息 */
	private UserInfo userInfo;

	/** 正在登录状态 */
	private ILogin logining;
	
	/** 登录成功状态 */
	private ILogin login;

	private static class InstanceHolder {
		private static UserManager instance = new UserManager();
	}
	
	private UserManager() {
		userInfo = getLatestUserInfo();
		login = LoginFactory.newInstance(null);
		logining = LoginFactory.newInstance(null);
	}
	
	public static UserManager getInstance() {
		return InstanceHolder.instance;
	}

	/**
	 * 保存用户信息到文件中
	 * 
	 * @param userInfo
	 *            用户信息
	 */
	public void saveUserInfoInCache(UserInfo userInfo) {
		MyApp.getApp().getSetting().saveObject(getUserDir(userInfo) + FILE_NAME_USER_INFO, userInfo);
	}
	
	/**
	 * 保存用户信息到文件中
	 * 
	 * @param userInfo
	 *            用户信息
	 */
	public static void saveLoginResultInCache(UserInfo userInfo, LoginResult loginResult) {
		MyApp.getApp().getSetting().saveObject(getUserDir(userInfo) + FILE_NAME_LOGIN_RESULT, loginResult);
	}
	
	public static LoginResult readLoginResultInCache(UserInfo userInfo) {
		return (LoginResult) MyApp.getApp().getSetting().readObject(getUserDir(userInfo) + FILE_NAME_USER_INFO);
	}
	
	/**
	 * 定时间段获取AccessToken
	 */
	public static void scheduleGetAccessToken() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (TestSetting.TaskTestSettings.RefreshAccesstoken) {
					final UserInfo userInfo = UserManager.getInstance().userInfo;
					RequestRefreshToken.requestRefreshToken(new RequestCallback<LoginResult>() {

						@Override
						public void callback(LoginResult result, RequestStatus status) {
							if (true) {
								LoginResult loginResult = UserManager.getInstance().getLogin().getLoginResult();
								loginResult.setAccessToken(result.getAccessToken());
								loginResult.setRefreshToken(result.getRefreshToken());
								
								//如果用户信息存在，就保存到对应的用户目录中
								if (userInfo != null) {
									saveLoginResultInCache(userInfo, loginResult);
								}
							}
						}
					});
					
					// 20分钟刷新一次
					try {
						Thread.sleep(1000 * 60 * 20);
					} catch (InterruptedException e) {
						DebugLog.e(TAG, "", e);
					}
				}
			}
		}, "RefreshAccesstoken").start();
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	
	public UserInfo getUserInfo() {
		return userInfo;
	}

	/**
	 * 获取当前用户的目录，如果返回值为空，表明用户还没有登录
	 */
	public String getCurrentUserDir() {
		return getUserDir(userInfo);
	}

	/**
	 * 获取用户目录，如果返回值为空，表明用户还没有登录
	 */
	public static String getUserDir(UserInfo userInfo) {
		if (userInfo != null) {
			String userId = userInfo.getUserId();
			if (!TextUtils.isEmpty(userId)) {
				String tmpDir = Environment.getInstance().getMyDir() + "/user/" + userId;
				File userDir = new File(tmpDir);
				if (userDir.exists()) {
					return tmpDir;
				} else {
					if (userDir.mkdirs()) {
						return tmpDir;
					}
				}
			}
		}
		return "";
	}

	/**
	 * 获取曾经登录过的所有用户信息
	 */
	public List<UserInfo> getAllUserInfos() {
		ISetting setting = MyApp.getApp().getSetting();

		List<UserInfo> userInfos = new ArrayList<UserInfo>();

		File userDir = new File(Environment.getInstance().getMyDir() + "/user/");
		if (userDir.exists()) {
			File[] userIdDirs = userDir.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File file) {
					return file.isDirectory();
				}
			});
			if (userIdDirs != null) {
				for (File userIdDir : userIdDirs) {
					UserInfo userInfo = (UserInfo) setting.readObject(userIdDir.getAbsolutePath() + FILE_NAME_USER_INFO);
					if (userInfo != null) {
						userInfos.add(userInfo);
					}
				}
			}
		}
		return userInfos;
	}

	/**
	 * 获取最后一次登录的用户
	 */
	public UserInfo getLatestUserInfo() {
		UserInfo targetUserInfo = null;

		List<UserInfo> userInfos = getAllUserInfos();
		if (!userInfos.isEmpty()) {
			targetUserInfo = userInfos.get(0);
			for (UserInfo userInfo : userInfos) {
				if (targetUserInfo.getSignInTime() < userInfo.getSignInTime()) {
					targetUserInfo = userInfo;
				}
			}
		}
		DebugLog.d(TAG, "getLatestUserInfo() " + targetUserInfo);
		return targetUserInfo;
	}

	public void checkLogin(final BaseFragmentActivity fragmentActivity) {
		FragmentMediator fragmentMediator = fragmentActivity.getFragmentMediator();
		
		// 如果登录成功过，就进入个人中心界面
		if (login.isLogin()) {
			fragmentMediator.addFragment(fragmentActivity, new IndividualCenterFragment());
		}
		// 如果没有登录成功过，就选择用户进行登录，或者自动登录
		else {
			fragmentMediator.addFragment(fragmentActivity, new LoginFragment());
		}
	}

	/**
	 * 自动登录，刚进入主界面就自动登录
	 */
	public void autoLogin() {
		DebugLog.d(TAG, "autoLogin() Latest userInfo = " + userInfo);

		if (!login.isLogin()) {
			return;
		}
		
		// 获取最后登录的用户
		if (userInfo == null) {
			return;
		}
		
		String userName = userInfo.getUserName();
		String password = userInfo.getPassword();

		// 如果此用户的用户名和密码都不存在了，就不自动登录了
		if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
			return;
		}

		RequestLogin.requestLogin(userName, password, new RequestCallback<LoginResult>() {

			@Override
			public void callback(LoginResult result, RequestStatus status) {
				// 如果自动登录成功
				if (true) {
					RequestUserInfo.requestUserInfo(null);
					
					//刷新Token
					if (TestSetting.TaskTestSettings.LonginFirst) {
						TestSetting.TaskTestSettings.RefreshAccesstoken = true;
						TestSetting.TaskTestSettings.LonginFirst = false;
						scheduleGetAccessToken();
					}
					TestSetting.TraceSettings.TraceMessageType = TestSetting.TraceSettings.TraceMessageType_LOGIN;
				}
			}
		});
	}
	
	public void setLogin(ILogin login) {
		this.login = login;
	}
	
	public ILogin getLogin() {
		return login;
	}

	public ILogin getLogining() {
		return logining;
	}

	public void setLogining(ILogin logining) {
		this.logining = logining;
	}
}
