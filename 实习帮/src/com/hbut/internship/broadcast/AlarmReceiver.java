package com.hbut.internship.broadcast;

import com.hbut.internship.service.LongRunningService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/*
 * 唤醒后台长期监测申请状态的服务。
 */
public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent it = new Intent(context, LongRunningService.class);
		context.startService(it);
	}

}
