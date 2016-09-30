package com.hbut.internship.service;

import java.util.ArrayList;
import java.util.List;

import com.hbut.internship.R;
import com.hbut.internship.broadcast.AlarmReceiver;
import com.hbut.internship.util.Internet;
import com.hbut.internship.util.MyApplication;
import com.internship.model.Apply;
import com.internship.model.Position;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;

/*
 * 监测申请过的职位申请状态变更！
 */
public class LongRunningService extends Service {

	private List<Apply> mList = new ArrayList<Apply>();
	private SharedPreferences preferences;
	private int i;
	private Position position;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			position = (Position) msg.obj;
			String poname = position.getPoName();
			NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			Notification notification = new Notification(R.drawable.icon,
					"你的申请状态更新啦！", System.currentTimeMillis());
			notification.setLatestEventInfo(MyApplication.getContext(), poname
					+ "申请状态更新啦！", "你申请的" + poname + "这个职位状态已更新，请及时查看", null);
			manager.notify(1, notification);

		};
	};

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				preferences = getSharedPreferences("Student", MODE_PRIVATE);
				try {
					mList = Internet.GetApply(preferences
							.getInt("studentID", 0));
					int len = mList.size();
					for (i = 0; i < len; i++) {
						if (mList.get(i).getApply_state() == 2) {
							new Thread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									Message msg = new Message();
									msg.obj = mList.get(i).getPosition();
									handler.sendMessage(msg);
								}
							}).start();
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = 10 * 1000;// 这是一分钟的毫秒数
		long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
		Intent i = new Intent(this, AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		return super.onStartCommand(intent, flags, startId);
	}

}
