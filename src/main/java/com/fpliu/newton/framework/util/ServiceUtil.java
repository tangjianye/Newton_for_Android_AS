package com.fpliu.newton.framework.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.IBinder;
import android.text.TextUtils;

/**
 * 服务的帮助类
 * 
 * @author 792793182@qq.com 2015-03-19
 *
 */
public final class ServiceUtil {

	public static final String SERVICE_NAME_SIP = "sip";
	
	public static final String SERVICE_NAME_PHONE = Context.TELEPHONY_SERVICE;
	
	public static final String SERVICE_NAME_SMS = "isms";
	
	public static final String SERVICE_NAME_NFC = Context.NFC_SERVICE;
	
	public static final String SERVICE_NAME_PRINT = Context.PRINT_SERVICE;
	
	public static final String SERVICE_NAME_DREAM = "dreams";
	
	public static final String SERVICE_NAME_WIDGET = "appwidget";
	
	public static final String SERVICE_NAME_BACKUP = "backup";
	
	public static final String SERVICE_NAME_UI_MODE = Context.UI_MODE_SERVICE;
	
	public static final String SERVICE_NAME_SECURITY = "security";
	
	public static final String SERVICE_NAME_SERIAL = "serial";
	
	public static final String SERVICE_NAME_USB = Context.USB_SERVICE;
	
	public static final String SERVICE_NAME_AUDIO = Context.AUDIO_SERVICE;
	
	public static final String SERVICE_NAME_WALLPAPER = Context.WALLPAPER_SERVICE;
	
	public static final String SERVICE_NAME_DROPBOX = Context.DROPBOX_SERVICE;
	
	public static final String SERVICE_NAME_SEARCH = Context.SEARCH_SERVICE;
	
	public static final String SERVICE_NAME_COUNTRY_DETECTOR = "country_detector";
	
	public static final String SERVICE_NAME_LOCATION = Context.LOCATION_SERVICE;
	
	public static final String SERVICE_NAME_NOTIFICATION = Context.NOTIFICATION_SERVICE;
	
	public static final String SERVICE_NAME_UPDATELOCK = "updatelock";
	
	public static final String SERVICE_NAME_SERVICEDISCOVERY = "servicediscovery";
	
	public static final String SERVICE_NAME_CONNECTIVITY = Context.CONNECTIVITY_SERVICE;
	
	public static final String SERVICE_NAME_WIFI = Context.WIFI_SERVICE;
	
	public static final String SERVICE_NAME_WIFI_P2P = Context.WIFI_P2P_SERVICE;
	
	public static final String SERVICE_NAME_NETPOLICY = "netpolicy";
	
	public static final String SERVICE_NAME_NETSTATS = "netstats";
	
	public static final String SERVICE_NAME_TEXT = Context.TEXT_SERVICES_MANAGER_SERVICE;
	
	public static final String SERVICE_NAME_NETWORK_MANAGEMENT = "network_management";
	
	public static final String SERVICE_NAME_CLIPBOARD = Context.CLIPBOARD_SERVICE;
	
	public static final String SERVICE_NAME_STATUSBAR = "statusbar";
	
	public static final String SERVICE_NAME_DEVICE_POLICY = "device_policy";
	
	public static final String SERVICE_NAME_LOCK_SETTINGS = "lock_settings";
	
	public static final String SERVICE_NAME_MOUNT = "mount";
	
	public static final String SERVICE_NAME_ACCESSIBILITY = Context.ACCESSIBILITY_SERVICE;
	
	public static final String SERVICE_NAME_INPUT_METHOD = Context.INPUT_METHOD_SERVICE;
	
	public static final String SERVICE_NAME_BLUETOOTH = Context.BLUETOOTH_SERVICE;
	
	public static final String SERVICE_NAME_INPUT = Context.INPUT_SERVICE;
	
	public static final String SERVICE_NAME_WINDOW = Context.WINDOW_SERVICE;
	
	public static final String SERVICE_NAME_ALARM = Context.ALARM_SERVICE;
	
	public static final String SERVICE_NAME_CONSUMER_IR = Context.CONSUMER_IR_SERVICE;
	
	public static final String SERVICE_NAME_VIBRATOR = Context.VIBRATOR_SERVICE;
	
	public static final String SERVICE_NAME_HARDWARE = "hardware";
	
	public static final String SERVICE_NAME_CONTENT = "content";
	
	public static final String SERVICE_NAME_ACCOUNT = Context.ACCOUNT_SERVICE;
	
	public static final String SERVICE_NAME_USER = Context.USER_SERVICE;
	
	public static final String SERVICE_NAME_PERMISSION = "permission";
	
