package com.fpliu.newton.base;

import java.io.File;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;


/**
 * 应用程序的环境
 *
 * @author 792793182@qq.com 2014-9-22
 */
public final class Environment {

    private static final String TAG = Environment.class.getSimpleName();

    private Context mContext;

    private int mScreenWidth;
    private int mScreenHeight;

    private static class InstanceHolder {
        private static Environment instance = new Environment();
    }

    private Environment() {
    }

    public static Environment getInstance() {
        return InstanceHolder.instance;
    }

    public void init(Context context) {
        mContext = context;
    }

    /**
     * 获取系统版本号
     */
    public int getOSVersionCode() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取系统代号
     */
    public String getOSVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取系统唯一标志
     */
    public String getPhoneModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机制造厂商
     */
    public static String getManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获取屏幕宽度
     *
     * @return 屏幕宽度（单位：px）
     */
    public int getScreenWidth() {
        if (0 == mScreenWidth) {
            DisplayMetrics metric = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metric);
            mScreenWidth = metric.widthPixels;
        }
        return mScreenWidth;
    }

    /**
     * 获取屏幕高度
     *
     * @return 屏幕高度（单位：px）
     */
    public int getScreenHeight() {
        if (0 == mScreenHeight) {
            DisplayMetrics metric = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metric);
            mScreenHeight = metric.heightPixels;
        }
        return mScreenHeight;
    }

    /**
     * 获取屏幕密度
     *
     * @return
     */
    public float getScreenDensity() {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metric);
        return metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
    }

    /***
     * 获取应用的名称
     */
    public String getApplicationName() {
        try {
            PackageManager pm = mContext.getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(mContext.getPackageName(), 0);
            String applicationName = (String) pm.getApplicationLabel(applicationInfo);
            return applicationName + "_android";
        } catch (Exception e) {
            DebugLog.e(TAG, "getApplicationName()", e);
        }
        return "";
    }

    /**
     * 获取灵犀的版本号
     *
     * @return
     */
    public int getMyVersionCode() {
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(mContext.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            DebugLog.e(TAG, "getMyVersionCode()", e);
        }
        return 0;
    }

    /**
     * 获取灵犀的版本代号
     *
     * @return
     */
    public String getMyVersionName() {
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(mContext.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            DebugLog.e(TAG, "getMyVersionName()", e);
        }
        return null;
    }

    /**
     * 获取包名
     */
    public String getPackageName() {
        return mContext.getPackageName();
    }

    /**
     * 判断外部存储器是否可用
     */
    public boolean isExternalStorageAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取外部存储器的路径
     */
    public String getExternalStorageDirectory() {
        return android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 手机可用存储空间，不是存储卡空间，返回值以MB单位
     */
    public long getAvailableSize(String path) {
        StatFs fileStats = new StatFs(path);
        fileStats.restat(path);
        return (long) fileStats.getAvailableBlocks() * (long) fileStats.getBlockSize() / 1024 / 1024;
    }

    public boolean isNetworkAvailable() {
        // 获取系统的连接服务
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.isConnected();
    }

    public boolean isWifi() {
        // 获取系统的连接服务
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取网络的连接情况
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 获取IMEI号
     */
    public String getIMEI() {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获取IP地址
     */
    public String getIPAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = inetAddress.getHostAddress();
                        if (!ip.contains("::") && !ip.contains(":")) {// ipV6的地址
                            return ip;
                        }
                    }
                }
            }
        } catch (Exception e) {
            DebugLog.e(TAG, "getDeviceIP()", e);
        }
        return "";
    }

    /**
     * 转换网络类型到字符串
     */
    public static String getNetworkTypeName(Context context) {
        if (context == null) {
            return "";
        }

        String res = "";
        int type = 0;

        TelephonyManager telephonyManager = (TelephonyManager) context.getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);

        if (null != telephonyManager) {
            type = telephonyManager.getNetworkType();
        }

        switch (type) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                res = "GPRS";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                res = "EDGE";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                res = "UMTS";
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                res = "HSDPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                res = "HSUPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                res = "HSPA";
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                res = "CDMA";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                res = "EVDO_0";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                res = "EVDO_A";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                res = "EVDO_B";
                break;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                res = "1xRTT";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                res = "LTE";
                break;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                res = "EHRPD";
                break;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                res = "IDEN";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                res = "HSPAP";
                break;
            default:
                res = "UNKNOWN";
                break;
        }
        return res + ";" + type;
    }

    /**
     * 判断是否是小米的ROM
     */
    public static boolean isMIUIRom() {
        try {
            Class.forName("miui.os.Build");
            return true;
        } catch (Exception e) {
            DebugLog.d(TAG, "isMIUIRom | catch exception");
            return false;
        }
    }

    /**
     * 获取SD卡的路径
     */
    public String getSDPath() {
        if (isExternalStorageAvailable()) {
            return getExternalStorageDirectory() + "/" + mContext.getPackageName();
        }
        return "";
    }

    /**
     * 获取ROM上的私有目录
     */
    public String getInternalDir() {
        return mContext.getFilesDir().getAbsolutePath();
    }

    /**
     * 获取我们的私有的目录，优先在SD卡上
     */
    public String getMyDir() {
        String dir = getSDPath();
        if (TextUtils.isEmpty(dir)) {
            dir = getInternalDir();
        }

        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }

        return dir;
    }

    public StringBuilder getAllInfo() {
        StringBuilder info = new StringBuilder();

        Class<?> clazz = null;
        try {
            clazz = Class.forName("android.os.Build");
        } catch (ClassNotFoundException ex) {
            try {
                clazz = Class.forName("miui.os.Build");
            } catch (ClassNotFoundException e) {
                DebugLog.e(TAG, "getAllInfo()", e);
            }
        }

        if (clazz != null) {
            try {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);

                    info.append(field.getName());
                    info.append(" = ");
                    info.append(field.get(null));
                    info.append("\n");
                }
            } catch (Exception e) {
                DebugLog.e(TAG, "getAllInfo()", e);
            }
        }
        return info;
    }
}
