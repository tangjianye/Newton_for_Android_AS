package com.fpliu.newton.business.config;

/**
 * 
 * 服务端返回的配置
 * 
 * @author 792793182@qq.com 2014-10-29
 *
 */
public final class Configuration {
	
	/** 渠道号，打渠道包时需要替换此值 */
	public static final String CHANNEL_ID = "100000";
	
	public static final String DB_NAME = "dbv1";
	
	private int withdrawalQuota = 5;

	private String azure = "";
	
	public int getWithdrawalQuota() {
		return withdrawalQuota;
	}

	public void setWithdrawalQuota(int withdrawalQuota) {
		this.withdrawalQuota = withdrawalQuota;
	}
	
	public String getAzureConf() {
		return azure;
	}
	
	@Override
	public String toString() {
		return "Configuration [withdrawalQuota=" + withdrawalQuota
				 + super.toString();
	}
}