	public static final String SERVICE_NAME_PROCSTATS = "procstats";
	
	public static final String SERVICE_NAME_ACTIVITY = Context.ACTIVITY_SERVICE;
	
	public static final String SERVICE_NAME_PACKAGE = "package";
	
	public static final String SERVICE_NAME_DISPLAY = "display";
	
	public static final String SERVICE_NAME_POWER = Context.POWER_SERVICE;
	
	public static final String SERVICE_NAME_SENSOR = Context.SENSOR_SERVICE;
	
	public static final String SERVICE_NAME_CAMERA = "media.camera";
	
	public static final String SERVICE_NAME_MEDIA_PLAYER = "media.player";
	
	public static final String SERVICE_NAME_KEYSTORE = "android.security.keystore";
	
	private static final Map<String, String> MAP = new HashMap<String, String>();
	
	/**
	 * 这个映射表可以通过adb shell service list进行查看
	 */
	static {
		MAP.put(SERVICE_NAME_SIP, "android.net.sip.ISipService");
		MAP.put(SERVICE_NAME_PHONE, "com.android.internal.telephony.ITelephony");
		MAP.put(SERVICE_NAME_SMS, "com.android.internal.telephony.ISms");
		MAP.put(SERVICE_NAME_NFC, "android.nfc.INfcAdapter");
		MAP.put(SERVICE_NAME_PRINT, "android.print.IPrintManager");
		MAP.put(SERVICE_NAME_DREAM, "android.service.dreams.IDreamManager");
		MAP.put(SERVICE_NAME_WIDGET, "com.android.internal.appwidget.IAppWidgetService");
		MAP.put(SERVICE_NAME_BACKUP, "android.app.backup.IBackupManager");
		MAP.put(SERVICE_NAME_UI_MODE, "android.app.IUiModeManager");
		MAP.put(SERVICE_NAME_SECURITY, "miui.security.ISecurityManager");
		MAP.put(SERVICE_NAME_SERIAL, "android.hardware.ISerialManager");
		MAP.put(SERVICE_NAME_USB, "android.hardware.usb.IUsbManager");
		MAP.put(SERVICE_NAME_AUDIO, "android.media.IAudioService");
		MAP.put(SERVICE_NAME_WALLPAPER, "android.app.IWallpaperManager");
		MAP.put(SERVICE_NAME_DROPBOX, "com.android.internal.os.IDropBoxManagerService");
		MAP.put(SERVICE_NAME_SEARCH, "android.app.ISearchManager");
		MAP.put(SERVICE_NAME_COUNTRY_DETECTOR, "android.location.ICountryDetector");
		MAP.put(SERVICE_NAME_LOCATION, "android.location.ILocationManager");
		MAP.put(SERVICE_NAME_NOTIFICATION, "android.app.INotificationManager");
		MAP.put(SERVICE_NAME_UPDATELOCK, "android.os.IUpdateLock");
		MAP.put(SERVICE_NAME_SERVICEDISCOVERY, "android.net.nsd.INsdManager");
		MAP.put(SERVICE_NAME_CONNECTIVITY, "android.net.IConnectivityManager");
		MAP.put(SERVICE_NAME_WIFI, "android.net.wifi.IWifiManager");
		MAP.put(SERVICE_NAME_WIFI_P2P, "android.net.wifi.p2p.IWifiP2pManager");
		MAP.put(SERVICE_NAME_NETPOLICY, "android.net.INetworkPolicyManager");
		MAP.put(SERVICE_NAME_NETSTATS, "android.net.INetworkStatsService");
		MAP.put(SERVICE_NAME_TEXT, "com.android.internal.textservice.ITextServicesManager");
		MAP.put(SERVICE_NAME_NETWORK_MANAGEMENT, "android.os.INetworkManagementService");
		MAP.put(SERVICE_NAME_CLIPBOARD, "android.content.IClipboard");
		MAP.put(SERVICE_NAME_STATUSBAR, "com.android.internal.statusbar.IStatusBarService");
		MAP.put(SERVICE_NAME_DEVICE_POLICY, "android.app.admin.IDevicePolicyManager");
		MAP.put(SERVICE_NAME_LOCK_SETTINGS, "com.android.internal.widget.ILockSettings");
		MAP.put(SERVICE_NAME_MOUNT, "android.os.storage.IMountService");
		MAP.put(SERVICE_NAME_ACCESSIBILITY, "android.view.accessibility.IAccessibilityManager");
		MAP.put(SERVICE_NAME_INPUT_METHOD, "com.android.internal.view.IInputMethodManager");
		MAP.put(SERVICE_NAME_BLUETOOTH, "android.bluetooth.IBluetoothManager");
		MAP.put(SERVICE_NAME_INPUT, "android.hardware.input.IInputManager");
		MAP.put(SERVICE_NAME_WINDOW, "android.view.IWindowManager");
		MAP.put(SERVICE_NAME_ALARM, "android.app.IAlarmManager");
		MAP.put(SERVICE_NAME_CONSUMER_IR, "android.hardware.IConsumerIrService");
		MAP.put(SERVICE_NAME_VIBRATOR, "android.os.IVibratorService");
		MAP.put(SERVICE_NAME_HARDWARE, "android.os.IHardwareService");
		MAP.put(SERVICE_NAME_CONTENT, "android.content.IContentService");
		MAP.put(SERVICE_NAME_ACCOUNT, "android.accounts.IAccountManager");
		MAP.put(SERVICE_NAME_USER, "android.os.IUserManager");
		MAP.put(SERVICE_NAME_PERMISSION, "android.os.IPermissionController");
		MAP.put(SERVICE_NAME_PROCSTATS, "com.android.internal.app.IProcessStats");
		MAP.put(SERVICE_NAME_ACTIVITY, "android.app.IActivityManager");
		MAP.put(SERVICE_NAME_PACKAGE, "android.content.pm.IPackageManager");
		MAP.put(SERVICE_NAME_DISPLAY, "android.hardware.display.IDisplayManager");
		MAP.put(SERVICE_NAME_POWER, "android.os.IPowerManager");
		MAP.put(SERVICE_NAME_SENSOR, "android.gui.SensorServer");
		MAP.put(SERVICE_NAME_CAMERA, "android.hardware.ICameraService");
		MAP.put(SERVICE_NAME_MEDIA_PLAYER, "android.media.IMediaPlayerService");
		MAP.put(SERVICE_NAME_KEYSTORE, "android.security.keystore");
		
		
//				74	media.audio_policy: [android.media.IAudioPolicyService]
//				75	scheduling_policy: [android.os.ISchedulingPolicyService]
//				76	telephony.registry: [com.android.internal.telephony.ITelephonyRegistry]
//				78	appops: [com.android.internal.app.IAppOpsService]
//				79	usagestats: [com.android.internal.app.IUsageStats]
//				80	batterystats: [com.android.internal.app.IBatteryStats]
//				83	listen.service: [com.qualcomm.listen.IListenService]
//				84	SurfaceFlinger: [android.ui.ISurfaceComposer]
//				85	display.qservice: [android.display.IQService]
//				86	mdb.remote_display: [mdb.IRemoteDisplayService]
//				88	drm.drmManager: [drm.IDrmManagerService]
//				91	media.audio_flinger: [android.media.IAudioFlinger]
//				92	miui.shell: [miui.IShellService]
//				93	batterypropreg: [android.os.IBatteryPropertiesRegistrar]

	}
	
