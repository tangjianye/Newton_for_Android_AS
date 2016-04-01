package com.fpliu.newton.business.update;


/**
 * 版本更新
 * 
 * @author 792793182@qq.com 2014-9-22
 * 
 */
public final class UpdateResult {
	
	private int versionCodeOnServer;

	public int getVersionCodeOnServer() {
		return versionCodeOnServer;
	}

	@Override
	public String toString() {
		return "UpdateResult [versionCodeOnServer=" + versionCodeOnServer + super.toString();
	}
}
