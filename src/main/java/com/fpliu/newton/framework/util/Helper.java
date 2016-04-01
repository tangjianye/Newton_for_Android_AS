package com.fpliu.newton.framework.util;

import java.io.File;
import java.text.SimpleDateFormat;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.fpliu.newton.base.DebugLog;

/**
 * 公共的帮助类
 * 
 * @author 792793182@qq.com 2014-11-02
 * 
 */
public final class Helper {

	private static final String TAG = Helper.class.getSimpleName();
	
	/**
	 * 格式化时间类型(yyyy-MM-dd,HH:mm:ss)
	 * */
	public static String getSimpleDateFormat0(long time) {
		return new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss").format(time);
	}

	public static String getSimpleDateFormat9(long time) {
		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(time);
	}
	public static String getResourcesString(Context context,int stringId) {
		return context.getResources().getString(stringId);
	}

	/**
	 * 判断目录是否存在，没有就创建
	 * 
	 * @param path
	 * @return path
	 */
	public static String createDirIfNotExistsReturnPath(String path) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			File file = new File(android.os.Environment.getExternalStorageDirectory(),path);
			if (!file.exists()) {
				if (!file.mkdirs()) {
					DebugLog.d(TAG, "Problem creating Image folder");
				}
			}
		}
		return path;
	}
	
	//当前时间
	public static String getUtilSimpleDateFormat0(){
		
		return getSimpleDateFormat0(System.currentTimeMillis());
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 下载语料请求
	 * 
	 * ****/
	public static String sendGetForGetCorpus(String url2, String directive,
			int taskNumber) {
		String rev = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			// String uri = "http://192.168.1.45:8082/api/metadata/get?id=1";
			HttpGet get = new HttpGet(url2);
			get.addHeader("Authorization", "Bearer " + directive);
			get.addHeader("Content-Type", "application/json");
			get.addHeader("User-Agent", "your agent");
			HttpResponse response;
			response = httpclient.execute(get);
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				rev = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			DebugLog.e(TAG, "sendGetForGetCorpus()", e);
		}

		return rev;

	}

	/**
	 * 下载语料请求
	 * 
	 * ****/
	public static String sendGetForGetCorpusByDefault(String url2,
			String directive, int taskNumber) {
		String rev = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			// String uri =
			// "http://192.168.1.45:8082/api/metadata/get_default?id=1";
			HttpGet get = new HttpGet(url2);
			get.addHeader("Authorization", "Bearer " + directive);
			get.addHeader("Content-Type", "application/json");
			get.addHeader("User-Agent", "your agent");
			HttpResponse response;
			response = httpclient.execute(get);
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				rev = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			DebugLog.e(TAG, "sendGetForGetCorpusByDefault()", e);
		}

		return rev;
	}

	/**
	 * 判断网络连接类型
	 * 
	 * @param telephoneManager
	 * @return
	 */
	public static String isConnectNetwork(Context context) {
		String mobileNetWorkType = "N/A";
		ConnectivityManager connectMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectMgr.getActiveNetworkInfo();
		if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
			int subType = info.getSubtype();
			if (subType == TelephonyManager.NETWORK_TYPE_GPRS
					|| subType == TelephonyManager.NETWORK_TYPE_CDMA
					|| subType == TelephonyManager.NETWORK_TYPE_IDEN
					|| subType == TelephonyManager.NETWORK_TYPE_EDGE) {
				mobileNetWorkType = "2G";
			} else if (subType == TelephonyManager.NETWORK_TYPE_EVDO_0
					|| subType == TelephonyManager.NETWORK_TYPE_EVDO_A
					|| subType == TelephonyManager.NETWORK_TYPE_HSDPA
					|| subType == TelephonyManager.NETWORK_TYPE_HSPA
					|| subType == TelephonyManager.NETWORK_TYPE_EHRPD

					|| subType == TelephonyManager.NETWORK_TYPE_HSUPA
					|| subType == TelephonyManager.NETWORK_TYPE_UMTS
					|| subType == TelephonyManager.NETWORK_TYPE_1xRTT
					|| subType == 15) {
				mobileNetWorkType = "3G";
			} else if (subType == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
				mobileNetWorkType = "未知";
			} else if (subType == 13) {
				mobileNetWorkType = "LTE";
			} else {
				mobileNetWorkType = newAddNetwork(context);
			}
		} else if (info != null
				&& info.getType() == ConnectivityManager.TYPE_WIFI) {
			mobileNetWorkType = "WIFI";
		} else {
			mobileNetWorkType = newAddNetwork(context);
		}
		return mobileNetWorkType;
	}

	public static String newAddNetwork(Context context) {
		if (getcurrentNetworkNoWifi(context) != null
				&& !"".equals(getcurrentNetworkNoWifi(context))) {
			if ("TD_SCDMA".equals(getcurrentNetworkNoWifi(context))) {
				return "3G";
			} else if ("TD-SCDMA".equals(getcurrentNetworkNoWifi(context))) {
				return "3G";
			} else if ("TDS_HSDPA".equals(getcurrentNetworkNoWifi(context))) {
				return "3G";
			} else if (getcurrentNetworkNoWifi(context).contains("TDS")
					|| getcurrentNetworkNoWifi(context).contains("HSDPA")
					|| getcurrentNetworkNoWifi(context).contains("HSPA")) {
				return "3G";
			} else {
				return "N/A";
			}
		} else {
			return "N/A";
		}
	}

	public static String getcurrentNetworkNoWifi(Context context) {
		String network_type = "UNKNOWN";// maybe usb reverse tethering
		NetworkInfo active_network = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if (active_network != null && active_network.isConnectedOrConnecting()) {
			network_type = active_network.getSubtypeName();
		}
		return network_type;
	}

	public static String getWifiStatus(Context context) {
		String wifiStatus = "";
		ConnectivityManager conMan = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();

		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		String ssid = wifiInfo.getSSID();

		if (wifi == State.CONNECTED) {
			wifiStatus = "无线网络已连接, " + "SSID: " + ssid;
		} else if (wifi == State.CONNECTING) {
			wifiStatus = "无线网络正在连接, " + "SSID: " + ssid;
		} else if (wifi == State.DISCONNECTED) {
			wifiStatus = "无线网络已断开";
		} else if (wifi == State.DISCONNECTING) {
			wifiStatus = "无线网络正在断开, " + "SSID: " + ssid;
		} else if (wifi == State.SUSPENDED) {
			wifiStatus = "无线网络挂起";
		} else if (wifi == State.UNKNOWN) {
			wifiStatus = "无线网络未连接";
		}
		return wifiStatus;
	}
}