	private ServiceUtil() { }
	
	/**
	 * 列出所有的系统服务
	 */
	public static ArrayList<String> listSystemServices() {
		ArrayList<String> result = new ArrayList<String>();
		
		try {
			Class<?> serviceManagerClass = Class.forName("android.os.ServiceManager");
			Method getServiceMethod = serviceManagerClass.getDeclaredMethod("listServices");
			getServiceMethod.setAccessible(true);
			String[] services = (String[]) getServiceMethod.invoke(null);
			if (services != null) {
				for (String service : services) {
					result.add(service);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 获取指定的系统服务
	 * @param name
	 * @return
	 */
	public static Object getSystemService(String name) {
		if (TextUtils.isEmpty(name)) {
			return null;
		}
		
		try {
			Class<?> serviceManagerClass = Class.forName("android.os.ServiceManager");
			Method getServiceMethod = serviceManagerClass.getDeclaredMethod("getService", String.class);
			getServiceMethod.setAccessible(true);
			IBinder binder = (IBinder) getServiceMethod.invoke(null, name);
			
			String interfaceName = MAP.get(name);
			if (TextUtils.isEmpty(interfaceName)) {
				return null;
			}
			
			Class<?> serviceClass = Class.forName(interfaceName + "$Stub");
			Method asInterfaceMethod = serviceClass.getDeclaredMethod("asInterface", IBinder.class);
			return asInterfaceMethod.invoke(null, binder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取指定的系统服务中的方法
	 */
	public static ArrayList<String> getSystemServiceMethods(String name) {
		ArrayList<String> result = new ArrayList<String>();
		
		Object service = getSystemService(name);
		Method[] methods = service.getClass().getDeclaredMethods();
		
		if (methods != null) {
			for (Method method : methods) {
				result.add(method.getName());
			}
		}
		
		return result;
	}
}
