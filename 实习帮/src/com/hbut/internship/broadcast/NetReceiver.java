package com.hbut.internship.broadcast;

import com.hbut.internship.util.NetWorkUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * 网络连接状态广播
 * 
 * @author Nie
 * 
 */
public class NetReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
			boolean isConnected = NetWorkUtil.isNetworkConnected(context);
			if (isConnected) {

			} else {
				Toast.makeText(context, "已经断开网络", Toast.LENGTH_LONG).show();
			}
		}
	}

}
