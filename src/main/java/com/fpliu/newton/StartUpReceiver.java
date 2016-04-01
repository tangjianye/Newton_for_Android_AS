package com.fpliu.newton;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 开机启动的广播接收者
 * 很多权限控制软件禁止了开机启动，需要设置开启才能接收到该广播
 * 
 * @author 792793182@qq.com 2014-9-22
 * 
 */
public final class StartUpReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
			context.startService(new Intent(context, BackgroudService.class));
		}
	}
}
